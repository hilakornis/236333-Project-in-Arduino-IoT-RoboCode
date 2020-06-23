package com.example.a236333_hw3.RCProgram.Command.Execute;

import androidx.annotation.NonNull;

public class RCExecuteRepsTurnRightCommand extends RCExecuteRepsCommand {
    @NonNull
    @Override
    public String toString() {
        return "======== TURN RIGHT COMMAND " + "\n" + super.toString();
    }
}
