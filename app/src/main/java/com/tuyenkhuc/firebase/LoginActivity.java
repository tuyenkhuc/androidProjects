/*
    Function: ok
    FA: ok
    Date: 9/9/2019
 */

package com.tuyenkhuc.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    String id="login";
    String name="";
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get FA instance
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        //set the view now
        setContentView(R.layout.activity_login);
//        if (auth.getCurrentUser() == null) {
//            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
//            finish();
//        }

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start FA
                Bundle bundle = new Bundle();
//                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,id);
//                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,name);
//                bundle.putString(FirebaseAnalytics.Event.VIEW_ITEM,"button");
                bundle.putString("btn_name","btnSignup");
                mFirebaseAnalytics.logEvent("login_btnSignup_is_click_success",bundle);
                //end FA
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start FA

                Bundle bundle = new Bundle();
//                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,id);
//                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,name);
//                bundle.putString(FirebaseAnalytics.Event.VIEW_ITEM,"button");
                bundle.putString("btn_name","btnReset");
                mFirebaseAnalytics.logEvent("login_btnReset_is_click_success",bundle);
                //end FA
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //if sign in fails, display a message to the user. If sign in succedds the auth
                                //state listener will be notified and logic to handle the signed in user can be handled in the listener
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    //FA
                                    Bundle bundle = new Bundle();
                                    bundle.putString("btn_name","btnLogin");
                                    mFirebaseAnalytics.logEvent("login_btnLogin_is_click_not_success",bundle);
                                    //End FA
                                    //there was a error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_fail), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    //start FA
                                    Bundle bundle = new Bundle();
                                    bundle.putString("btn_name","btnLogin");
                                    mFirebaseAnalytics.logEvent("login_btnLogin_is_click_success",bundle);
                                    //end FA
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
