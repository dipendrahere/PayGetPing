package com.example.dipendra.paygetping.wallet;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.dipendra.paygetping.BaseActivity;
import com.example.dipendra.paygetping.R;
import com.example.dipendra.paygetping.models.User;
import com.example.dipendra.paygetping.utils.Constants;
import com.example.dipendra.paygetping.utils.FriendAddToListAdapter;
import com.google.firebase.database.Query;

public class SelectFriendActivity extends BaseActivity {
    private EditText autoCompleteTextView;
    private String searchString;
    private ListView addedUsers;
    private FriendAddToListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        initialize();
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(adapter != null){
                    adapter.cleanup();
                }
                searchString = autoCompleteTextView.getText().toString().toLowerCase();
                if(searchString.equals("") || searchString.length() < 2){
                    addedUsers.setAdapter(null);
                    findViewById(R.id.nomatch).setVisibility(View.VISIBLE);
                }
                else{
                    Query q = Constants.getDatabase().getReference().child("userFriends").child(user.getEncodedEmail()).
                            orderByChild("encodedEmail").startAt(searchString).endAt(searchString+"~");
                    adapter = new FriendAddToListAdapter(SelectFriendActivity.this, User.class, R.layout.row_add_friends, q , SelectFriendActivity.this.user.getEncodedEmail());
                    addedUsers.setAdapter(adapter);
                    if(adapter.getCount() == 0){
                        findViewById(R.id.nomatch).setVisibility(View.VISIBLE);
                    }
                    else{
                        findViewById(R.id.nomatch).setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void initialize() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        autoCompleteTextView = (EditText) findViewById(R.id.searchfriends);
        addedUsers = (ListView) findViewById(R.id.added_users);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adapter!=null)
            adapter.cleanup();
    }
}
