package com.example.a236333_hw3.ui.logout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a236333_hw3.R;
import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.example.a236333_hw3.Tools.SuccessFailureHandler;
import com.example.a236333_hw3.ui.home.HomeFragment;

public class logoutFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        RoboCodeSettings.getInstance().current = null;
        RoboCodeSettings.getInstance().userNickname = null;
        RoboCodeSettings.getInstance().hasPairingCode = false;
        RoboCodeSettings.getInstance().a = "0";
        RoboCodeSettings.getInstance().b = "0";
        RoboCodeSettings.getInstance().c = "0";
        RoboCodeSettings.getInstance().d = "0";
        getActivity().finish();
        return v;
    }
}
