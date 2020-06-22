package com.example.a236333_hw3.Compiler.Command;

public class RCJumpCommand extends RCCommand {

    private int         numberOfRepsToExecute;
    private int         jumpId;
    private int         nextIndex;

    public RCJumpCommand() {
        setNextIndex(-1);
        setNumberOfRepsToExecute(-1);
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
