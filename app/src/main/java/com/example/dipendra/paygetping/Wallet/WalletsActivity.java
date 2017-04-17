package com.example.dipendra.paygetping.wallet;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dipendra.paygetping.BaseActivity;
import com.example.dipendra.paygetping.MainActivity;
import com.example.dipendra.paygetping.R;
import com.example.dipendra.paygetping.login.LoginActivity;
import com.example.dipendra.paygetping.models.Wallet;
import com.example.dipendra.paygetping.models.WalletListWrapper;
import com.example.dipendra.paygetping.utils.Constants;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WalletsActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView listview;
    private FirebaseListAdapter adapter;
    private ArrayList<WalletListWrapper> wrapperList;
    private WalletListWrapper wrapper;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallets);
        initialize();
        //Toast.makeText(this, "Please Choose a wallet to proceed further!", Toast.LENGTH_SHORT).show();
        if(user.getEncodedEmail()==null){
            Intent i = new Intent(WalletsActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
         else {
            this.reference = Constants.getDatabase().getReference().child("users").child(user.getEncodedEmail()).child("lists");
            progressDialog.setMessage("Refreshing..");
            progressDialog.show();
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() == null){
                        Toast.makeText(WalletsActivity.this, "Please add a wallet!", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(WalletsActivity.this, "Network Failed us!", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }
            });
            adapter = new FirebaseListAdapter<WalletListWrapper>(this, WalletListWrapper.class, R.layout.wallet_list, reference) {
                @Override
                protected void populateView(View view, WalletListWrapper walletListWrapper, int i) {
                    WalletsActivity.this.findViewById(R.id.emptynotifier).setVisibility(View.GONE);
                    wrapperList.add(walletListWrapper);
                    ((TextView) view.findViewById(R.id.wallet_list_name)).setText(walletListWrapper.getName());
                    ((TextView) view.findViewById(R.id.wallet_list_owner)).setText("Owner: " + walletListWrapper.getOwner());
                }
            };
            adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    progressDialog.hide();
                }

                @Override
                public void onInvalidated() {
                    super.onInvalidated();
                    progressDialog.hide();
                    Toast.makeText(WalletsActivity.this, "Unsuccessful!", Toast.LENGTH_SHORT).show();
                }
            });
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void initialize() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fab = (FloatingActionButton) findViewById(R.id.fabwallet);
        fab.setOnClickListener(this);
        listview = (ListView) findViewById(R.id.wallet_list);
        wrapperList = new ArrayList<WalletListWrapper>();
        readSharedPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adapter!=null){
            adapter.cleanup();
        }

    }
    private void updateAndIntent(WalletListWrapper w){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("currentWallet", w.getPushId());
        editor.putString("currentWalletName", w.getName());
        editor.putString("currentWalletOwnerID", w.getOwnerID());
        editor.putString("currentWalletOwner", w.getOwner());
        editor.commit();
        currentList = w;

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle options =
                ActivityOptions.makeCustomAnimation(this, R.xml.left, R.xml.right).toBundle();
        startActivity(intent, options);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        updateAndIntent(wrapperList.get(i));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fabwallet){
            View v = LinearLayout.inflate(this, R.layout.new_wallet_dialog, null);
            final EditText ed = (EditText) v.findViewById(R.id.new_wallet_name);
            ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        onPositiveClick(ed);
                    }
                    return true;
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(v);
            builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onPositiveClick(ed);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private void onPositiveClick(EditText ed){
        if(ed.getText().toString().length()<=0){
            Toast.makeText(WalletsActivity.this, "Empty Name Not Allowed!", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog = new ProgressDialog(WalletsActivity.this);
            progressDialog.setMessage("Creating.. Wait!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            DatabaseReference ref = Constants.getDatabase().getReference();
            final String key = ref.child("wallets").push().getKey();
            Wallet w = new Wallet();
            w.setOwnerID(user.getEncodedEmail());
            w.setOwner(user.getName());
            w.setName(ed.getText().toString());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("/wallets/" + key, w.toMap());
            wrapper = new WalletListWrapper(w);
            wrapper.setPushId(key);
            map.put("/users/" + user.getEncodedEmail() + "/lists/" + key, wrapper.toMap());
            ref.updateChildren(map).addOnCompleteListener(WalletsActivity.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Constants.getDatabase().getReference().child("wallets").child(key).child("sharedWith").child(user.getEncodedEmail()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.hide();
                                    Toast.makeText(WalletsActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                                    updateAndIntent(wrapper);
                                }
                                else{

                                    Toast.makeText(WalletsActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        progressDialog.hide();
                        Toast.makeText(WalletsActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }
}
