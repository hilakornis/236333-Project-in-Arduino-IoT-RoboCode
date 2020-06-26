package com.example.a236333_hw3.RunEnvironment.Program.Command.Jump;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Program.Command.RCCommand;

public class RCJumpCommand extends RCCommand {

    private int         numberOfRepsToExecute;
    private int         jumpId;
    private int         nextNoJumpIndex;

    private int         internalLoopCounter;
    private int         actualNextIndex;

    public RCJumpCommand() {
        setNextNoJumpIndex(NOT_DEF);
        setNumberOfRepsToExecute(NOT_DEF);
        internalLoopCounter = 0;
        actualNextIndex = NOT_DEF;
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

    public int getNextNoJumpIndex() {
        return nextNoJumpIndex;
    }

    public void setNextNoJumpIndex(int nextNoJumpIndex) {
        this.nextNoJumpIndex = nextNoJumpIndex;
    }

    @NonNull
    @Override
    public String toString() {
        return "======== JUMP COMMAND " + "\n" +
                super.toString() +
                "nextIndex = " + (nextNoJumpIndex == NOT_DEF ? "not defined" : nextNoJumpIndex) + "\n" +
                "numberOfReps = " + getNumberOfRepsToExecute() + "\n" +
                "jumpId" + jumpId + "\n";
    }

    @Override
    public void execute(ArduinoConnector connector) {
        internalLoopCounter++;

        if (internalLoopCounter > getNumberOfRepsToExecute() && getNumberOfRepsToExecute() != NOT_DEF) {
            actualNextIndex = getNextNoJumpIndex();
            internalLoopCounter = 0;
        } else {
            actualNextIndex = getJumpId();
        }
    }


    public int getNextIndex() {
        return actualNextIndex;
    }
}
