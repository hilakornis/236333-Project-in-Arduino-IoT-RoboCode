package com.example.a236333_hw3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends AppCompatActivity {

    // ============================================================================================

    EditText passwordEditText, nicknameEditText, EmailEditText;
    Button cancelButton, signButton;

    // ============================================================================================

    String TAG = "SignupActivity";

    FirebaseAuth firebaseAuthenticator;
    FirebaseFirestore db;

    String userID;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseAuthenticator = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initUI();
    }

    private void initUI() {
        // Set UI elements
        EmailEditText = findViewById(R.id.textEmailAddress);
        passwordEditText = findViewById(R.id.textPassword);
        nicknameEditText = findViewById(R.id.textNickname);
        signButton = findViewById(R.id.signButton);
        cancelButton = findViewById(R.id.cancelButton);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cancelButton.setEnabled(true);
                signButton.setEnabled(true);
            }
        });

        setTitle("Sign up");

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cancelButton.setEnabled(false);
                        signButton.setEnabled(false);
                    }
                });

                trySignUp();
            }
        });
    }

    private void trySignUp() {
        final String email, password, nickname;
        email = EmailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        nickname = nicknameEditText.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(SignupActivity.this, "Please enter an email!", Toast.LENGTH_LONG).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cancelButton.setEnabled(true);
                    signButton.setEnabled(true);
                }
            });
            return;
        }
        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(SignupActivity.this, "Please enter a nickname!", Toast.LENGTH_LONG).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cancelButton.setEnabled(true);
                    signButton.setEnabled(true);
                }
            });
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(SignupActivity.this, "Please enter a password!", Toast.LENGTH_LONG).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cancelButton.setEnabled(true);
                    signButton.setEnabled(true);
                }
            });
            return;
        }

    firebaseAuthenticator.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signUpWithEmail:success");
                RoboCodeSettings.getInstance().user = firebaseAuthenticator.getCurrentUser();

                // ------------------------------------
                userID = firebaseAuthenticator.getCurrentUser().getUid();
                userEmail = firebaseAuthenticator.getCurrentUser().getEmail();



                Map<String,Object> user = new HashMap<>();
                Map<String,Integer> finTasks =new HashMap<>();

                user.put("Grade",0);
                user.put("Name",nickname);
                user.put("UID",userID);
                user.put("FinishedTasks",finTasks);

                db.collection("Users").document(email)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.w(TAG, "Setting new user data base: success");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Setting new user data base: failure", e);
                            }
                        });
                // ------------------------------------


                RoboCodeSettings.getInstance().user = firebaseAuthenticator.getCurrentUser();
                startActivity(new Intent(SignupActivity.this, NavigationActivity.class));
                SignupActivity.this.finish();
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signUpWithEmail:failure", task.getException());
                Toast.makeText(SignupActivity.this, "Registration failed",
                        Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cancelButton.setEnabled(true);
                        signButton.setEnabled(true);
                    }
                });
            }
            }
        });



    }
}
