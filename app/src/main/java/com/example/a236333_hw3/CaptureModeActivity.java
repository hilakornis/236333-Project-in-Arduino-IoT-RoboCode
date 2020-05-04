package com.example.a236333_hw3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.example.a236333_hw3.Camera.APictureCapturingService;
import com.example.a236333_hw3.Camera.PictureCapturingListener;
import com.example.a236333_hw3.Camera.PictureCapturingServiceImpl;

public class CaptureModeActivity extends AppCompatActivity implements
        PictureCapturingListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String[] requiredPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;

    // UI Elements ================================================================================
    private ImageView uploadBackPhoto;
    private ImageView uploadFrontPhoto;
    private Button uploadButton;
    // ============================================================================================

    // Capture Service ============================================================================
    private BroadcastReceiver messageHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CaptureModeActivity.this.showToast("Starting capture!");
            APictureCapturingService pictureService =
                    PictureCapturingServiceImpl.getInstance(CaptureModeActivity.this);
            pictureService.startCapturing(CaptureModeActivity.this);
        }
    };
    // ============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(this).registerReceiver(messageHandler, new IntentFilter("com.example.a236333_hw3_CaptureMessage"));

        setContentView(R.layout.activity_capture_mode);
        checkPermissions();
        uploadBackPhoto     = (ImageView) findViewById(R.id.backIV);
        uploadFrontPhoto    = (ImageView) findViewById(R.id.frontIV);
        uploadButton        = (Button) findViewById(R.id.startCaptureBtn);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureModeActivity.this.showToast("Starting capture!");
                APictureCapturingService pictureService =
                        PictureCapturingServiceImpl.getInstance(CaptureModeActivity.this);
                pictureService.startCapturing(CaptureModeActivity.this);
            }
        });
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
        if (pictureData != null && pictureUrl != null) {
            runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
                        final int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                        final Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                        if (pictureUrl.contains("0_pic.jpg")) {
                            uploadBackPhoto.setImageBitmap(scaled);
                        } else if (pictureUrl.contains("1_pic.jpg")) {
                            uploadFrontPhoto.setImageBitmap(scaled);
                        }
                    }
                });
            showToast("Picture saved to " + pictureUrl);
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



    // Capture Service ============================================================================
    @Override
    protected void onPause() {
        super.onPause();
        try{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(messageHandler);
        } catch (Exception ex) {}
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
        try{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(messageHandler);
        } catch (Exception ex) {}
    }
    // ============================================================================================
}
