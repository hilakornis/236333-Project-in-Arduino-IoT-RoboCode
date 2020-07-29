package com.example.a236333_hw3.RunEnvironment.Program.Command.Execute;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;

public class RCExecuteStartProgram extends RCExecuteCommand {

    @Override
    public void execute(ArduinoConnector connector) throws InterruptedException {
        // ask for status
        updateStatus(connector);

        if (RCProgramStatus.getInstance().isForkLiftUp()) {
            connector.trySendData(CMD_FORK_DOWN);
            updateStatus(connector);
        }

        logStatus();
    }

    @NonNull
    @Override
    public String toString() {
        return "======== START PROGRAM COMMAND " + "\n" +
               super.toString() +
               "start command, making sure that the program status is updated!\n";
    }
}
