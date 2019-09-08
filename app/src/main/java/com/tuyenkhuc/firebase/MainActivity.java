/*
    Function: ok
    FA: ok
    Date:9/9/2019
 */

package com.tuyenkhuc.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.PluralsRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Date;
import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity {

    //Declare FA
    private FirebaseAnalytics mFirebaseAnalytics;

    private Button btnChangeEmail, btnChangePassword, btnSendResetEmail,
            btnRemoveUser, changeEmail, changePassword, sendEmail, remove, signout, login_home;
    private EditText currentEmail, newEmail, currentPassword, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    //private Timestamp timestamp  = new Timestamp(System.currentTimeMillis());
    /*long epoch = Long.parseLong(String.valueOf(timestamp));
    Date openTime = new Date(epoch*1000);*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get FA instance
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //Tracking time: open app
        Bundle bundle = new Bundle();
        bundle.putString("open_time", "open_time_value");
        mFirebaseAnalytics.logEvent("app_open_time", bundle);
        //End Tracking time: open app


        //get Firebase instance
        auth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    //user auth state is changed - user is null
                    //launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        btnChangeEmail = (Button) findViewById(R.id.change_email_button);
        btnChangePassword = (Button) findViewById(R.id.change_password_button);
        btnSendResetEmail = (Button) findViewById(R.id.sending_pass_reset_button);
        btnRemoveUser = (Button) findViewById(R.id.remove_user_button);
        signout = (Button) findViewById(R.id.sign_out);
        currentEmail = (EditText) findViewById(R.id.current_email);
        newEmail = (EditText) findViewById(R.id.new_email);
        currentPassword = (EditText) findViewById(R.id.current_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        changeEmail = (Button) findViewById(R.id.changeEmail);
        changePassword = (Button) findViewById(R.id.changePass);
        remove = (Button) findViewById(R.id.remove);
        sendEmail = (Button) findViewById(R.id.send);
        login_home = (Button) findViewById(R.id.login_home);

        currentEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        currentPassword.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);
        login_home.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (user == null) {
            login_home.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            Toast.makeText(MainActivity.this, "User not login yet, go to login page", Toast.LENGTH_LONG).show();
        }
        //btnLogin
        /*login_home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });*/

        //btnChangeEmail
        btnChangeEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                currentEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.VISIBLE);
                currentPassword.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);

                changeEmail.setVisibility(View.VISIBLE);
                changePassword.setVisibility(View.GONE);
                sendEmail.setVisibility(View.GONE);
                remove.setVisibility(View.GONE);
                //FA
                Bundle bundle = new Bundle();
                bundle.putString("btn_name", "btnChangeEmail");
                mFirebaseAnalytics.logEvent("main_btnChangeEmail_is_click", bundle);
                //end FA
            }
        });

        //changeEmail
        changeEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newEmail.getText().toString().trim().equals("")) {
                    user.updateEmail(newEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //FA
                                        Bundle bundle = new Bundle();
                                        bundle.putString("btn_name", "changeEmail");
                                        mFirebaseAnalytics.logEvent("main_changeEmail_is_click_success", bundle);
                                        //end FA
                                        Toast.makeText(MainActivity.this, "Email address is update. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                        signOut();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        //FA
                                        Bundle bundle = new Bundle();
                                        bundle.putString("btn_name", "changeEmail");
                                        mFirebaseAnalytics.logEvent("main_changeEmail_is_click_not_success", bundle);
                                        //end FA
                                        Toast.makeText(MainActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else if (newEmail.getText().toString().trim().equals("")) {
                    newEmail.setError("Enter email");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        //btnChangePassword
        btnChangePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                currentEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.GONE);
                currentPassword.setVisibility(View.GONE);
                newPassword.setVisibility(View.VISIBLE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.VISIBLE);
                sendEmail.setVisibility(View.GONE);
                remove.setVisibility(View.GONE);
                //FA
                Bundle bundle = new Bundle();
                bundle.putString("btn_name","btnChangePassword");
                mFirebaseAnalytics.logEvent("main_btnChangePassword_is_click",bundle);
                //End FA
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newPassword.getText().toString().trim().equals("")) {
                    if (newPassword.getText().toString().trim().length() < 6) {
                        newPassword.setError("Password too short, enter minimum 6 characters");
                        progressBar.setVisibility(View.GONE);
                    } else {
                        user.updatePassword(newPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //FA
                                            Bundle bundle = new Bundle();
                                            bundle.putString("btn_name","changePassword");
                                            mFirebaseAnalytics.logEvent("main_changePassword_is_click_success",bundle);
                                            //end FA
                                            Toast.makeText(MainActivity.this, "Password is updated, sign in with new password", Toast.LENGTH_LONG).show();
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            //FA
                                            Bundle bundle = new Bundle();
                                            bundle.putString("btn_name","changePassword");
                                            mFirebaseAnalytics.logEvent("main_changePassword_is_click_not_success",bundle);
                                            //end FA
                                            Toast.makeText(MainActivity.this, "Fail to update password", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                } else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError("Enter password");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        //btnSendResetEmail
        btnSendResetEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                currentEmail.setVisibility(View.VISIBLE);
                newEmail.setVisibility(View.GONE);
                currentPassword.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);

                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.GONE);
                sendEmail.setVisibility(View.VISIBLE);
                remove.setVisibility(View.GONE);
                //FA
                Bundle bundle = new Bundle();
                bundle.putString("btn_name","btnSendResetEmail");
                mFirebaseAnalytics.logEvent("main_btnSendResetEmail_is_click",bundle);
                //End FA
            }
        });
        sendEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if (!currentEmail.getText().toString().trim().equals("")) {
                    auth.sendPasswordResetEmail(currentEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //FA
                                        Bundle bundle = new Bundle();
                                        bundle.putString("btn_name","sendEmail");
                                        mFirebaseAnalytics.logEvent("main_sendEmail_is_success",bundle);
                                        //End FA

                                        Toast.makeText(MainActivity.this, "Reset password email is sent!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        //FA
                                        Bundle bundle = new Bundle();
                                        bundle.putString("btn_name","sendEmail");
                                        mFirebaseAnalytics.logEvent("main_sendEmail_is_not_success",bundle);
                                        //End FA
                                        Toast.makeText(MainActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else {
                    currentEmail.setError("Enter email");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        //btnRemoveUser
        btnRemoveUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //FA
                                        Bundle bundle = new Bundle();
                                        bundle.putString("btn_name","btnRemoveUser");
                                        mFirebaseAnalytics.logEvent("main_btnRemoveUser_is_click_success",bundle);
                                        //End FA
                                        Toast.makeText(MainActivity.this, "Your profile is deleted :(. Create a new account now!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, SignupActivity.class));
                                        finish();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        //FA
                                        Bundle bundle = new Bundle();
                                        bundle.putString("btn_name","btnRemoveUser");
                                        mFirebaseAnalytics.logEvent("main_btnRemoveUser_is_click_not_success",bundle);
                                        //End FA
                                        Toast.makeText(MainActivity.this, "Fail to delete your account", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });

        //signOut
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                //FA
                Bundle bundle = new Bundle();
                bundle.putString("btn_name","signout");
                mFirebaseAnalytics.logEvent("main_signout_is_click_success",bundle);
                //End FA
            }
        });
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
