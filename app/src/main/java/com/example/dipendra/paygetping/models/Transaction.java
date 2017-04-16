package com.example.dipendra.paygetping.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by dipendra on 15/04/17.
 */
@IgnoreExtraProperties
public class Transaction {
    private String desciption;
    private String from;
    private String to;
    private String amount;

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
