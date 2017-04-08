package com.example.dipendra.paygetping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by dipendra on 06/04/17.
 */

public class LoginActivity extends BaseActivity implements Button.OnClickListener{
    private Button loginButton;
    private Button registerButton;
    private EditText userid;
    private EditText password;
    private String usermail;
    private String passwordString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initialize();
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }


    private void initialize(){
        this.loginButton = (Button) findViewById(R.id.loginButton);
        this.registerButton = (Button) findViewById(R.id.registerButton);
        this.userid = (EditText) findViewById(R.id.userid);
        this.password = (EditText) findViewById(R.id.password);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.loginButton){
            login();
        }
        else if(view.getId() == R.id.registerButton){
            register();
        }
    }
    private void login(){
        usermail = userid.getText().toString();
        passwordString = password.getText().toString();
        signin(usermail, passwordString);
    }
    private void register(){
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
