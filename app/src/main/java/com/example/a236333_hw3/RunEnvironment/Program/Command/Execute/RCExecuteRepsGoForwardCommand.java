package com.example.a236333_hw3.RunEnvironment.Program.Command.Execute;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Log.Item.RCProgramLogItemMovementType;

public class RCExecuteRepsGoForwardCommand extends RCExecuteRepsCommand {
    @NonNull
    @Override
    public String toString() {
        return "======== GO FORWARDS COMMAND " + "\n" + super.toString();
    }

    @Override
    public void execute(ArduinoConnector connector) throws InterruptedException {
        executeWithReps(connector, CMD_FORWARDS, RCProgramLogItemMovementType.GO_FORWARD);
    }
}
