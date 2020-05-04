package com.example.a236333_hw3.ui.Pairing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.a236333_hw3.CaptureModeActivity;
import com.example.a236333_hw3.R;

public class PairingFragment extends Fragment {

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

        return v;
    }

}
