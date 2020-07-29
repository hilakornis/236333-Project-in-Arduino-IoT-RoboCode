package com.example.a236333_hw3.RunEnvironment.Program.Command.Execute;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Log.Item.RCProgramLogItemMovement;
import com.example.a236333_hw3.RunEnvironment.Log.Item.RCProgramLogItemMovementType;
import com.example.a236333_hw3.RunEnvironment.Log.RCProgramLog;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;

public class RCExecuteForkUpCommand extends RCExecuteCommand {
    @NonNull
    @Override
    public String toString() {
        return "======== FORK UP COMMAND " + "\n" +
                super.toString();
    }

    @Override
    public void execute(ArduinoConnector connector) throws InterruptedException {
        if (!RCProgramStatus.getInstance().isForkLiftUp()) {
            connector.trySendData(CMD_FORK_UP);
            updateStatus(connector);
            RCProgramLog.getInstance().
                    log(new RCProgramLogItemMovement(RCProgramLogItemMovementType.FORKLIFT_UP));
            updateStatus(connector);
            logStatus();
        }
    }
}

