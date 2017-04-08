package com.example.dipendra.paygetping;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dipendra.paygetping.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dipendra on 07/04/17.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText username;
    private EditText usermail;
    private EditText password;
    private Button register;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initialize();
        register.setOnClickListener(this);
    }
    private void initialize(){
        username = (EditText) findViewById(R.id.usernameregister);
        usermail = (EditText) findViewById(R.id.useridregister);
        password = (EditText) findViewById(R.id.passwordregister);
        register = (Button) findViewById(R.id.registerButtonregister);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.registerButtonregister){
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Wait! Your Registration is in progress..");
            progressDialog.show();
            auth.createUserWithEmailAndPassword(usermail.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        User user = new User();
                        user.setName(username.getText().toString());
                        user.setEncodedEmail(usermail.getText().toString().replace(".",","));
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getEncodedEmail());
                        reference.setValue(user);
                        progressDialog.hide();
                        signin(user.getEncodedEmail().replace(",","."), password.getText().toString());
                    }
                }
            });
        }
    }
}
