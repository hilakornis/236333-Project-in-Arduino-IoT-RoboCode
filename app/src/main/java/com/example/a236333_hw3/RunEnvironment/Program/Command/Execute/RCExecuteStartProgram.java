package com.example.a236333_hw3.RunEnvironment.Program.Command.Execute;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;

public class RCExecuteStartProgram extends RCExecuteCommand {

    @Override
    public void execute(ArduinoConnector connector) throws InterruptedException {
        // ask for status
        updateStatus(connector);
    }

    @NonNull
    @Override
    public String toString() {
        return "======== START PROGRAM COMMAND " + "\n" +
               super.toString() +
               "start command, making sure that the program status is updated!\n";
    }
}
