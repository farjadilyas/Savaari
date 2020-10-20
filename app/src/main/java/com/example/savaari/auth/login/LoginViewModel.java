package com.example.savaari.auth.login;

import android.util.Log;
import com.example.savaari.auth.AuthInputValidator;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.savaari.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<RecoveryFormState> recoveryFormState = new MutableLiveData<>();

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<RecoveryFormState> getRecoveryFormState() {
        return recoveryFormState;
    }


    public void recoveryEmailDataChanged(String username) {
        if (!AuthInputValidator.isUserNameValid(username))
            recoveryFormState.setValue(new RecoveryFormState(R.string.invalid_username));
        else
            recoveryFormState.setValue(new RecoveryFormState(true));
    }

    public void loginDataChanged(String username, String password) {

        boolean isValid = true, userNameValid = AuthInputValidator.isUserNameValid(username);
        int passwordValidityType = AuthInputValidator.isPasswordValid(password);

        if (!userNameValid) {
            isValid = false;
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        }
        if (passwordValidityType != 0) {
            isValid = false;

            switch (passwordValidityType)
            {
                case (1):
                    loginFormState.setValue(new LoginFormState(null, R.string.invalid_password1));
                    break;
                case (2):
                    loginFormState.setValue(new LoginFormState(null, R.string.invalid_password2));
                    break;
                case (3):
                    loginFormState.setValue(new LoginFormState(null, R.string.invalid_password3));
                    break;
                case (4):
                    loginFormState.setValue(new LoginFormState(null, R.string.invalid_password4));
                    break;
                case (5):
                    loginFormState.setValue(new LoginFormState(null, R.string.invalid_password5));
                    break;
                case (6):
                    loginFormState.setValue(new LoginFormState(null, R.string.invalid_password6));
                    break;
                case (7):
                    loginFormState.setValue(new LoginFormState(null, R.string.invalid_password7));
                    break;
            }
        }

       if (isValid) {

           loginFormState.setValue(new LoginFormState(true));
       }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        Log.d("this happened!", "the fuck");
    }
}