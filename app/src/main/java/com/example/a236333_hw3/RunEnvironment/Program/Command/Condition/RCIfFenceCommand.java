package com.example.a236333_hw3.RunEnvironment.Program.Command.Condition;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;

public class RCIfFenceCommand extends RCIfCommand {
    @NonNull
    @Override
    public String toString() {
        return "======== FENCE CONDITION " + "\n" +
                super.toString();
    }

    @Override
    public void execute(ArduinoConnector connector) {
        // TODO : implement
    }

    @Override
    public int getNextNoJumpIndex() {
        // TODO : Complete
        return -1;
    }
}
