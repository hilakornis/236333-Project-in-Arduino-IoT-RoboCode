package com.example.a236333_hw3.RCProgram.Command.Execute;

import androidx.annotation.NonNull;

public class RCExecuteRepsGoBackwardCommand extends RCExecuteRepsCommand {
    @NonNull
    @Override
    public String toString() {
        return "======== GO BACKWARDS COMMAND " + "\n" + super.toString();
    }
}
