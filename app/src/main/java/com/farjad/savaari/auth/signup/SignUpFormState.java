package com.farjad.savaari.auth.signup;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class SignUpFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer nicknameError;
    private boolean isDataValid, isSignupDataValid, isRecoveryDataValid;

    SignUpFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer nicknameError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.nicknameError = nicknameError;
        this.isDataValid = false;
    }

    SignUpFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.nicknameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getNicknameError() {
        return nicknameError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}