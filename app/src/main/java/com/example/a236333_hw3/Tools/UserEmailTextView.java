package com.example.a236333_hw3.Tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class UserEmailTextView extends TextView {
    public UserEmailTextView(Context context) {
        super(context);
        this.setText(RoboCodeSettings.getInstance().user.getEmail());
    }

    public UserEmailTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setText(RoboCodeSettings.getInstance().user.getEmail());
    }

    public UserEmailTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setText(RoboCodeSettings.getInstance().user.getEmail());
    }

    public UserEmailTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setText(RoboCodeSettings.getInstance().user.getEmail());
     }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            if (this.getText() != RoboCodeSettings.getInstance().user.getEmail())
                this.setText(RoboCodeSettings.getInstance().user.getEmail());
        }
        catch (Exception ex) {
        }
        super.onDraw(canvas);
    }
}
