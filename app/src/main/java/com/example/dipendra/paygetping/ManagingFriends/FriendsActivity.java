package com.example.dipendra.paygetping.managingFriends;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

public class FriendsActivity extends BaseActivity implements View.OnClickListener {
    private FloatingActionButton fab;
    private ListView listView;
    FirebaseListAdapter<User> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        initialize();
        this.reference = Constants.getDatabase().getReference().child("userFriends").child(user.getEncodedEmail());
        adapter = new FirebaseListAdapter<User>(this, User.class, R.layout.friend_list, reference) {
            @Override
            protected void populateView(View view, final User user, int i){
                ((TextView)view.findViewById(R.id.listfriend_name)).setText(user.getName());
                ((TextView)view.findViewById(R.id.listfriend_mail)).setText(user.getEncodedEmail().replace(",","."));
                Button b = (Button) view.findViewById(R.id.delete_this_friend);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Constants.getDatabase().getReference().child("userFriends").child(FriendsActivity.this.user.getEncodedEmail()).child(user.getEncodedEmail()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(FriendsActivity.this, "Task Unsuccessful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FriendsActivity.this, "Task successful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        };
        listView.setAdapter(adapter);
    }
    private void initialize(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fabfriend);
        fab.setOnClickListener(this);
        readSharedPreferences();
        listView = (ListView) findViewById(R.id.listfriends);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adapter!=null)
            adapter.cleanup();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fabfriend){
            Intent i = new Intent(FriendsActivity.this, AddFriendsActivity.class);
            Bundle options =
                    ActivityOptions.makeCustomAnimation(this, R.xml.left, R.xml.right).toBundle();
            startActivity(i, options);
        }
    }
}
