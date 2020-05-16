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
    private Button uploadButton;
    // ============================================================================================

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pairing, container, false);

        Button btn = v.findViewById(R.id.SwitchModeButton);

        btn.setOnClickListener(new View.OnClickListener() {
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

        // UI Elements
        ARand               = (EditText) v.findViewById(R.id.paringAText);
        BRand               = (EditText) v.findViewById(R.id.paringBText);
        CRand               = (EditText) v.findViewById(R.id.paringCText);
        DRand               = (EditText) v.findViewById(R.id.paringDText);
        uploadButton        = (Button)v.findViewById(R.id.startCaptureBtn);

        // This button inits a capture request
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                String time = sdf.format(cal.getTime());

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> docData = new HashMap<>();
                docData.put("senderId",     RoboCodeSettings.getInstance().user.getEmail());
                docData.put("recipientId",  RoboCodeSettings.getInstance().user.getEmail());
                docData.put("senderName",   RoboCodeSettings.getInstance().user.getEmail());
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
