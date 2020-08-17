package com.example.mtandao.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mtandao.R;
import com.example.mtandao.controllers.AccountsAPI;
import com.example.mtandao.services.AccountListener;
import com.example.mtandao.services.Loader;

public class RegristrationActivity extends AppCompatActivity implements View.OnClickListener, AccountListener.RegistrationListener {
    private EditText emailEdit, passwordEdit;
    private Button registerBtn, existingAccountBtn;

    private AccountsAPI accountsAPI;
    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regristration);

        accountsAPI = new AccountsAPI(this);
        accountsAPI.setRegistrationListener(this);
        loader = new Loader(this);


        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        registerBtn = findViewById(R.id.registerBtn);
        existingAccountBtn = findViewById(R.id.existingAccountBtn);

        registerBtn.setOnClickListener(this);
        existingAccountBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.equals(existingAccountBtn)){
            goToLoginActivity();
        }

        if (view.equals(registerBtn)){
            if (validated()){
                String email = emailEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                accountsAPI.registerUser(email,password);
                loader.showDialogue();
            }
        }

    }

    private void goToLoginActivity() {
        startActivity(new Intent(new Intent(RegristrationActivity.this, LoginActivity.class)));
        finish();
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


    @Override
    public void onAccountCreated() {
        loader.hideDialogue();
        Toast.makeText(this, "Account created Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(new Intent(RegristrationActivity.this,MainActivity.class)));
        finish();

    }

    @Override
    public void onFailureResponse(Exception e) {
        loader.hideDialogue();
        Toast.makeText(this, "Account could not be created..."+e.getMessage(), Toast.LENGTH_SHORT).show();


    }
}