package com.example.a236333_hw3.Compiler.Command.Execute;

import androidx.annotation.NonNull;

public class RCExecuteRepsTurnLeftCommand extends RCExecuteRepsCommand {
    @NonNull
    @Override
    public String toString() {
        return "======== TURN LEFT COMMAND " + "\n" + super.toString();
    }
}
