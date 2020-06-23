package com.example.a236333_hw3.RunEnvironment.Program.Command;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Log.RCProgramLog;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;

public abstract class RCCommand {

    public static final int NOT_DEF = -1;

    private int commandIndex;
    private int spinalIndex;
    private int lineIndex;
    private int length;
    private boolean reachable;

    public RCCommand() {
        setCommandIndex(NOT_DEF);
        setSpinalIndex(NOT_DEF);
        setLineIndex(NOT_DEF);
        setLength(0);
        setReachable(false);
    }

    public abstract int getNextNoJumpIndex();

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSpinalIndex() {
        return spinalIndex;
    }

    public void setSpinalIndex(int spinalIndex) {
        this.spinalIndex = spinalIndex;
    }

    public int getCommandIndex() {
        return commandIndex;
    }

    public void setCommandIndex(int commandIndex) {
        this.commandIndex = commandIndex;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    @NonNull
    @Override
    public String toString() {
        return "commandIndex = " + getCommandIndex() + "\n" +
                "spinalIndex = " + getSpinalIndex()  + "\n" +
                "lineIndex = " + getLineIndex()  + "\n" +
                "length = " + getLength()  + "\n" +
                "reachable = " + (isReachable() ? "yes" : "no") + "\n";
    }

    public abstract void execute(RCProgramLog logger, RCProgramStatus status, ArduinoConnector connector);
}
