package com.example.dipendra.paygetping.wallet;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dipendra.paygetping.BaseActivity;
import com.example.dipendra.paygetping.R;
import com.example.dipendra.paygetping.models.User;
import com.example.dipendra.paygetping.utils.Constants;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddUserToList extends BaseActivity implements View.OnClickListener {
    private ListView listView;
    private FirebaseListAdapter<User>adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_to_list);
        initialize();
        Query q = Constants.getDatabase().getReference().child("wallets").child(currentList.getPushId()).child("sharedWith");
        adapter = new FirebaseListAdapter<User>(this, User.class, R.layout.friend_list, q) {
            @Override
            protected void populateView(View view, final User user, int i) {
                ((TextView)view.findViewById(R.id.listfriend_name)).setText(user.getName());
                ((TextView)view.findViewById(R.id.listfriend_mail)).setText(user.getEncodedEmail().replace(",","."));
                ImageButton b = (ImageButton) view.findViewById(R.id.delete_this_friend);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(user.getEncodedEmail().equals(AddUserToList.this.user.getEncodedEmail())){
                            Toast.makeText(AddUserToList.this, "You are the owner, You can't Backout!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Map<String, Object> map = new HashMap<String, Object>();
                            progressDialog.setMessage("Wait!!");
                            progressDialog.show();
                            map.put("/wallets/" + currentList.getPushId() + "/sharedWith/" + user.getEncodedEmail() + "/", null);
                            map.put("/users/" + user.getEncodedEmail() + "/lists/" + currentList.getPushId() + "/", null);
                            Constants.getDatabase().getReference().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.hide();
                                    } else {
                                        Toast.makeText(AddUserToList.this, "Unsuccessful!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            Constants.getDatabase().getReference().child("wallets").child(currentList.getPushId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild("sharedWith")) {
                                        Constants.getDatabase().getReference().child("wallets").child(currentList.getPushId()).setValue(null);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("currentWallet", null);
                                        editor.putString("currentWalletName", null);
                                        editor.putString("currentWalletOwner", null);
                                        editor.commit();
                                        AddUserToList.this.finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
            }

        };
        progressDialog.setMessage("Refreshing..");
        progressDialog.show();
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                progressDialog.hide();
            }
        });
        listView.setAdapter(adapter);

    }

    private void initialize() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_to_user_list);
        fab.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.sharedWith);
        readSharedPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adapter!=null){
            adapter.cleanup();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab_add_to_user_list){
            Intent i = new Intent(AddUserToList.this, SelectFriendActivity.class);
            Bundle options =
                    ActivityOptions.makeCustomAnimation(this, R.xml.left, R.xml.right).toBundle();
            startActivity(i, options);
        }
    }
}