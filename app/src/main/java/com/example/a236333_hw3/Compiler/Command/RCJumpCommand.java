package com.example.a236333_hw3.Compiler.Command;

public class RCJumpCommand extends RCCommand {

    public final int NOT_DEF = -1;

    private int         numberOfRepsToExecute;
    private int         jumpId;
    private int         nextIndex;

    public RCJumpCommand() {
        setNextIndex(NOT_DEF);
        setNumberOfRepsToExecute(NOT_DEF);
    }

    public int getNumberOfRepsToExecute() {
        return numberOfRepsToExecute;
    }

    public void setNumberOfRepsToExecute(int numberOfRepsToExecute) {
        this.numberOfRepsToExecute = numberOfRepsToExecute;
    }

    public int getJumpId() {
        return jumpId;
    }

    public void setJumpId(int jumpId) {
        this.jumpId = jumpId;
    }

    public int getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(int nextIndex) {
        this.nextIndex = nextIndex;
    }
}
