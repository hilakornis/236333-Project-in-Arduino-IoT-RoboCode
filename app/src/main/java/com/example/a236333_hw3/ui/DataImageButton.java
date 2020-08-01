package com.example.a236333_hw3.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class DataImageButton extends androidx.appcompat.widget.AppCompatImageButton {
    String data;

    public DataImageButton(Context context) {
        super(context);
        data = "NaN";
    }

    public DataImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        data = "NaN";
    }

    public DataImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        data = "NaN";
    }

    public void setData(String s){
        data = s;
    }

    public String getData(){
        return data;
    }
}
