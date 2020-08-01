package com.example.a236333_hw3.ui.Tasks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.a236333_hw3.R;
import com.example.a236333_hw3.TasksActivity;
import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.example.a236333_hw3.Tools.SuccessFailureHandler;
import com.example.a236333_hw3.Tools.roboCodeTask;
import com.example.a236333_hw3.roboCodeTaskActivity;
import com.example.a236333_hw3.roboCodeTaskButton;

public class TasksFragment extends Fragment {

    LinearLayout    ButtonsLayout;
    ProgressBar     ProgressBarLoadTasks;
    Context         selfContext;
    ViewGroup       selfContainer;

    public TasksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);
        selfContainer = container;
        ButtonsLayout = v.findViewById(R.id.ButtonsLayout);
        ProgressBarLoadTasks = v.findViewById(R.id.loadTasksProgressBar);
        selfContext = v.getContext();
        // TODO : setTitle("Tasks");
        RoboCodeSettings.getInstance().getRoboCodeTasksAsync(new HandleTaskReqResult());

        return v;
    }


    private class HandleTaskReqResult implements SuccessFailureHandler {
        @Override
        public void onSuccess() {
            getActivity().runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            ProgressBarLoadTasks.setVisibility(View.GONE);
                            for (roboCodeTask task : RoboCodeSettings.getInstance().roboCodeTasks) {
                                if (!task.Active) continue;
                                final roboCodeTaskButton btn = new roboCodeTaskButton(selfContext);
                                btn.RCTask = task;
                                btn.setText(task.Title);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 300
                                );
                                params.setMargins(10, 10, 10, 0);
                                btn.setLayoutParams(params);
                                ButtonsLayout.addView(btn);
                                btn.setEnabled( (! task.Accomplished) &&
                                                (RoboCodeSettings.getInstance().hasPairingCode ||
                                                !RoboCodeSettings.USE_CLUDE) );
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    RoboCodeSettings.getInstance().current = ((roboCodeTaskButton) v).RCTask;
                                    /*Fragment fragment = new roboCodeTaskFragment();
                                        getActivity()
                                            .getSupportFragmentManager()
                                            .beginTransaction()
                                            .remove(TasksFragment.this)
                                            .add(selfContainer.getId(), fragment)
                                            .commit();*/
                                     getActivity().startActivity(new Intent(getActivity(), roboCodeTaskActivity.class));
                                    }
                                });
                            }
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
