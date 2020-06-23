package com.example.a236333_hw3.RCProgram.Command.Condition;

import androidx.annotation.NonNull;

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
}
