/*
Status: Build ok
Run: ok
Firebase Analytics(FA): ok
Function: ok
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

import org.w3c.dom.Text;

public class SignupActivity extends AppCompatActivity {

    //Declare FA (Firebase Analytics) object
    private FirebaseAnalytics mFirebaseAnalytics;


    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get FA instance
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        //btnResetPassword
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start_forgot_password_signup event
                Bundle bundle = new Bundle();
                bundle.putString("btn_name", "btnResetPassword");
                mFirebaseAnalytics.logEvent("signup_btnResetPassword_is_click_success", bundle);
                //end_forgot_password_signup event
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });
        //btnSignIn
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start_forgot_password_signup event
                Bundle bundle = new Bundle();
                bundle.putString("btn_name", "btnSignIn");
                mFirebaseAnalytics.logEvent("signup_btnSignIn_is_click_success", bundle);
                //end_forgot_password_signup event

                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
        //btnSignUp
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //Create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                //if sign in fails, display a message to the user.
                                //if sign in succeed, the auth state listener will be notified and logic to handle
                                //the signed in user can be handled in the listenter.
                                if (!task.isSuccessful()) {
                                    //start_forgot_password_signup event
                                    Bundle bundle = new Bundle();
                                    bundle.putString("btn_name", "btnSignUp");
                                    mFirebaseAnalytics.logEvent("signup_btnSignUp_is_click_not_success", bundle);
                                    //end_forgot_password_signup event
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();

                                } else {
                                    //start_forgot_password_signup event
                                    Bundle bundle = new Bundle();
                                    bundle.putString("btn_name", "btnSignUp");
                                    mFirebaseAnalytics.logEvent("signup_btnSignUp_is_click_success", bundle);
                                    //end_forgot_password_signup event
                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
