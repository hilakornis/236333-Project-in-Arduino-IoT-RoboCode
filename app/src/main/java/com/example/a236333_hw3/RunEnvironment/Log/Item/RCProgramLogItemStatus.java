package com.example.a236333_hw3.RunEnvironment.Log.Item;
import com.example.a236333_hw3.RunEnvironment.Program.Color;

public class RCProgramLogItemStatus extends RCProgramLogItem {
    public static final int NO_BOX = 0;

    private int     boxId;
    private boolean forkLiftUp;
    private Color clr;

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public boolean isForkLiftUp() {
        return forkLiftUp;
    }

    public void setForkLiftUp(boolean forkLiftUp) {
        this.forkLiftUp = forkLiftUp;
    }

    public Color getClr() {
        return clr;
    }

    public void setClr(Color clr) {
        this.clr = clr;
    }

    public RCProgramLogItemStatus(int boxId, boolean forkLiftUp, Color clr) {
        this.boxId = boxId;
        this.forkLiftUp = forkLiftUp;
        this.clr = clr;
    }

    public RCProgramLogItemStatus() {
        this.boxId = 0;
        this.forkLiftUp = false;
        this.clr = Color.BLACK;
    }
}
