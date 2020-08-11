package com.example.mtandao.services;

public interface AccountListener {
    interface RegistrationListener{
        void onAccountCreated();
        void onFailureResponse(Exception e);
    }
}
