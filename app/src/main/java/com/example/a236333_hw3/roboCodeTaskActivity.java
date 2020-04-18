package com.example.a236333_hw3;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a236333_hw3.Tools.roboCodeTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class roboCodeTaskActivity extends AppCompatActivity {

    // Title
    TextView titleText;
    TextView titlePoints;

    // Description
    TextView DescriptionData;
    TextView DescriptionHints;
    TextView DescriptionArrangement;

    // Action
    Button      backButton;
    Button      loadImageButton;
    ImageView   myImageView;
    Button      approveButton;

    private StorageReference mStorageRef;

    // ============================================================================================

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
//    Uri image_uri;//where the image is located.
    public Uri image_uri;//where the image is located.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robo_code_task);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        // save the elements we are using
        loadImageButton = findViewById(R.id.uploadButton);
        myImageView = findViewById(R.id.centerView);
        approveButton = findViewById(R.id.approveButton);
        titleText = findViewById(R.id.titleText);
        titlePoints = findViewById(R.id.pointsText);
        DescriptionData = findViewById(R.id.taskInfo);
        DescriptionHints = findViewById(R.id.hints);
        DescriptionArrangement = findViewById(R.id.arrangement);
        backButton = findViewById(R.id.backButton);

        setTask(roboCodeTask.current);

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {
                        String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        // permission already given
                        openCamera();
                    }
                } else {
                    // OS is old
                    openCamera();
                }
            }
        });

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(roboCodeTaskActivity.this, "Image is been uploaded ...", Toast.LENGTH_SHORT).show();
//                Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
                //this is image_uri

                StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

                riversRef.putFile(image_uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                                Toast.makeText(roboCodeTaskActivity.this, "Image was uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                Toast.makeText(roboCodeTaskActivity.this, "Failure! image was not uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    public void setTask(final roboCodeTask task) {

        image_uri = Uri.EMPTY;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // reset the approve solution option
                approveButton           .setVisibility(View.GONE);
                myImageView             .setImageURI(Uri.EMPTY);
                loadImageButton         .setText("Upload Solution");

                // Set the task title
                titleText               .setText("Task #" + task.ID + ": " + task.Title);
                setTitle("Task #" + task.ID + ": " + task.Title);
                titlePoints             .setText("| " + task.Points +" Points");

                // Set the task details
                DescriptionData         .setText(task.Description);
                DescriptionHints        .setText(task.Hints);
                DescriptionArrangement  .setText(task.Arrangement);
            }
        });
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            myImageView.setImageURI(image_uri);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadImageButton.setText("Change Solution");
                    approveButton.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
