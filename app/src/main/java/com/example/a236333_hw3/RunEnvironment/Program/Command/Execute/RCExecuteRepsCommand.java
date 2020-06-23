package com.example.a236333_hw3.RunEnvironment.Program.Command.Execute;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Log.RCProgramLog;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;

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

    @NonNull
    @Override
    public String toString() {
        return super.toString() +
                "NumberOfReps = " + (getNumberOfRepsToExcute() == NOT_DEF ? "not defined" : getNumberOfRepsToExcute()) + "\n";
    }

    @Override
    public void execute(RCProgramLog logger, RCProgramStatus status, ArduinoConnector connector) {
        // TODO : implement
    }
}
