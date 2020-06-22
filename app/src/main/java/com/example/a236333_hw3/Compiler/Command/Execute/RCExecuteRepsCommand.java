package com.example.a236333_hw3.Compiler.Command.Execute;

import androidx.annotation.NonNull;

public class RCExecuteRepsCommand extends RCExecuteCommand {

    private int numberOfRepsToExcute;

    public RCExecuteRepsCommand() {
        setNumberOfRepsToExcute(NOT_DEF);
    }

    public int getNumberOfRepsToExcute() {
        return numberOfRepsToExcute;
    }

    public void setNumberOfRepsToExcute(int numberOfRepsToExcute) {
        this.numberOfRepsToExcute = numberOfRepsToExcute;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() +
                "NumberOfReps = " + (getNumberOfRepsToExcute() == NOT_DEF ? "not defined" : getNumberOfRepsToExcute()) + "\n";
    }
}
