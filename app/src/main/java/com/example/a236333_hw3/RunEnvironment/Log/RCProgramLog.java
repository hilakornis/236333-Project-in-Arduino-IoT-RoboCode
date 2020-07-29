package com.example.a236333_hw3.RunEnvironment.Log;

import com.example.a236333_hw3.RunEnvironment.Log.Item.RCProgramLogItem;

import java.util.ArrayList;

public class RCProgramLog {
    private static RCProgramLog _inst;

    private ArrayList<RCProgramLogItem> logger;

    public static RCProgramLog getInstance() {
        if (_inst == null) _inst = new RCProgramLog();
        return _inst;
    }

    public ArrayList<RCProgramLogItem> getLogger() {
        return logger;
    }

    private RCProgramLog() {
        this.logger = new ArrayList<>();
    }

    public void init() {
        this.logger.clear();
    }

    public void log(RCProgramLogItem log) {
        getLogger().add(log);
    }
}
