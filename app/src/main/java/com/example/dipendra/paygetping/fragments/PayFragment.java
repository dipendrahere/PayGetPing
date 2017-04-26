package com.example.dipendra.paygetping.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dipendra.paygetping.R;
import com.example.dipendra.paygetping.models.User;

import java.util.ArrayList;


public class PayFragment extends Fragment{
    private ListView pays;
    private ArrayList<User> dialogFriendList;
    public PayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_pay, container, false);
        initialize(v);
        return v;
    }

    private void initialize(View v){
        pays = (ListView) v.findViewById(R.id.listview_pay_frag);
        pays.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        dialogFriendList = new ArrayList<User>();
    }

}
