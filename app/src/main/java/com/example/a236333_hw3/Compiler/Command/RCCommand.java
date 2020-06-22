package com.example.a236333_hw3.Compiler.Command;

public class RCCommand {

    private int index;
    private int spinalIndex;
    private int length;
    private boolean reachable;

    public RCCommand() {
        setReachable(false);
    }

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
