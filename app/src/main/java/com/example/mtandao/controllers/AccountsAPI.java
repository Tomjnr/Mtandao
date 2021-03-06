package com.example.mtandao.controllers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.mtandao.services.AccountListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AccountsAPI {
    private Context context;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private AccountListener.RegistrationListener registrationListener;
    private AccountListener.LoginListener loginListener;

    public AccountsAPI(Context context) {
         this.context = context;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

    }
    public void registerUser(final String email,final String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();

                            HashMap<Object, String > map = new HashMap<>();
                            map.put("uid", uid);
                            map.put("email", email);

                            reference.child(uid).setValue(map);
                            registrationListener.onAccountCreated();

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        registrationListener.onFailureResponse(e);

                    }
                });
    }

    public void loginUser(final String email, final String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            loginListener.onSuccessLogin();
                        }
                        else {
                            loginListener.onLoginFailure();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loginListener.onFailureResponse(e);

                    }
                });
    }

    public AccountListener.RegistrationListener getRegistrationListener() {
        return registrationListener;
    }

    public void setRegistrationListener(AccountListener.RegistrationListener registrationListener) {
        this.registrationListener = registrationListener;
    }

    public AccountListener.LoginListener getLoginListener() {
        return loginListener;
    }

    public void setLoginListener(AccountListener.LoginListener loginListener) {
        this.loginListener = loginListener;
    }
}
