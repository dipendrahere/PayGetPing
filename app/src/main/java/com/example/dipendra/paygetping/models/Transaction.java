package com.example.dipendra.paygetping.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by dipendra on 15/04/17.
 */
@IgnoreExtraProperties
public class Transaction {
    private User to;
    private User from;
    private int amount;
    private boolean done;
    private String description;

    public Transaction(){
        done = false;
    }
    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getTo() {
        return to;

    }

    public void setTo(User to) {
        this.to = to;
    }
}
