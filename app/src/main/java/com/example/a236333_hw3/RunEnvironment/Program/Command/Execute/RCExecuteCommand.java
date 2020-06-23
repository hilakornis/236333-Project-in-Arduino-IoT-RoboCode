package com.example.a236333_hw3.RunEnvironment.Program.Command.Execute;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.RunEnvironment.Program.Command.RCCommand;

public class RCExecuteCommand extends RCCommand {

    private int         nextIndex;

    public RCExecuteCommand() {
        setNextIndex(NOT_DEF);
    }

    public int getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(int nextIndex) {
        this.nextIndex = nextIndex;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() +
                "nextIndex = " + (nextIndex == NOT_DEF ? "not defined" : nextIndex) + "\n";
    }
}
