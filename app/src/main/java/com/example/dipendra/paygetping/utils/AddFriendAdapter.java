package com.example.dipendra.paygetping.utils;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dipendra.paygetping.R;
import com.example.dipendra.paygetping.models.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dipendra on 16/04/17.
 */

public class AddFriendAdapter extends FirebaseListAdapter<User> {
    private String encodedEmail;
    private Activity activity;
    public AddFriendAdapter(Activity activity, Class<User> modelClass, int modelLayout, Query ref, String encodedEmail) {
        super(activity, modelClass, modelLayout, ref);
        this.activity = activity;
        this.encodedEmail = encodedEmail;
    }

    @Override
    protected void populateView(View view, final User user, int i) {
        TextView name = (TextView) view.findViewById(R.id.added_friend_name);
        TextView email = (TextView) view.findViewById(R.id.added_friend_mail);
        email.setText(user.getEncodedEmail().replace(",","."));
        name.setText(user.getName());
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.row_item_user_add_friend);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getEncodedEmail().equals(encodedEmail)){
                    Toast.makeText(activity, "You cannot add yourself as a friend", Toast.LENGTH_SHORT).show();
                }
                else{
                    final DatabaseReference ref = Constants.getDatabase().getReference().child("userFriends").child(encodedEmail).child(user.getEncodedEmail());
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue(User.class)!=null){
                                Toast.makeText(activity, "Already Added!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                User u = new User();
                                u.setName(user.getName());
                                u.setEncodedEmail(user.getEncodedEmail());
                                ref.setValue(u);
                                activity.finish();
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
}
