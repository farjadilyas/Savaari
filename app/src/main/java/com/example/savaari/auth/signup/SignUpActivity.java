package com.example.savaari.auth.signup;

import android.app.Activity;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savaari.LoadDataTask;
import com.example.savaari.OnAuthenticationListener;
import com.example.savaari.R;

import com.example.savaari.Util;


public class SignUpActivity extends Util {

    private void signupAction(final ConstraintLayout successBanner, final ProgressBar loadingProgressBar, final String username, final String password, final String nickname)
    {
        loadingProgressBar.setVisibility(View.VISIBLE);

        new LoadDataTask(new OnAuthenticationListener() {
            @Override
            public void authenticationStatus(int USER_ID) {
                loadingProgressBar.setVisibility(View.GONE);
                if (USER_ID > 0) {
                    Toast.makeText(getApplicationContext(), R.string.sign_up_success, Toast.LENGTH_LONG).show();
                    NavUtils.navigateUpFromSameTask(SignUpActivity.this);
                }
            }
        }, null).execute("signup", nickname, username, password);
    }

    private com.example.savaari.auth.signup.SignUpViewModel signUpViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        themeSelect(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        signUpViewModel = ViewModelProviders.of(this, new SignUpViewModelFactory())
                .get(SignUpViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText nicknameEditText = findViewById(R.id.nickname);
        final Button signUpButton = findViewById(R.id.signUp);
        final Button backToLogin = findViewById(R.id.backToLogin);
        final Button backFromBanner = findViewById(R.id.backFromBanner);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final ConstraintLayout successBanner = findViewById(R.id.successBanner);
        successBanner.setVisibility(View.INVISIBLE);

        backFromBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                NavUtils.navigateUpFromSameTask(SignUpActivity.this);
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(SignUpActivity.this);
            }
        });

        signUpViewModel.getSignUpFormState().observe(this, new Observer<SignUpFormState>() {
            @Override
            public void onChanged(@Nullable SignUpFormState signupFormState) {
                if (signupFormState == null) {
                    return;
                }
                signUpButton.setEnabled(signupFormState.isDataValid());
                if (signupFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(signupFormState.getUsernameError()));
                }
                if (signupFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(signupFormState.getPasswordError()));
                }
                if (signupFormState.getNicknameError() != null) {
                    nicknameEditText.setError(getString(signupFormState.getNicknameError()));
                }
            }
        });

        /*

        signUpViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });*/

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                signUpViewModel.signUpDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), nicknameEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        nicknameEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //replace with sign in code
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    loadingProgressBar.setVisibility(View.VISIBLE);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

                    View view = getCurrentFocus();
                    if (view == null)
                        view = new View(getApplicationContext());

                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    signupAction(successBanner, loadingProgressBar, usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), nicknameEditText.getText().toString());
                }
                return false;
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

                View view = getCurrentFocus();
                if (view == null)
                    view = new View(getApplicationContext());

                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                signupAction(successBanner, loadingProgressBar, usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), nicknameEditText.getText().toString());
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(SignUpActivity.this);
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}