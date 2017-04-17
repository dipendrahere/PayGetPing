package com.example.dipendra.paygetping.managingFriends;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dipendra.paygetping.BaseActivity;
import com.example.dipendra.paygetping.R;
import com.example.dipendra.paygetping.models.User;
import com.example.dipendra.paygetping.utils.AddFriendAdapter;
import com.example.dipendra.paygetping.utils.Constants;
import com.google.firebase.database.Query;

public class AddFriendsActivity extends BaseActivity {
    private EditText autoCompleteTextView;
    private String searchString;
    private ListView addedUsers;
    private AddFriendAdapter adapter;
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
              //  progressDialog.show();
                if(adapter != null){
                    adapter.cleanup();
                }
                searchString = autoCompleteTextView.getText().toString().toLowerCase();
                if(searchString.equals("") || searchString.length() < 2){
                    addedUsers.setAdapter(null);
                    findViewById(R.id.nomatch).setVisibility(View.VISIBLE);
                }
                else{
                    Query q = Constants.getDatabase().getReference().child("users").
                            orderByChild("encodedEmail").startAt(searchString).endAt(searchString+"~");
                    adapter = new AddFriendAdapter(AddFriendsActivity.this, User.class, R.layout.row_add_friends, q , AddFriendsActivity.this.user.getEncodedEmail());
                    if(adapter.getCount() == 0){
                        findViewById(R.id.nomatch).setVisibility(View.VISIBLE);
                    }
                    else{
                        findViewById(R.id.nomatch).setVisibility(View.GONE);
                    }
                    adapter.registerDataSetObserver(new DataSetObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                            //progressDialog.hide();
                        }
                    });
                    addedUsers.setAdapter(adapter);
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
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                }
                return true;
            }
        });
        addedUsers = (ListView) findViewById(R.id.added_users);
        progressDialog.setMessage("Refreshing..");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adapter!=null)
            adapter.cleanup();
    }
}
