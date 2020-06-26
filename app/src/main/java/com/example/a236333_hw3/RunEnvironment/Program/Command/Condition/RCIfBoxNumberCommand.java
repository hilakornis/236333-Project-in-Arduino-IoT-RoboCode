package com.example.a236333_hw3.RunEnvironment.Program.Command.Condition;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;

public class RCIfBoxNumberCommand extends RCIfCommand {
    int boxId;

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    @NonNull
    @Override
    public String toString() {
        return "======== BOX NUMBER CONDITION " + "\n" +
                super.toString() +
                "box id = " + getBoxId() + "\n";
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
