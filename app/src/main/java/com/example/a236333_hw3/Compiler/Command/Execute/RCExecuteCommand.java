package com.example.a236333_hw3.Compiler.Command.Execute;

import com.example.a236333_hw3.Compiler.Command.RCCommand;
import com.example.a236333_hw3.Compiler.QREnums;

public class RCExecuteCommand extends RCCommand {

    private int         nextIndex;

    public RCExecuteCommand() {
        setNextIndex(NOT_DEF);
    }

    public int getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(int nextIndex) {
        this.nextIndex = nextIndex;
    }
}
