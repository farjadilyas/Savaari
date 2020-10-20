package com.example.savaari.auth.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.savaari.LoadAuthDataTask;
import com.example.savaari.OnAuthenticationListener;
import com.example.savaari.R;
import com.example.savaari.Util;
import com.example.savaari.auth.signup.SignUpActivity;
import com.example.savaari.ride.RideActivity;

public class LoginActivity extends Util {

    private LoginViewModel loginViewModel;      // input validation
    private EditText usernameEditText, passwordEditText, recoveryEmailEditText;
    private Button loginButton, newAccountButton, backFromBanner, forgotPasswordButton;
    private ImageButton closeBanner;
    private ConstraintLayout recoveryEmailBanner, forgotPasswordBanner, emailSentBanner;
    private ProgressBar loadingProgressBar, recoveryProgressBar;
    boolean isEmailSent = false;                // forgot password transition management

    public LoginActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        themeSelect(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        init();
        forgotPasswordBannerHandler();;
        loginFormStateWatcher();
        recoveryFormStateWatcher();
        loginRequestHandler();

        // Launches SignupActivity

        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    private void init() {
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        recoveryEmailEditText = findViewById(R.id.recoveryEmail);
        loginButton = findViewById(R.id.login);
        newAccountButton = findViewById(R.id.newAccountBTN);

        closeBanner = findViewById(R.id.closeBanner);

        backFromBanner = findViewById(R.id.backFromBanner);
        forgotPasswordBanner = findViewById(R.id.forgotPasswordBanner);
        emailSentBanner = findViewById(R.id.emailSentPanel);

        forgotPasswordButton = findViewById(R.id.forgotPassword);
        loadingProgressBar = findViewById(R.id.loading);
        recoveryEmailBanner = findViewById(R.id.recoveryEmailBanner);
        forgotPasswordBanner = findViewById(R.id.forgotPasswordBanner);
        recoveryProgressBar = findViewById(R.id.recoveryProgressBar);


        // Visibility settings for forgot password banner

        forgotPasswordBanner.setVisibility(View.INVISIBLE);
        emailSentBanner.setVisibility(View.INVISIBLE);
        backFromBanner.setText(R.string.pass_reset_btn_text);
    }

    private void forgotPasswordBannerHandler() {
        // Displays forgot password banner

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                emailSentBanner.setVisibility(View.INVISIBLE);
                recoveryEmailBanner.setVisibility(View.VISIBLE);
                backFromBanner.setText(R.string.pass_reset_btn_text);

                forgotPasswordBanner.startAnimation(inFromBottomAnimation(250));
                forgotPasswordBanner.setVisibility(View.VISIBLE);
            }
        });



        // Handles forgot-pass banner interactions

        closeBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                forgotPasswordBanner.startAnimation(outToBottomAnimation());
                forgotPasswordBanner.setVisibility(View.INVISIBLE);
                emailSentBanner.startAnimation(outToRightAnimation(500));
                isEmailSent = false;
            }
        });


        // [ sends recovery email on first button press ] + [ retracts banner on second press]

        backFromBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                if (isEmailSent)
                {
                    forgotPasswordBanner.startAnimation(outToBottomAnimation());
                    forgotPasswordBanner.setVisibility(View.INVISIBLE);
                    emailSentBanner.startAnimation(outToRightAnimation(500));
                    isEmailSent = false;
                }
                else
                {
                    recoveryProgressBar.setVisibility(View.VISIBLE);

                    //TODO: Handle Password Reset Action
                }
            }
        });
    }


    private void loginFormStateWatcher() {
        // Receives and displays input validation messages - for login page

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {

            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        // Listener for login page input fields

        TextWatcher afterLoginTextChangedListener = new TextWatcher() {
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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterLoginTextChangedListener);
        passwordEditText.addTextChangedListener(afterLoginTextChangedListener);
    }


    private void recoveryFormStateWatcher() {
        // Receives and displays input validation messages - for password recovery banner

        loginViewModel.getRecoveryFormState().observe(this, new Observer<RecoveryFormState>() {

            @Override
            public void onChanged(@Nullable RecoveryFormState recoveryFormState) {

                if (recoveryFormState == null)
                    return;

                backFromBanner.setEnabled(recoveryFormState.isDataValid());

                if (recoveryFormState.getRecoveryEmailError() != null) {
                    recoveryEmailEditText.setError(getString(recoveryFormState.getRecoveryEmailError()));
                }
            }
        });

        // Listener for recovery page input fields

        TextWatcher afterRecoveryTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.recoveryEmailDataChanged(recoveryEmailEditText.getText().toString());
            }
        };

        recoveryEmailEditText.addTextChangedListener(afterRecoveryTextChangedListener);
    }


    private void loginRequestHandler() {
        // Sends login requests to loginAction()

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    loginAction(loadingProgressBar, usernameEditText.getText().toString(), passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

                View view = getCurrentFocus();
                if (view == null)
                    view = new View(getApplicationContext());

                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                loginAction(loadingProgressBar, usernameEditText.getText().toString(), passwordEditText.getText().toString());

            }
        });
    }

    //Method: Handles Login Request

    private void loginAction(final ProgressBar loadingProgressBar, final String username, final String password) {

        loadingProgressBar.setVisibility(View.VISIBLE);

        new LoadAuthDataTask(new OnAuthenticationListener() {
            @Override
            public void authenticationStatus(int USER_ID) {
                loadingProgressBar.setVisibility(View.GONE);

                SharedPreferences sharedPreferences
                        = getSharedPreferences("AuthSharedPref", MODE_PRIVATE);

                SharedPreferences.Editor myEdit
                        = sharedPreferences.edit();

                if (USER_ID == -1) {
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                    myEdit.putInt("USER_ID", -1);
                    myEdit.commit();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_LONG).show();

                    myEdit.putInt("USER_ID", USER_ID);

                    myEdit.commit();

                    Intent i = new Intent(LoginActivity.this, RideActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, null).execute("login", username, password);
    }

    @Override
    public void onBackPressed() {
        if (isEmailSent) {
            forgotPasswordBanner.startAnimation(outToBottomAnimation());
            forgotPasswordBanner.setVisibility(View.INVISIBLE);
            emailSentBanner.startAnimation(outToRightAnimation(500));
            isEmailSent = false;
        }
        else {
            super.onBackPressed();
        }
    }
}