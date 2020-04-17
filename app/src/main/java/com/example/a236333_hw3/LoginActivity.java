package com.example.a236333_hw3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // ============================================================================================

    Button logInButton, signUpButton;
    EditText passwordEditText, EmailEditText;

    // ============================================================================================

    String TAG = "LoginActivity";
    FirebaseAuth firebaseAuthenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuthenticator = FirebaseAuth.getInstance();
        initUI();
    }

    private void initUI() {
        // Set UI elements
        EmailEditText = findViewById(R.id.textEmailAddress);
        passwordEditText = findViewById(R.id.textPassword);
        logInButton = findViewById(R.id.logInButton);
        signUpButton = findViewById(R.id.signUpButton);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logInButton.setEnabled(true);
                signUpButton.setEnabled(true);
            }
        });

        setTitle("Login");

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logInButton.setEnabled(false);
                        signUpButton.setEnabled(false);
                    }
                });

                tryLogin();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void tryLogin() {
        String email, password;
        email = EmailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Please enter email!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuthenticator.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            RoboCodeSettings.getInstance().user = firebaseAuthenticator.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, TasksActivity.class));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    logInButton.setEnabled(true);
                                    signUpButton.setEnabled(true);
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    logInButton.setEnabled(true);
                                    signUpButton.setEnabled(true);
                                }
                            });
                        }
                    }
                });
    }
}
