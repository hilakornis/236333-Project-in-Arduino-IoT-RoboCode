package com.example.a236333_hw3.RunEnvironment.Program.Command.Execute;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;

public class RCExecuteRepsTurnUTurnCommand extends RCExecuteRepsCommand {
    @NonNull
    @Override
    public String toString() {
        return "======== U TURN COMMAND " + "\n" + super.toString();
    }

    @Override
    public void execute(ArduinoConnector connector) throws InterruptedException {
        executeWithReps(connector, CMD_U_TURN);
    }
}
