package com.example.savaari.auth.login;

import androidx.annotation.Nullable;

public class RecoveryFormState {
    @Nullable
    private Integer recoveryEmailError;

    private boolean isDataValid;

    RecoveryFormState(@Nullable Integer recoveryEmailError) {
        this.recoveryEmailError = recoveryEmailError;
        this.isDataValid = false;
    }

    RecoveryFormState(boolean isDataValid) {
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getRecoveryEmailError() {
        return recoveryEmailError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
