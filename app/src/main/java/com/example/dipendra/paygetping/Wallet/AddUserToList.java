package com.example.dipendra.paygetping.wallet;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dipendra.paygetping.BaseActivity;
import com.example.dipendra.paygetping.R;
import com.example.dipendra.paygetping.models.User;
import com.example.dipendra.paygetping.utils.Constants;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
                Button b = (Button) view.findViewById(R.id.delete_this_friend);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Constants.getDatabase().getReference().child("wallets").child(currentList.getPushId()).child("sharedWith").child(user.getEncodedEmail()).setValue(null);
                        Constants.getDatabase().getReference().child("users").child(user.getEncodedEmail()).child("lists").child(currentList.getPushId()).setValue(null);
                        Constants.getDatabase().getReference().child("wallets").child(currentList.getPushId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.hasChild("sharedWith")){
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
                });
            }

        };
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