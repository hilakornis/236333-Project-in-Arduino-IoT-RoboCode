package com.example.a236333_hw3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class ExecuteTask extends AppCompatActivity {


    private int arrived = 0;
    ArrayList<String> arrivals;

    LinearLayout Step1, Step2;
    TextView r1, r2, r3, r4, r5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_task);

        // register to receive notifications and capture requests
        LocalBroadcastManager.getInstance(ExecuteTask.this).
                registerReceiver(messageHandler,
                        new IntentFilter("com.example.a236333_hw3_CaptureMessage"));

        arrived = 0;
        arrivals = new ArrayList<String>();

        Step1 = findViewById(R.id.Step1Panel);
        Step2 = findViewById(R.id.Step2Panel);
        r1 = findViewById(R.id.res_1);
        r2 = findViewById(R.id.res_2);
        r3 = findViewById(R.id.res_3);
        r4 = findViewById(R.id.res_4);
        r5 = findViewById(R.id.res_5);
        Step1.setVisibility(View.VISIBLE);
        Step2.setVisibility(View.GONE);


        FirebaseMessaging.getInstance().subscribeToTopic(RoboCodeSettings.getInstance().currentAnswerTopic);
    }

    // Capture Service ============================================================================
    private BroadcastReceiver messageHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //String userId = intent.getExtras().get("title").toString();
            String data = intent.getExtras().get("message").toString();
            showToast(data);
            arrivals.add(data);
            arrived++;
            showToast(arrived + ": " + data);

            if (arrived >= RoboCodeSettings.NUM_OF_CAPTURES) {
                try {
                    FirebaseMessaging.getInstance().
                            unsubscribeFromTopic(RoboCodeSettings.getInstance().currentAnswerTopic);
                    } catch (Exception ex) {
                }

                ExecuteTask.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Step1.setVisibility(View.GONE);
                        Step2.setVisibility(View.VISIBLE);
                        r1.setText(arrivals.get(0));
                        r2.setText(arrivals.get(1));
                        /*r3.setText(arrivals.get(2));
                        r4.setText(arrivals.get(3));
                        r5.setText(arrivals.get(4));*/
                    }
                });
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            FirebaseMessaging.getInstance().
                    unsubscribeFromTopic(RoboCodeSettings.getInstance().currentAnswerTopic);
        } catch (Exception ex) {}
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
}
