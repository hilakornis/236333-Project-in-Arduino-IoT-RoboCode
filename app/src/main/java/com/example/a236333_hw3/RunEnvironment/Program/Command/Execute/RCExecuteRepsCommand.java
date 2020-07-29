package com.example.a236333_hw3.RunEnvironment.Program.Command.Execute;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Log.Item.RCProgramLogItemMovement;
import com.example.a236333_hw3.RunEnvironment.Log.Item.RCProgramLogItemMovementType;
import com.example.a236333_hw3.RunEnvironment.Log.RCProgramLog;

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

    protected void executeWithReps(ArduinoConnector connector, String cmd, RCProgramLogItemMovementType type) throws InterruptedException {
        // execute with or without reps
        int repsCount =
                getNumberOfRepsToExcute() == NOT_DEF ?
                        1 : getNumberOfRepsToExcute();

        for (int i=0; i< repsCount; i++) {

            // Do the command
            connector.trySendData(cmd);

            // log it
            RCProgramLog.getInstance().log(new RCProgramLogItemMovement(type));

            // ask for status and log it
            updateStatus(connector);
            logStatus();
        }
    }
}
