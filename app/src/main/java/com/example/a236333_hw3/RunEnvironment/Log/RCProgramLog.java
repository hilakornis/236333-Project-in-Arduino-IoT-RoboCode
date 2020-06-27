package com.example.a236333_hw3.RunEnvironment.Log;

import com.example.a236333_hw3.RunEnvironment.Program.Color;

public class RCProgramLog {

    private static RCProgramLog _inst;

    public static RCProgramLog getInstance() {
        if (_inst == null) _inst = new RCProgramLog();
        return _inst;
    }

    private RCProgramLog() {
    }

    public void init() {
    }
}
