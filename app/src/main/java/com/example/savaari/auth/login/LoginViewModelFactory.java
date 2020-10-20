package com.example.savaari.auth.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(com.example.savaari.auth.login.LoginViewModel.class)) {
            return (T) new com.example.savaari.auth.login.LoginViewModel();
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}