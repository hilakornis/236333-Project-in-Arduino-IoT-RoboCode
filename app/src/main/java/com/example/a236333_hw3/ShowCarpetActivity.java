package com.example.a236333_hw3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Semaphore;

public class ShowCarpetActivity extends AppCompatActivity {

    static public Semaphore ShowCarpetActivitySemaphore = new Semaphore(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_carpet);
        Button btn = (Button)findViewById(R.id.ready_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCarpetActivitySemaphore.release();
                ShowCarpetActivity.this.finish();
            }
        });
    }
}
