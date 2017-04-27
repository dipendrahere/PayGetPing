package com.example.dipendra.paygetping.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dipendra.paygetping.R;
import com.example.dipendra.paygetping.models.Transaction;

import java.util.ArrayList;

/**
 * Created by dipendra on 27/04/17.
 */

public class PayFragmentListAdapter extends ArrayAdapter<Transaction>{

    private Context context;
    private int resource;
    private ArrayList<Transaction> objects;
    public PayFragmentListAdapter(Context context, int resource, ArrayList<Transaction> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(resource, null);
        }
        TextView amount = (TextView)v.findViewById(R.id.pay_amount);
        TextView name = (TextView)v.findViewById(R.id.name_payer);
        TextView id = (TextView) v.findViewById(R.id.id_payer);
        name.setText(objects.get(position).getFrom().getName());
        id.setText(objects.get(position).getFrom().getEncodedEmail().replace(",","."));
        amount.setText(""+context.getResources().getString(R.string.Rs)+objects.get(position).getAmount());
        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
