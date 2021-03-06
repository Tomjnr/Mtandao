package com.example.mtandao.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mtandao.R;
import com.example.mtandao.controllers.AccountsAPI;
import com.example.mtandao.services.AccountListener;
import com.example.mtandao.services.Loader;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener, AccountListener.LoginListener {
    private EditText emailEdit,passwordEdit;
    private Button loginBtn, createAccountBtn;
    private TextView txtForgotPassword;

    private AccountsAPI accountsAPI;
    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        loginBtn = findViewById(R.id.loginBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        loginBtn.setOnClickListener(this);
        createAccountBtn.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);

        accountsAPI = new AccountsAPI(this);
        loader = new Loader(this);
        accountsAPI.setLoginListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(createAccountBtn)){
            goToRegistrationActivity();
        }
        if (view.equals(loginBtn)){
            if (validated()){
                String email = emailEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                accountsAPI.loginUser(email,password);
                loader.showDialogue();
            }
        }

    }

    private boolean validated() {
        if (TextUtils.isEmpty(emailEdit.getText().toString())){
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailEdit.getText().toString()).matches()){
            Toast.makeText(this, "Invalid email pattern", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(passwordEdit.getText().toString())){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }else return true;

    }

    private void goToRegistrationActivity() {
        startActivity(new Intent(LoginActivity.this,RegristrationActivity.class));
        finish();
    }

    @Override
    public void onSuccessLogin() {
        loader.hideDialogue();
        Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();

    }

    @Override
    public void onLoginFailure() {
        loader.hideDialogue();
        Toast.makeText(this, "Oops! some error occurred,try again.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFailureResponse(Exception e) {
        loader.hideDialogue();
        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();

    }
}