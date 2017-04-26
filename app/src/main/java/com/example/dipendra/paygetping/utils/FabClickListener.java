package com.example.dipendra.paygetping.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dipendra.paygetping.MainActivity;
import com.example.dipendra.paygetping.R;
import com.example.dipendra.paygetping.models.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by dipendra on 27/04/17.
 */

public class FabClickListener implements View.OnClickListener{
    private Activity activity;
    private ArrayList<User> added;
    public FabClickListener(){

    }
    public FabClickListener(MainActivity mainActivity) {
        this.activity = mainActivity;
        added = new ArrayList<User>();
    }

    @Override
    public void onClick(View view) {

        final String amount;
        final String description;
        if(view.getId() == R.id.fabs){
            View v = LinearLayout.inflate(activity, R.layout.pay_alert_dialog, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setView(v);
            final EditText amountEd = (EditText) v.findViewById(R.id.dialog_amount);
            final EditText descriptionEd = (EditText) v.findViewById(R.id.dialog_description);
            descriptionEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_NULL
                            && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        if(amountEd.getText().toString().length() <= 0){
                            Toast.makeText(activity, "No Money! Are you joking?", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            onPositiveClick(Integer.parseInt(amountEd.getText().toString()), descriptionEd.getText().toString());
                        }
                    }
                    return true;
                }
            });
            builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(amountEd.getText().toString().length() <= 0){
                        Toast.makeText(activity, "No Money! Are you joking?", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        onPositiveClick(Integer.parseInt(amountEd.getText().toString()), descriptionEd.getText().toString());
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void onPositiveClick(final int amount, String description) {
        View v = LinearLayout.inflate(activity, R.layout.share_transaction_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(v);
        builder.setTitle("Share With");
        ListView friends = (ListView) v.findViewById(R.id.share_with_friends_list);
        DatabaseReference ref = Constants.getDatabase().getReference().child("wallets").child(((MainActivity)activity).getCurrentList().getPushId()).child("sharedWith");
        final FirebaseListAdapter<User> adapter = new FirebaseListAdapter<User>(activity, User.class, R.layout.row_frag_pay_dialog, ref) {
            @Override
            protected void populateView(View v, final User model, final int position) {
                if(added.size() == position){
                    added.add(null);
                }
                added.set(position, model);
                RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.click_row_pay_dialog);
                ((TextView)v.findViewById(R.id.checked_row_pay_name)).setText(model.getName());
                final CheckedTextView check = ((CheckedTextView)v.findViewById(R.id.checked_row_pay_id));
                check.setText(model.getEncodedEmail().replace(",","."));
                check.setChecked(true);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(check.isChecked()){
                            added.set(position, null);
                            check.setChecked(false);
                        }
                        else{
                            added.set(position, model);
                            check.setChecked(true);
                        }
                    }
                });

            }
        };
        friends.setAdapter(adapter);
        friends.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        builder.setPositiveButton("hello", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int it = 0; it < added.size();it++){

                }
            }
        });
        builder.create().show();
    }
}
