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
    private String description;

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
