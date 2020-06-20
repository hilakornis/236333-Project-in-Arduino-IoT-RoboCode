package com.example.a236333_hw3.ui.Pairing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.a236333_hw3.CaptureModeActivity;
import com.example.a236333_hw3.R;
import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PairingFragment extends Fragment {

    // UI Elements ================================================================================
    private EditText ARand, BRand, CRand, DRand;
    // For Debug
    private Button uploadButton, doParingButton, switchModeButton;
    private TextView devicePairedTxt;
    // ============================================================================================

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pairing, container, false);

        // UI Elements
        ARand               = (EditText) v.findViewById(R.id.paringAText);
        BRand               = (EditText) v.findViewById(R.id.paringBText);
        CRand               = (EditText) v.findViewById(R.id.paringCText);
        DRand               = (EditText) v.findViewById(R.id.paringDText);
        uploadButton        = (Button)v.findViewById(R.id.startCaptureBtn);
        doParingButton      = (Button)v.findViewById(R.id.DoParingButton);
        switchModeButton    = (Button)v.findViewById(R.id.SwitchModeButton);
        devicePairedTxt     = (TextView)v.findViewById(R.id.devicePaired);

        if (RoboCodeSettings.getInstance().hasPairingCode) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    doParingButton.setText("DISCONNECT");
                    switchModeButton.setEnabled(false);
                    ARand.setEnabled(false);
                    ARand.setText(RoboCodeSettings.getInstance().a);
                    BRand.setEnabled(false);
                    BRand.setText(RoboCodeSettings.getInstance().b);
                    CRand.setEnabled(false);
                    CRand.setText(RoboCodeSettings.getInstance().c);
                    DRand.setEnabled(false);
                    DRand.setText(RoboCodeSettings.getInstance().d);
                    devicePairedTxt.setText("This device is paired!");
                }
            });
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    doParingButton.setText("CONNECT");
                    switchModeButton.setEnabled(true);
                    ARand.setEnabled(true);
                    BRand.setEnabled(true);
                    CRand.setEnabled(true);
                    DRand.setEnabled(true);
                    devicePairedTxt.setText("Enter pairing code to connect devices");
                }
            });
        }

        doParingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RoboCodeSettings.getInstance().hasPairingCode) {
                    RoboCodeSettings.getInstance().hasPairingCode = false;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doParingButton.setText("CONNECT");
                            switchModeButton.setEnabled(true);
                            ARand.setEnabled(true);
                            BRand.setEnabled(true);
                            CRand.setEnabled(true);
                            DRand.setEnabled(true);
                            devicePairedTxt.setText("Enter pairing code to connect devices");
                        }
                    });
                } else {
                    RoboCodeSettings.getInstance().hasPairingCode = true;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doParingButton.setText("DISCONNECT");
                            switchModeButton.setEnabled(false);
                            ARand.setEnabled(false);
                            BRand.setEnabled(false);
                            CRand.setEnabled(false);
                            DRand.setEnabled(false);
                            RoboCodeSettings.getInstance().a = ARand.getText().toString();
                            RoboCodeSettings.getInstance().b = BRand.getText().toString();
                            RoboCodeSettings.getInstance().c = CRand.getText().toString();
                            RoboCodeSettings.getInstance().d = DRand.getText().toString();
                            devicePairedTxt.setText("This device is paired!");
                        }
                    });
                }
            }
        });

        // This button inits a capture request
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                String time = sdf.format(cal.getTime());
                String taskId = String.format("%03d", 8);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> docData = new HashMap<>();
                docData.put("senderId",     RoboCodeSettings.getInstance().user.getEmail());
                docData.put("recipientId",  RoboCodeSettings.getInstance().user.getEmail());
                docData.put("senderName",   RoboCodeSettings.getInstance().user.getEmail());
                docData.put("taskId",       taskId);
                docData.put("text",         "A picture of the board is required");
                docData.put("date",         sdf.format(cal.getTime()));
                String ps = ARand.getText().toString() +
                        BRand.getText().toString() +
                        CRand.getText().toString() +
                        DRand.getText().toString();
                docData.put("pairingCode", ps);

                String docName = RoboCodeSettings.getInstance().user.getEmail() + "_" +
                        time +"_" + RoboCodeSettings.getInstance().user.getUid();

                db.collection("requestMessages").document(docName).set(docData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showToast( "Setting new user data base: success");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showToast("Setting new user data base: failure");
                            }
                        });;

            }
        });

        switchModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().startActivity(new Intent(getActivity(), CaptureModeActivity.class));
                    }
                });
            }
        });

        return v;
    }

    private void showToast(final String text) {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
