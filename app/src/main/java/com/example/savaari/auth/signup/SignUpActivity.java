package com.example.savaari.auth.signup;

import android.app.Activity;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.example.savaari.SavaariApplication;
import com.example.savaari.Util;
import com.example.savaari.services.network.NetworkServiceUtil;

import java.util.Objects;


public class SignUpActivity extends Util implements SignUpResponseListener {

    private com.example.savaari.auth.signup.SignUpViewModel signUpViewModel;
    ProgressBar loadingProgressBar;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText nicknameEditText;
    Button signUpButton;
    Button backToLogin;
    Button backFromBanner;
    ConstraintLayout successBanner;

    private void signupAction(final ConstraintLayout successBanner, final ProgressBar loadingProgressBar, final String username, final String password, final String nickname)
    {
        Log.d("SignUpActivity", "signUpAction");
        
        loadingProgressBar.setVisibility(View.VISIBLE);
        signUpViewModel.signupAction(nickname, username, password);

        signUpViewModel.isSignUpComplete().observe(this, this::signUpResponseAction);
        //NetworkServiceUtil.signup(SignUpActivity.this, nickname, username, password);
    }
    
    private void signUpResponseAction(Boolean result) {
        loadingProgressBar.setVisibility(View.GONE);

        if (result) {
            Toast.makeText(getApplicationContext(), R.string.sign_up_success, Toast.LENGTH_LONG).show();
            NavUtils.navigateUpFromSameTask(SignUpActivity.this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        themeSelect(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        registerSignUpReceiver();

        initialize();
        backToLoginHandler();
        signUpFormStateWatcher();

        signUpRequestHandler();
    }
    /* End of onCreate() method */

    private void initialize() {
        signUpViewModel = ViewModelProviders.of(this,
                new SignUpViewModelFactory(((SavaariApplication) this.getApplication()).getRepository()))
                .get(SignUpViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        nicknameEditText = findViewById(R.id.nickname);

        signUpButton = findViewById(R.id.signUp);
        backToLogin = findViewById(R.id.backToLogin);
        backFromBanner = findViewById(R.id.backFromBanner);

        successBanner = findViewById(R.id.successBanner);
        successBanner.setVisibility(View.INVISIBLE);

        loadingProgressBar = findViewById(R.id.loading);
    }

    /* Sign Up action handlers */
    private void signUpRequestHandler() {
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

    /*  Back to Login Activity actions */
    private void backToLoginHandler() {
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
    }


    /* Observe changes in Sign Up form state */
    private void signUpFormStateWatcher() {

        /* Called when Sign Up Form State changes (Input validation) */
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

    /* Receives response from NetworkService methods */
    private static class SignUpReceiver extends BroadcastReceiver {
        private SignUpResponseListener signUpResponseListener;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras().getString("TASK").equals("signup")) {
                signUpResponseListener = (SignUpResponseListener) context;
                signUpResponseListener.onResponseReceived(intent);
            }
        }
    }

    SignUpReceiver signUpReceiver;

    /* Register receiver */
    public void registerSignUpReceiver() {
        signUpReceiver = new SignUpReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("RESULT");

        registerReceiver(signUpReceiver, intentFilter);
    }

    @Override
    public void onResponseReceived(Intent intent) {
        signUpResponseAction(false);
    }
}