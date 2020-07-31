package com.example.a236333_hw3;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a236333_hw3.Camera.APictureCapturingService;
import com.example.a236333_hw3.Camera.PictureCapturingServiceImpl;
import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.example.a236333_hw3.Tools.roboCodeTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class roboCodeTaskActivity extends AppCompatActivity {

    // Title
    TextView titleText;
    TextView titlePoints;

    // Description
    TextView DescriptionData;
    TextView DescriptionHints;
    TextView DescriptionArrangement;
    TextView DescriptionColors;
    TextView DescriptionStepsLimit;

    // Action
    Button      backButton;
    Button      loadImageButton;



    // ============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robo_code_task);

        // save the elements we are using
        loadImageButton = findViewById(R.id.uploadButton);
        titleText = findViewById(R.id.titleText);
        titlePoints = findViewById(R.id.pointsText);
        DescriptionData = findViewById(R.id.taskInfo);
        DescriptionHints = findViewById(R.id.hints);
        DescriptionArrangement = findViewById(R.id.arrangement);
        backButton = findViewById(R.id.backButton);
        DescriptionColors = findViewById(R.id.FenceColors);
        DescriptionStepsLimit = findViewById(R.id.taskStepsLimit);

        setTask(RoboCodeSettings.getInstance().current);

        // Button Click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadImageButton.setEnabled(false);

                if (RoboCodeSettings.getInstance().current.UseCarpet) {
                    startActivity(new Intent(roboCodeTaskActivity.this,
                            ShowCarpetActivity.class));
                }

                Thread d = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (RoboCodeSettings.getInstance().current.UseCarpet) {
                            try {
                                ShowCarpetActivity.ShowCarpetActivitySemaphore.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                        String time = sdf.format(cal.getTime());
                        final String taskId = String.format("%03d", RoboCodeSettings.getInstance().current.ID);

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> docData = new HashMap<>();
                        docData.put("senderId",     RoboCodeSettings.getInstance().user.getEmail());
                        docData.put("recipientId",  RoboCodeSettings.getInstance().user.getEmail());
                        docData.put("senderName",   RoboCodeSettings.getInstance().user.getEmail());
                        docData.put("taskId",       taskId);
                        docData.put("text",         "A picture of the board is required");
                        docData.put("date",         sdf.format(cal.getTime()));
                        final String pairingCode =  RoboCodeSettings.getInstance().a +
                                RoboCodeSettings.getInstance().b +
                                RoboCodeSettings.getInstance().c +
                                RoboCodeSettings.getInstance().d;
                        docData.put("pairingCode", pairingCode);

                        String docName = RoboCodeSettings.getInstance().user.getEmail() + "_" +
                                time +"_" + RoboCodeSettings.getInstance().user.getUid();

                        db.collection("requestMessages").document(docName).set(docData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        RoboCodeSettings.getInstance().currentAnswerTopic = taskId + "_captured_" + pairingCode;

                                        // Calling and closing execute task
                                        startActivity(new Intent(roboCodeTaskActivity.this, ExecuteTask.class));
                                        roboCodeTaskActivity.this.finish();
                                        loadImageButton.setEnabled(true);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showToast("requesting for an image: failure with connecting server!");
                                        loadImageButton.setEnabled(true);
                                    }
                                });
                    }
                });

                d.start();
            }
        });
    }

    private void showToast(final String text) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void setTask(final roboCodeTask task) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Set the task title
                titleText.setText("Task #" + task.ID + ": " + task.Title);
                setTitle("Task #" + task.ID + ": " + task.Title);
                titlePoints.setText("| " + task.Points + " Points");

                // Set the task details
                DescriptionData.setText(task.Description);
                DescriptionHints.setText(task.Hints);
                DescriptionArrangement.setText(task.Arrangement);

                if (task.FenceColors.size() > 0) {
                    DescriptionColors.setVisibility(View.VISIBLE);
                    DescriptionColors.setText(
                            "RoboCode MUST NOT step on the following fence tiles: " +
                            task.getFenceColorsString()
                    );
                } else {
                    DescriptionColors.setVisibility(View.GONE);
                }

                DescriptionStepsLimit.setText( task.stepsLimit == -1 ?
                        "This task does not have any steps limit" :
                        "RoboCode must not execute more then " + String.valueOf(task.stepsLimit) + " steps"
                );

            }
        });
    }
}
