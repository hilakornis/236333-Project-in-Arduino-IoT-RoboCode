package com.example.a236333_hw3.RunEnvironment.Program.Command.Execute;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Log.Item.RCProgramLogItemMovementType;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;

public class RCExecuteRepsGoBackwardCommand extends RCExecuteRepsCommand {
    @NonNull
    @Override
    public String toString() {
        return "======== GO BACKWARDS COMMAND " + "\n" + super.toString();
    }

    @Override
    public void execute(ArduinoConnector connector) throws InterruptedException {
        executeWithReps(connector, CMD_BACKWARDS, RCProgramLogItemMovementType.GO_BACKWARD);
    }
}
