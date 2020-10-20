package com.example.savaari.auth.signup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class SignUpViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(com.example.savaari.auth.signup.SignUpViewModel.class)) {
            return (T) new com.example.savaari.auth.signup.SignUpViewModel();
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}