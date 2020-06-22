package com.example.a236333_hw3.Compiler.Command;

import com.example.a236333_hw3.Compiler.QREnums;

public class RCExcuteCommand extends RCCommand {

    private QREnums cmd;
    private int         numberOfRepsToExcute;
    private int         nextIndex;

    public RCExcuteCommand() {
        setNextIndex(-1);
        setNumberOfRepsToExcute(1);
    }

    public QREnums getCmd() {
        return cmd;
    }

    public void setCmd(QREnums cmd) {
        this.cmd = cmd;
    }

    public int getNumberOfRepsToExcute() {
        return numberOfRepsToExcute;
    }

    public void setNumberOfRepsToExcute(int numberOfRepsToExcute) {
        this.numberOfRepsToExcute = numberOfRepsToExcute;
    }

    public int getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(int nextIndex) {
        this.nextIndex = nextIndex;
    }
}
