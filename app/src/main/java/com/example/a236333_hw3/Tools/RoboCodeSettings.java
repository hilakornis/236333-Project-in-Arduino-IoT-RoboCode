package com.example.a236333_hw3.Tools;

public class RoboCodeSettings {

    private static RoboCodeSettings _inst;

    public static RoboCodeSettings getInstance() {
        if (_inst == null) _inst = new RoboCodeSettings();
        return _inst;
    }

    private RoboCodeSettings()
    {

    }

}
