package com.example.a236333_hw3.Compiler.Command.Execute;

public class RCExecuteRepsCommand extends RCExecuteCommand {

    private int numberOfRepsToExcute;

    public RCExecuteRepsCommand() {
        setNumberOfRepsToExcute(NOT_DEF);
    }

    public int getNumberOfRepsToExcute() {
        return numberOfRepsToExcute;
    }

    public void setNumberOfRepsToExcute(int numberOfRepsToExcute) {
        this.numberOfRepsToExcute = numberOfRepsToExcute;
    }

}
