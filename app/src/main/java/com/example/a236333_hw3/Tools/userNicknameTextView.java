package com.example.a236333_hw3.Tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class userNicknameTextView extends TextView {
    public userNicknameTextView(Context context) {
        super(context);
        this.setText(RoboCodeSettings.getInstance().user.getEmail());
    }

    public userNicknameTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setText(RoboCodeSettings.getInstance().user.getEmail());
    }

    public userNicknameTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setText(RoboCodeSettings.getInstance().user.getEmail());
    }

    public userNicknameTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setText(RoboCodeSettings.getInstance().user.getEmail());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
                this.setText(RoboCodeSettings.getInstance().userNickname);
        }
        catch (Exception ex) {
        }
        super.onDraw(canvas);
    }
}
