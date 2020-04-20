package com.example.a236333_hw3.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.a236333_hw3.R;
import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.example.a236333_hw3.Tools.SuccessFailureHandler;
import com.example.a236333_hw3.Tools.roboCodeTask;
import com.example.a236333_hw3.roboCodeTaskActivity;
import com.example.a236333_hw3.roboCodeTaskButton;
import com.example.a236333_hw3.ui.Tasks.TasksFragment;

public class HomeFragment extends Fragment {

    TextView userNicknameTextView;
    LinearLayout homeFragment_finishLoadLayout, homeFragment_doLoadLayout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        userNicknameTextView = v.findViewById(R.id.homeFragment_userIdTextView);
        homeFragment_finishLoadLayout = v.findViewById(R.id.homeFragment_finishLoadLayout);
        homeFragment_doLoadLayout = v.findViewById(R.id.homeFragment_doLoadLayout);

        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        homeFragment_finishLoadLayout.setVisibility(View.GONE);
                        homeFragment_doLoadLayout.setVisibility(View.VISIBLE);
                    }
                }
        );

        RoboCodeSettings.getInstance().getUserDataAsync(new HomeFragment.HandleTaskReqResult());

        return v;
    }

    private class HandleTaskReqResult implements SuccessFailureHandler {
        @Override
        public void onSuccess() {
            getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        userNicknameTextView.setText(RoboCodeSettings.getInstance().userNickname);
                        homeFragment_finishLoadLayout.setVisibility(View.VISIBLE);
                        homeFragment_doLoadLayout.setVisibility(View.GONE);
                    }
                }
            );
        }

        @Override
        public void onFailure() {
            // TODO - hanldle
        }
    }

}
