package com.example.a236333_hw3.Compiler.Command;

public class RCIfCommand extends RCCommand {
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
}
