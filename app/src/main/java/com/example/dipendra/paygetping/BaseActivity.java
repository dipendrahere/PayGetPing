package com.example.dipendra.paygetping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dipendra.paygetping.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dipendra on 06/04/17.
 */

public class BaseActivity extends AppCompatActivity {
    protected FirebaseAuth auth;
    protected ProgressDialog progressDialog;
    protected SharedPreferences sp;
    protected FirebaseAuth.AuthStateListener authStateListener;
    protected User user;
    protected DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Base", "Yeh chal raha hai");
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if(user == null){
                    getToLoginScreen();
                }
                else{
                    readSharedPreferences();
                }
            }
        };
        initialize();
    }
    private void initialize(){
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        user = new User();
    }
    private void getToLoginScreen(){
        Intent i = new Intent(BaseActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    private void readSharedPreferences(){
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        user.setEncodedEmail(sp.getString("encodedEmail", null));
        user.setName(sp.getString("username", null));
        Toast.makeText(this, "You are:"+ user.getName()+user.getEncodedEmail(), Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(!(this instanceof LoginActivity || this instanceof RegisterActivity)){
            auth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            if(!(this instanceof LoginActivity || this instanceof RegisterActivity)) {
                auth.removeAuthStateListener(authStateListener);
            }
        }
    }
    protected void signin(String usermail, String passwordString){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in.. Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        final String mail = usermail.replace(".",",");
        auth.signInWithEmailAndPassword(usermail, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    progressDialog.hide();
                    Toast.makeText(BaseActivity.this, "Ohho! Some Problem like this occurred: "+task.getException(),Toast.LENGTH_LONG).show();
                }
                else{
                    final SharedPreferences.Editor ed = sp.edit();
                    reference = FirebaseDatabase.getInstance().getReference().child("users").child(mail);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);
                            ed.putString("username", user.getName());
                            ed.putString("encodedEmail", user.getEncodedEmail());
                            ed.commit();
                            Intent i = new Intent(BaseActivity.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            progressDialog.hide();
                            startActivity(i);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.hide();
                            Toast.makeText(BaseActivity.this, "Some Error Occured",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private void signOutUser(){
        auth.signOut();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("encodedEmail", null);
        editor.putString("username", null);
        editor.commit();
        user = null;
        getToLoginScreen();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            signOutUser();
        }

        return super.onOptionsItemSelected(item);
    }
}
