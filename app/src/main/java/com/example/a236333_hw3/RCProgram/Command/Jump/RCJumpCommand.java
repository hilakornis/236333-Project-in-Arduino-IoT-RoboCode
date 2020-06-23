package com.example.a236333_hw3.RCProgram.Command.Jump;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.RCProgram.Command.RCCommand;

public class RCJumpCommand extends RCCommand {

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

    @NonNull
    @Override
    public String toString() {
        return "======== JUMP COMMAND " + "\n" +
                super.toString() +
                "nextIndex = " + (nextIndex == NOT_DEF ? "not defined" : nextIndex) + "\n" +
                "numberOfReps = " + getNumberOfRepsToExecute() + "\n" +
                "jumpId" + jumpId + "\n";
    }
}
