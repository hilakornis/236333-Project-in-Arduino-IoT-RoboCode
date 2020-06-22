package com.example.a236333_hw3.Compiler.Command.Condition;

import androidx.annotation.NonNull;

public class RCIfFenceCommand extends RCIfCommand {
    @NonNull
    @Override
    public String toString() {
        return "======== FENCE CONDITION " + "\n" +
                super.toString();
    }
}
