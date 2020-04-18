package com.example.a236333_hw3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.example.a236333_hw3.Tools.SuccessFailureHandler;
import com.example.a236333_hw3.Tools.roboCodeTask;

import java.util.Vector;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    LinearLayout ButtonsLayout;
    ProgressBar ProgressBarLoadTasks;
    TasksActivity selfContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        ButtonsLayout = findViewById(R.id.ButtonsLayout);
        ProgressBarLoadTasks = findViewById(R.id.loadTasksProgressBar);
        selfContext = this;
        setTitle("Tasks");
        RoboCodeSettings.getInstance().getRoboCodeTasksAsync(new HandleTaskReqResult());
    }

    private class HandleTaskReqResult implements SuccessFailureHandler {
           @Override
        public void onSuccess() {
            selfContext.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        ProgressBarLoadTasks.setVisibility(View.GONE);
                        for (roboCodeTask task : RoboCodeSettings.getInstance().roboCodeTasks) {
                            final roboCodeTaskButton btn = new roboCodeTaskButton(selfContext);
                            btn.RCTask = task;
                            btn.setText(task.Title);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, 300
                            );
                            params.setMargins(10, 10, 10, 0);
                            btn.setLayoutParams(params);
                            ButtonsLayout.addView(btn);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RoboCodeSettings.getInstance().current = ((roboCodeTaskButton) v).RCTask;
                                    startActivity(new Intent(TasksActivity.this, roboCodeTaskActivity.class));
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