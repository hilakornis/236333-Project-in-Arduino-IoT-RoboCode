package com.example.a236333_hw3.RunEnvironment.Program.Command.Jump;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Program.Command.RCCommand;

public class RCJumpCommand extends RCCommand {

    private int         numberOfRepsToExecute;

    private int         nextNoJumpIndex;
    private int         nextJumpIndex;

    private int         internalLoopCounter;
    private int         actualNextIndex;

    public int getNumberOfRepsToExecute() {
        return numberOfRepsToExecute;
    }

    public void setNumberOfRepsToExecute(int numberOfRepsToExecute) { this.numberOfRepsToExecute = numberOfRepsToExecute; }

    public int getNextJumpIndex() {
        return nextJumpIndex;
    }

    public void setNextJumpIndex(int nextJumpIndex) {
        this.nextJumpIndex = nextJumpIndex;
    }

    public int getNextNoJumpIndex() { return nextNoJumpIndex; }

    public void setNextNoJumpIndex(int nextNoJumpIndex) {
        this.nextNoJumpIndex = nextNoJumpIndex;
    }

    public RCJumpCommand() {
        setNextJumpIndex(NOT_DEF);
        setNextNoJumpIndex(NOT_DEF);
        setNumberOfRepsToExecute(NOT_DEF);
        internalLoopCounter = 0;
        actualNextIndex = NOT_DEF;
    }

    @NonNull
    @Override
    public String toString() {
        return "======== JUMP COMMAND " + "\n" +
                super.toString() +
                "nextIndex = " + (nextNoJumpIndex == NOT_DEF ? "not defined" : nextNoJumpIndex) + "\n" +
                "numberOfReps = " + getNumberOfRepsToExecute() + "\n" +
                "jumpId" + nextJumpIndex + "\n";
    }

    @Override
    public void execute(ArduinoConnector connector) {
        // inc internal counter
        internalLoopCounter++;

        // if we reached max Reps - go to next-no-jump
        if (internalLoopCounter > getNumberOfRepsToExecute() && getNumberOfRepsToExecute() != NOT_DEF) {
            actualNextIndex = getNextNoJumpIndex();
            internalLoopCounter = 0;
        } else {
            actualNextIndex = getNextJumpIndex();
        }
    }

    public int getNextActualIndex() {
        return actualNextIndex;
    }
}
