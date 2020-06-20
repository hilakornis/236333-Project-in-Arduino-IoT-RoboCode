package com.example.a236333_hw3.Compiler;

public class RCIfCommand extends RCCommand {

    // FOR EXAMPLE condition=tile + value=red -> is the current tile were on red?
    private QREnums condition;
    private int     value;
    private RCCommand nextTrue;
    private RCCommand nextFalse;

    public RCCommand getNextTrue() {
        return nextTrue;
    }

    public void setNextTrue(RCCommand nextTrue) {
        this.nextTrue = nextTrue;
    }

    public RCCommand getNextFalse() {
        return nextFalse;
    }

    public void setNextFalse(RCCommand nextFalse) {
        this.nextFalse = nextFalse;
    }

    public QREnums getCondition() {
        return condition;
    }

    public void setCondition(QREnums condition) {
        this.condition = condition;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
