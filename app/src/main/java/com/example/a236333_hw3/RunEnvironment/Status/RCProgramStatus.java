package com.example.a236333_hw3.RunEnvironment.Status;
import com.example.a236333_hw3.RunEnvironment.Program.Color;

public class RCProgramStatus {

    private static RCProgramStatus _inst;

    public static RCProgramStatus getInstance() {
        if (_inst == null) _inst = new RCProgramStatus();
        return _inst;
    }

    private RCProgramStatus() {
    }

    public static final int NO_BOX = 0;

    private int     boxId;
    private boolean forkLiftUp;
    private Color   clr;

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

    public void init() {
        setBoxId(NO_BOX);
        setForkLiftUp(false);
        setClr(Color.BLACK);
    }
}
