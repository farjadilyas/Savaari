package com.example.savaari.login.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

public class LoginDataSource {



    //private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void login(final Context context, final String username, final String password) {

        /*
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(context, "Sign in successful", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            String errorMessage;

                            try {
                                throw task.getException();
                            }
                            catch (FirebaseAuthInvalidUserException e) {errorMessage = "Invalid Email ID";}
                            catch (FirebaseAuthInvalidCredentialsException e) {errorMessage = "Invalid Password";}
                            catch (FirebaseNetworkException e) {errorMessage = "Network Error";}
                            catch (Exception e) {errorMessage = "Sign in failed (Error Unknown)";}

                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                        }

                    }
                });*/
    }



    public void signup(final Context context, final String username, final String password, final String nickname) {

        /*
        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    Toast.makeText(context, "Welcome " + nickname + "!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    String errorMessage;

                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException e) {errorMessage = "Invalid Email ID";}
                    catch (FirebaseAuthInvalidCredentialsException e) {errorMessage = "Invalid Password";}
                    catch (FirebaseNetworkException e) {errorMessage = "Network Error";}
                    catch (Exception e) {errorMessage = "Sign in failed (Error Unknown)";}

                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                }


            }
        });*/
    }

    public void logout() {
        // TODO: revoke authentication

    }
}