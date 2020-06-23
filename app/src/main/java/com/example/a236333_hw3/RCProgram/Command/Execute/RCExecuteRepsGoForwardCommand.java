package com.example.a236333_hw3.RCProgram.Command.Execute;

import androidx.annotation.NonNull;

public class RCExecuteRepsGoForwardCommand extends RCExecuteRepsCommand {
    @NonNull
    @Override
    public String toString() {
        return "======== GO FORWARDS COMMAND " + "\n" + super.toString();
    }
}
