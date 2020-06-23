package com.example.a236333_hw3.RunEnvironment.Program.Command.Condition;

import androidx.annotation.NonNull;

public class RCIfFenceCommand extends RCIfCommand {
    @Override
    public int getNextIndex() {
        // TODO : Complete
        return -1;
    }

    @NonNull
    @Override
    public String toString() {
        return "======== FENCE CONDITION " + "\n" +
                super.toString();
    }
}
