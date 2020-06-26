package com.example.a236333_hw3.RunEnvironment.Program.Command.Execute;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;

public abstract class RCExecuteRepsCommand extends RCExecuteCommand {

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

    protected void executeWithReps(ArduinoConnector connector, String cmd) throws InterruptedException {
        // TODO : Write Reps loop
        connector.trySendData(cmd);
        updateStatus(connector);
    }
}
