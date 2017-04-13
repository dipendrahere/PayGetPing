package com.example.dipendra.paygetping;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dipendra.paygetping.Utils.MyEditText;
import com.example.dipendra.paygetping.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dipendra on 07/04/17.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private MyEditText username;
    private MyEditText usermail;
    private MyEditText password;
    private Button register;
    private TextView txtname;
    private TextView txtid;
    private TextView txtpassword;
    private String email;
    private String pass;
    private String name;
    private TextWatcher twid;
    private TextWatcher twname;
    private TextWatcher twpassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
       // errorIcon = getResources().getDrawable(R.drawable.ic_error);
        //errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
        initialize();

        twid = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                email = s.toString();
                if (isValidEmail(email)) {
                    txtid.setText("Email in correct format");
                }
                else {
                    txtid.setText("Email not in correct format");
                }
            }
        };
        usermail.addTextChangedListener(twid);
        twpassword = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pass = s.toString();
                if (isValidPassword(pass)) {
                    txtpassword.setText("Password in correct format");
                }
                else {
                    txtpassword.setText("Password must contain atleast six characters");
                }
            }
        };
        password.addTextChangedListener(twpassword);

        twname = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString();
                if (isValidName(name)) {
                    txtname.setText("Username in correct format");
                }
                else {
                    txtname.setText("Username must not be null");
                }
            }
        };
        username.addTextChangedListener(twname);
        register.setOnClickListener(this);
    }
    private void initialize(){
        username = (MyEditText) findViewById(R.id.usernameregister);
        usermail = (MyEditText) findViewById(R.id.useridregister);
        password = (MyEditText) findViewById(R.id.passwordregister);
        txtid = (TextView) findViewById(R.id.textViewid);
        txtname = (TextView) findViewById(R.id.textViewname);
        txtpassword = (TextView) findViewById(R.id.textViewpassword);
        register = (Button) findViewById(R.id.registerButtonregister);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.registerButtonregister){
            email = usermail.getText().toString();
            name = username.getText().toString();
            pass = password.getText().toString();
            if (isValidEmail(email) && (isValidPassword(pass) && isValidName(name))) {
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
        }}
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }

    private boolean isValidName (String name) {
        if (name != null) {
            return true;
        }
        return false;
    }
}
