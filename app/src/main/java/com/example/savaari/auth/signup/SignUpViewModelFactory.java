package com.example.savaari.auth.signup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.savaari.Repository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class SignUpViewModelFactory implements ViewModelProvider.Factory {

    private Repository repository;

    public SignUpViewModelFactory(Repository repository) {
        this.repository = repository;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(com.example.savaari.auth.signup.SignUpViewModel.class)) {
            return (T) new com.example.savaari.auth.signup.SignUpViewModel(repository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}