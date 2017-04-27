package com.example.dipendra.paygetping.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dipendra.paygetping.MainActivity;
import com.example.dipendra.paygetping.R;
import com.example.dipendra.paygetping.models.Transaction;
import com.example.dipendra.paygetping.models.User;
import com.example.dipendra.paygetping.utils.Constants;
import com.example.dipendra.paygetping.utils.PayFragmentListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;


public class PayFragment extends Fragment{
    private ListView pays;
    private ChildEventListener listener;
    private Query q;
    private PayFragmentListAdapter adapter;
    private ArrayList<Transaction> transactions;
    private ArrayList<User> dialogFriendList;
    public PayFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listener!=null)
            q.removeEventListener(listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.fragment_pay, container, false);
        initialize(v);
        adapter = new PayFragmentListAdapter(getActivity(), R.layout.row_frag_pay, transactions);
        try {
            q = Constants.getDatabase().getReference().child("wallets").child(((MainActivity) getActivity()).getCurrentList().getPushId()).child("transactions");
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Transaction t = dataSnapshot.getValue(Transaction.class);
                    if (t.getTo().getEncodedEmail().equalsIgnoreCase(((MainActivity) getActivity()).getUser().getEncodedEmail())) {
                        adapter.add(t);
                        adapter.notifyDataSetChanged();
                    }
                    if(adapter.getCount() <= 0){
                        (v.findViewById(R.id.emptynotifier)).setVisibility(View.VISIBLE);
                    }
                    else{
                        (v.findViewById(R.id.emptynotifier)).setVisibility(View.GONE);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            q.addChildEventListener(listener);
            pays.setAdapter(adapter);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return v;
    }

    private void initialize(View v){
        pays = (ListView) v.findViewById(R.id.listview_pay_frag);
        pays.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        dialogFriendList = new ArrayList<User>();
        transactions = new ArrayList<Transaction>();
    }
}
