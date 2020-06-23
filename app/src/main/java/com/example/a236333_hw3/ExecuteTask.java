package com.example.a236333_hw3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.google.firebase.messaging.FirebaseMessaging;

public class ExecuteTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_task);

        // register to receive notifications and capture requests
        LocalBroadcastManager.getInstance(ExecuteTask.this).
                registerReceiver(messageHandler,
                        new IntentFilter("com.example.a236333_hw3_CaptureMessage"));

        FirebaseMessaging.getInstance().subscribeToTopic(RoboCodeSettings.getInstance().currentAnswerTopic);
    }

    // Capture Service ============================================================================
    private BroadcastReceiver messageHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //String userId = intent.getExtras().get("title").toString();
            String data = intent.getExtras().get("message").toString();
            try {
                FirebaseMessaging.getInstance().
                        unsubscribeFromTopic(RoboCodeSettings.getInstance().currentAnswerTopic);
            } catch (Exception ex) {}
            showToast(data);
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
