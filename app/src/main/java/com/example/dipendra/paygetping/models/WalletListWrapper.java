package com.example.dipendra.paygetping.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

/**
 * Created by dipendra on 15/04/17.
 */
@IgnoreExtraProperties
public class WalletListWrapper {
    private String pushId;
    private String name;
    private String ownerID;
    private String owner;

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public WalletListWrapper(){

    }
    public WalletListWrapper(Wallet w){
        this.name = w.getName();
        this.owner = w.getOwner();
        this.ownerID = w.getOwnerID();
    }
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    public HashMap<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("pushId",pushId);
        map.put("name", name);
        map.put("owner", owner);
        map.put("ownerID", ownerID);
        return map;
    }
    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
