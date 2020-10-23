package com.example.savaari.ride;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RideViewModelFactory implements ViewModelProvider.Factory {

    private int USER_ID = -1;

    public RideViewModelFactory(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RideViewModel.class)) {
            return (T) new RideViewModel(USER_ID);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}