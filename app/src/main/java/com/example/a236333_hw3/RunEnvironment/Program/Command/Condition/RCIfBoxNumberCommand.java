package com.example.a236333_hw3.RunEnvironment.Program.Command.Condition;

import androidx.annotation.NonNull;

public class RCIfBoxNumberCommand extends RCIfCommand {
    int boxId;

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    @Override
    public int getNextIndex() {
        // TODO : Complete
        return -1;
    }

    @NonNull
    @Override
    public String toString() {
        return "======== BOX NUMBER CONDITION " + "\n" +
                super.toString() +
                "box id = " + getBoxId() + "\n";
    }
}
