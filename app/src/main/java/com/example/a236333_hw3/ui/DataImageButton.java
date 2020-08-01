package com.example.a236333_hw3.ui;

import android.content.Context;
import android.widget.ImageButton;

public class DataImageButton extends androidx.appcompat.widget.AppCompatImageButton {
    String data;

    public DataImageButton(Context context) {
        super(context);
    }

    public void setData(String s){
        data = s;
    }

    public String getData(){
        return data;
    }
}
