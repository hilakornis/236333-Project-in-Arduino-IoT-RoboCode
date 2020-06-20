package com.example.a236333_hw3.Compiler;

public class RCIfCommand extends RCCommand {

    // FOR EXAMPLE condition=tile + value=red -> is the current tile were on red?
    private QREnums condition;
    private int     value;
    private int nextTrue;
    private int nextFalse;

    public RCIfCommand() {
        setNextFalse(-1);
        setNextTrue(-1);
    }

    public int getNextTrue() {
        return nextTrue;
    }

    public void setNextTrue(int nextTrue) {
        this.nextTrue = nextTrue;
    }

    public int getNextFalse() {
        return nextFalse;
    }

    public void setNextFalse(int nextFalse) {
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
