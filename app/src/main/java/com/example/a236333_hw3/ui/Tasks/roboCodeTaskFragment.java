package com.example.a236333_hw3.ui.Tasks;

import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.example.a236333_hw3.Tools.roboCodeTask;
import android.content.pm.PackageManager;
import androidx.fragment.app.Fragment;
import android.content.ContentValues;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.view.View;
import android.os.Bundle;
import android.os.Build;
import android.Manifest;
import android.net.Uri;

import com.example.a236333_hw3.R;

public class roboCodeTaskFragment extends Fragment {

    // Title
    TextView    titleText;
    TextView    titlePoints;

    // Description
    TextView    DescriptionData;
    TextView    DescriptionHints;
    TextView    DescriptionArrangement;

    // Action
    Button      backButton;
    Button      loadImageButton;
    ImageView   myImageView;
    Button      approveButton;
    ViewGroup       selfContainer;

    // ============================================================================================

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri image_uri;

    public roboCodeTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_robo_code_task, container, false);
        selfContainer = container;
        // save the elements we are using
        loadImageButton = v.findViewById(R.id.loadButton);
        myImageView = v.findViewById(R.id.centerView);
        approveButton = v.findViewById(R.id.approveButton);
        titleText = v.findViewById(R.id.TitleText);
        titlePoints = v.findViewById(R.id.pointsText);
        DescriptionData = v.findViewById(R.id.taskInfo);
        DescriptionHints = v.findViewById(R.id.hints);
        DescriptionArrangement = v.findViewById(R.id.arrangement);
        backButton = v.findViewById(R.id.backButton);

        setTask(RoboCodeSettings.getInstance().current);

        // Button Click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoboCodeSettings.getInstance().current = null;
                Fragment fragment = new TasksFragment();
                getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .remove(roboCodeTaskFragment.this)
                    .add(selfContainer.getId(), fragment)
                    .commit();
            }
        });

        loadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                        getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {
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

        return v;
    }

    public void setTask(final roboCodeTask task) {

        image_uri = Uri.EMPTY;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // reset the approve solution option
                approveButton           .setVisibility(View.GONE);
                myImageView             .setImageURI(Uri.EMPTY);
                loadImageButton         .setText("Upload Solution");

                // Set the task title
                titleText               .setText("Task #" + task.ID + ": " + task.Title);
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
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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
                    Toast.makeText(getActivity(), "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            myImageView.setImageURI(image_uri);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadImageButton.setText("Change Solution");
                    approveButton.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}