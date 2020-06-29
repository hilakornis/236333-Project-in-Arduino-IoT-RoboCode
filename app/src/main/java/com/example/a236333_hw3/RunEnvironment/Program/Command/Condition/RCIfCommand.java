package com.example.a236333_hw3.RunEnvironment.Program.Command.Condition;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.RunEnvironment.Program.Command.RCCommand;

public abstract class RCIfCommand extends RCCommand {
    private int nextTrue;
    private int nextFalse;

    public RCIfCommand() {
        setNextFalse(-1);
        setNextTrue(-1);
    }

    public int getNextTrue() {
        return nextTrue;
    }

    public void setNextTrue(int nextTrue) {
        this.nextTrue = nextTrue;
    }

    public int getNextFalse() {
        return nextFalse;
    }

    public void setNextFalse(int nextFalse) {
        this.nextFalse = nextFalse;
    }

    public abstract int getNextActualIndex();

    @NonNull
    @Override
    public String toString() {
        return super.toString() +
                "true index = " + getNextTrue() + "\n" +
                "false index = " + getNextFalse() + "\n" ;
    }
}
