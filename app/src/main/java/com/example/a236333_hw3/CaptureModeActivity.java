package com.example.a236333_hw3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.a236333_hw3.Camera.APictureCapturingService;
import com.example.a236333_hw3.Camera.PictureCapturingListener;
import com.example.a236333_hw3.Camera.PictureCapturingServiceImpl;
import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class CaptureModeActivity extends AppCompatActivity implements
        PictureCapturingListener, ActivityCompat.OnRequestPermissionsResultCallback {


    private static final String[] requiredPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;

    private String pairingCode;
    private String taskId = "008";

    private boolean allow_new = true;
    private int     capture_loop_counter = 0;
    private String  timeStamp = "";

    // UI Elements ================================================================================
    private Button backButton;
    private TextView ARand, BRand, CRand, DRand;
    // ============================================================================================

    // WakeLock Service ===========================================================================
    PowerManager.WakeLock wakeLock;
    // ============================================================================================

    static int id = 0;
    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_mode);

        // Make sure I have the right permissions
        checkPermissions();

        // UI Elements
        ARand               = (TextView) findViewById(R.id.paringCodeRandA);
        BRand               = (TextView) findViewById(R.id.paringCodeRandB);
        CRand               = (TextView) findViewById(R.id.paringCodeRandC);
        DRand               = (TextView) findViewById(R.id.paringCodeRandD);
        backButton          = (Button)   findViewById(R.id.backToRegularModeBtn);

        // Randomize a Pairing code
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Random randomer = new Random();
                    ARand.setText(Integer. toString(randomer.nextInt(9)));
                    BRand.setText(Integer. toString(randomer.nextInt(9)));
                    CRand.setText(Integer. toString(randomer.nextInt(9)));
                    DRand.setText(Integer. toString(randomer.nextInt(9)));
                    pairingCode = ARand.getText().toString() +
                            BRand.getText().toString() +
                            CRand.getText().toString() +
                            DRand.getText().toString();
                } catch (Exception ex) {}
            }
        });

        // register to receive notifications and capture requests
        LocalBroadcastManager.getInstance(this).
                registerReceiver(messageHandler,
                        new IntentFilter("com.example.a236333_hw3_CaptureMessage"));
        FirebaseMessaging.getInstance().subscribeToTopic("CaptureRequests_" + pairingCode);

        // Clicking the back button will exit the activity
        // note that more actions are done in the OnDestroy
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureModeActivity.this.finish();
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Make sure that the screen will not get locked
        PowerManager powerManager = (PowerManager)this.getBaseContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON , "myLock");
        wakeLock.acquire();
    }

    /**
     * checking  permissions at Runtime.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        final List<String> neededPermissions = new ArrayList<>();
        for (final String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission);
            }
        }
        if (!neededPermissions.isEmpty()) {
            requestPermissions(neededPermissions.toArray(new String[]{}),
                    MY_PERMISSIONS_REQUEST_ACCESS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_CODE: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    checkPermissions();
                }
            }
        }
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

    /**
     * We've finished taking pictures from all phone's cameras
     */
    @Override
    public void onDoneCapturingAllPhotos(TreeMap<String, byte[]> picturesTaken) {

        if (picturesTaken != null && !picturesTaken.isEmpty()) {
            showToast("Done capturing all photos!");
            return;
        }
        showToast("No camera detected!");
    }

    /**
     * Displaying the pictures taken.
     */
    @Override
    public void onCaptureDone(final String pictureUrl, final byte[] pictureData) {
        try {
            if (pictureData != null && pictureUrl != null) {

                FirebaseUser user = RoboCodeSettings.getInstance().user;

                final String path =
                        "Users"
                        + "/" + user.getEmail()
                        + "/" + taskId + "_" + timeStamp
                        + "/" + capture_loop_counter + "_" + user.getEmail() + "_" + taskId + "_captured_" + pairingCode + ".jpg";
                capture_loop_counter++;

                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                final Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
                                final int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                                final Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                                if (pictureUrl.contains("0_pic.jpg")) {
                                    //uploadBackPhoto.setImageBitmap(scaled)
                                    // ;
                                    showToast("Image is been uploaded ...");

                                    //this is image_uri
                                    // Alon :: Here I send the picture to shahaf's function
                                    StorageReference riversRef =
                                        FirebaseStorage.getInstance().getReference().child(path);

                                    try {
                                        InputStream stream = new FileInputStream(new File(pictureUrl));

                                        riversRef.putStream(stream)//putFile(Uri.parse(pictureUrl))
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        // Get a URL to the uploaded content
                                                        Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                                                        showToast("Image was uploaded");
                                                        startOnCaptureFlow();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        showToast("Failure! image was not uploaded");
                                                        allow_new = true;
                                                    }
                                                });

                                    } catch (FileNotFoundException e) {
                                        showToast("Failure! image was not uploaded! file stream error!");
                                        allow_new = true;
                                    }
                                }// else if (pictureUrl.contains("1_pic.jpg")) {
                                //                                    //uploadFrontPhoto.setImageBitmap(scaled);
                                //                                //}
                            }
                        });
                //showToast("Picture saved to " + pictureUrl);
            }
        } catch (Exception ex) {
            showToast("Error! message: " + ex.getMessage());
        }
    }

    // Capture Service ============================================================================
    private BroadcastReceiver messageHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (allow_new) {
                allow_new = false;
                timeStamp = String.valueOf(System.currentTimeMillis());
                capture_loop_counter = 0;

                String userId = intent.getExtras().get("title").toString();
                taskId = intent.getExtras().get("message").toString();
                CaptureModeActivity.this.showToast("Starting capture! task id is: " + taskId + ", userId is " + userId);

                if (RoboCodeSettings.getInstance().user.getEmail().equals(userId)) {
                    startOnCaptureFlow();
                } else {
                    showToast("not the same user - " + userId + " - ignore!");
                }
            }
        }
    };

    private void startOnCaptureFlow() {
        if (capture_loop_counter < RoboCodeSettings.NUM_OF_CAPTURES) {
            try {
                APictureCapturingService pictureService =
                        PictureCapturingServiceImpl.getInstance(CaptureModeActivity.this);

                pictureService.startCapturing(CaptureModeActivity.this);
            } catch (Exception ex) {
                showToast("Error :( ... message: " + ex.getMessage());
            }
        } else allow_new = true;
    }



    @Override
    protected void onPause() {
        super.onPause();
        try{
            //wakeLock.release();
        } catch (Exception ex) {}
        try{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(messageHandler);
        } catch (Exception ex) {}
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
           //wakeLock.acquire();
        } catch (Exception ex) {}
        try{
            LocalBroadcastManager.getInstance(this).registerReceiver(messageHandler, new IntentFilter("com.example.a236333_hw3_CaptureMessage"));
        } catch (Exception ex) {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("CaptureRequests_" + pairingCode);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        try{
            wakeLock.release();
        } catch (Exception ex) {}
        try{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(messageHandler);
        } catch (Exception ex) {}
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
        try{
            wakeLock.release();
        } catch (Exception ex) {}
        try{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(messageHandler);
        } catch (Exception ex) {}
    }
    // ============================================================================================
}
