package com.example.a236333_hw3.RCProgram.Command.Execute;

import androidx.annotation.NonNull;

public class RCExecuteForkDownCommand extends RCExecuteCommand {
    @NonNull
    @Override
    public String toString() {
        return "======== FORK DOWN COMMAND " + "\n" +
                super.toString();
    }
}
