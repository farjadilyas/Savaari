package com.example.savaari.login;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.savaari.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<SignupFormState> signupFormState = new MutableLiveData<>();
    private MutableLiveData<RecoveryFormState> recoveryFormState = new MutableLiveData<>();

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<SignupFormState> getSignupFormState() {
        return signupFormState;
    }

    LiveData<RecoveryFormState> getRecoveryFormState() {
        return recoveryFormState;
    }

    /*

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(final Context context, String username, String password) {
        // can be launched in a separate asynchronous job

        loginRepository.login(context, username, password);
    }


    public void  signup(final Context context, String username, String password, String nickname) {

        loginRepository.signup(context, username, password, nickname);
    }*/

    public void recoveryEmailDataChanged(String username) {
        if (!isUserNameValid(username))
            recoveryFormState.setValue(new RecoveryFormState(R.string.invalid_username));
        else
            recoveryFormState.setValue(new RecoveryFormState(true));
    }

    public void loginDataChanged(String username, String password) {

        boolean isValid = true, userNameValid = isUserNameValid(username);
        int passwordValidityType = isPasswordValid(password);

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

    public void signupDataChanged(String username, String password, String nickname)
    {
        boolean isValid = true, usernameValid = isUserNameValid(username), nicknameValid = isNicknameValid(nickname);
        int passwordValidityType = isPasswordValid(password);

        if (!usernameValid) {
            isValid = false;
            signupFormState.setValue(new SignupFormState(R.string.invalid_username, null, null));
        }
        if (passwordValidityType != 0) {
            isValid = false;

            switch (passwordValidityType) {
                case (1):
                    signupFormState.setValue(new SignupFormState(null, R.string.invalid_password1, null));
                    break;
                case (2):
                    signupFormState.setValue(new SignupFormState(null, R.string.invalid_password2, null));
                    break;
                case (3):
                    signupFormState.setValue(new SignupFormState(null, R.string.invalid_password3, null));
                    break;
                case (4):
                    signupFormState.setValue(new SignupFormState(null, R.string.invalid_password4, null));
                    break;
                case (5):
                    signupFormState.setValue(new SignupFormState(null, R.string.invalid_password5, null));
                    break;
                case (6):
                    signupFormState.setValue(new SignupFormState(null, R.string.invalid_password6, null));
                    break;
                case (7):
                    signupFormState.setValue(new SignupFormState(null, R.string.invalid_password7, null));
                    break;
            }
        }

        if (!nicknameValid) {

            isValid = false;

            signupFormState.setValue(new SignupFormState(signupFormState.getValue().getUsernameError(),
                    signupFormState.getValue().getPasswordError(), R.string.invalid_nickname));
        }

        if (isValid) {

            signupFormState.setValue(new SignupFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private int isPasswordValid(String password) {

        int length = password.length(), resultType = 0;

        boolean upperCasePresent = false;
        boolean numericPresent = false;
        boolean eightCharPresent = (length > 8);

        char charAtIndex;

        for (int index = 0 ; index < length ; ++index)
        {
            charAtIndex = password.charAt(index);

            if (Character.isUpperCase(charAtIndex))
                upperCasePresent = true;

            if (Character.isDigit(charAtIndex))
                numericPresent = true;
        }

        if (!eightCharPresent)
            resultType += 1;
        if (!numericPresent)
            resultType += 2;
        if (!upperCasePresent)
            resultType += 4;

        return resultType;
    }

    //A placeholder nickname validation check
    private boolean isNicknameValid(String nickname) {
        return nickname.length() > 0;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        Log.d("this happened!", "the fuck");
    }
}