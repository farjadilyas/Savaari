package com.farjad.savaari.auth.signup;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.farjad.savaari.R;
import com.farjad.savaari.services.persistence.Repository;
import com.farjad.savaari.auth.AuthInputValidator;

public class SignUpViewModel extends ViewModel {

    private final static String LOG_TAG = SignUpViewModel.class.getSimpleName();
    private final Repository repository;
    private final MutableLiveData<SignUpFormState> signupFormState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> signUpComplete = new MutableLiveData<>(false);

    public SignUpViewModel(Repository repository) {
        this.repository = repository;
    }

    LiveData<SignUpFormState> getSignUpFormState() {
        return signupFormState;
    }
    LiveData<Boolean> isSignUpComplete() { return signUpComplete; }

    public void signupAction(String nickname, String username, String password) {
        repository.signup(object -> {
            boolean result;
            try {
                result = (boolean) object;
                signUpComplete.postValue(result);
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "signupAction(): exception");
            }
        }, nickname, username, password);
    }

    public void signUpDataChanged(String username, String password, String nickname)
    {
        boolean isValid = true, usernameValid = AuthInputValidator.isUserNameValid(username), nicknameValid = AuthInputValidator.isNicknameValid(nickname);
        int passwordValidityType = AuthInputValidator.isPasswordValid(password);

        if (!usernameValid) {
            isValid = false;
            signupFormState.setValue(new SignUpFormState(R.string.invalid_username, null, null));
        }
        if (passwordValidityType != 0) {
            isValid = false;

            switch (passwordValidityType) {
                case (1):
                    signupFormState.setValue(new SignUpFormState(null, R.string.invalid_password1, null));
                    break;
                case (2):
                    signupFormState.setValue(new SignUpFormState(null, R.string.invalid_password2, null));
                    break;
                case (3):
                    signupFormState.setValue(new SignUpFormState(null, R.string.invalid_password3, null));
                    break;
                case (4):
                    signupFormState.setValue(new SignUpFormState(null, R.string.invalid_password4, null));
                    break;
                case (5):
                    signupFormState.setValue(new SignUpFormState(null, R.string.invalid_password5, null));
                    break;
                case (6):
                    signupFormState.setValue(new SignUpFormState(null, R.string.invalid_password6, null));
                    break;
                case (7):
                    signupFormState.setValue(new SignUpFormState(null, R.string.invalid_password7, null));
                    break;
            }
        }

        if (!nicknameValid) {

            isValid = false;

            signupFormState.setValue(new SignUpFormState(signupFormState.getValue().getUsernameError(),
                    signupFormState.getValue().getPasswordError(), R.string.invalid_nickname));
        }

        if (isValid) {

            signupFormState.setValue(new SignUpFormState(true));
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}