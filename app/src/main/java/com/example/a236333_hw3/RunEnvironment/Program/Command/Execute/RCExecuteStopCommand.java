package com.example.a236333_hw3.RunEnvironment.Program.Command.Execute;

import androidx.annotation.NonNull;

public class RCExecuteStopCommand extends RCExecuteCommand {
    @NonNull
    @Override
    public String toString() {
        return "======== STOP COMMAND " + "\n" + super.toString();
    }

}