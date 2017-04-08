package com.example.dipendra.paygetping.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by dipendra on 06/04/17.
 */
@IgnoreExtraProperties
public class User {
    private String name;
    private String encodedEmail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEncodedEmail() {
        return encodedEmail;
    }

    public void setEncodedEmail(String encodedEmail) {
        this.encodedEmail = encodedEmail;
    }
}
