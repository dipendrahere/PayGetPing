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

import com.example.dipendra.paygetping.login.LoginActivity;
import com.example.dipendra.paygetping.login.RegisterActivity;
import com.example.dipendra.paygetping.models.User;
import com.example.dipendra.paygetping.models.WalletListWrapper;
import com.example.dipendra.paygetping.utils.Constants;
import com.example.dipendra.paygetping.wallet.AddUserToList;
import com.example.dipendra.paygetping.wallet.WalletsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

    public User getUser() {
        return user;
    }

    public WalletListWrapper getCurrentList() {
        return currentList;
    }

    protected WalletListWrapper currentList;
    protected DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Base", "Yeh chal raha hai");
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if(firebaseUser == null){
                    getToLoginScreen();
                }
                else{
                    readSharedPreferences();
                    try {
                        if (currentList.getOwnerID() != null && currentList.getOwnerID().equals(user.getEncodedEmail()) && BaseActivity.this instanceof MainActivity) {
                            Constants.menu = R.menu.menu_main;
                        }
                        else  {
                            Constants.menu = R.menu.menu_main_1;
                        }
                    }catch (Exception e){
                        Log.e("TAG:: MAIN ACTIVITY", "Menu not yet created");
                    }
                    invalidateOptionsMenu();
                }
            }
        };
        initialize();
    }
    private void initialize(){
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        user = new User();
        currentList = new WalletListWrapper();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        if(Constants.isOnline(this) == false){
            Toast.makeText(this, "Working Off-the line is not good! Please be online!", Toast.LENGTH_SHORT).show();
        }
    }
    private void getToLoginScreen(){
        Intent i = new Intent(BaseActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    protected void readSharedPreferences(){
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        user.setEncodedEmail(sp.getString("encodedEmail", null));
        user.setName(sp.getString("username", null));
        String nameOfTheCurrentList = sp.getString("currentWalletName", null);
        if(nameOfTheCurrentList == null && !(this instanceof WalletsActivity)){
            Intent i = new Intent(this, WalletsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else{
            currentList.setName(nameOfTheCurrentList);
        }
        currentList.setOwner(sp.getString("currentWalletOwner", null));
        currentList.setPushId(sp.getString("currentWallet", null));
        currentList.setOwnerID(sp.getString("currentWalletOwnerID", null));

       // Toast.makeText(this, "You are:"+ user.getName()+user.getEncodedEmail(), Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(!(this instanceof LoginActivity || this instanceof RegisterActivity)){
            auth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                    reference = Constants.getDatabase().getReference().child("users").child(mail);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(Constants.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.logout){
            signOutUser();
        }
        else if(id == R.id.add_user_to_list){
            Intent i = new Intent(BaseActivity.this, AddUserToList.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    protected void signOutUser(){
        auth.signOut();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("encodedEmail", null);
        editor.putString("username", null);
        editor.putString("currentWallet", null);
        editor.putString("currentWalletName", null);
        editor.putString("currentWalletOwner", null);
        editor.commit();
        user = null;
        getToLoginScreen();
    }

}
