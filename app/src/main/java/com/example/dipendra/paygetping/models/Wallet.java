package com.example.dipendra.paygetping.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dipendra on 15/04/17.
 */
@IgnoreExtraProperties
public class Wallet {
    private String name;
    private String owner;
    private String ownerID;
    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    private ArrayList<Transaction> transactions;
    public Wallet(){
        transactions = new ArrayList<Transaction>();
        name = "";
        owner = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
    public Map<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("ownerID", ownerID);
        return map;
    }
}
