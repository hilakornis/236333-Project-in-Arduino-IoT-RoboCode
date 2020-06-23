package com.example.a236333_hw3.RunEnvironment.Executor;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Log.RCProgramLog;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;
import com.example.a236333_hw3.RunEnvironment.Program.Command.RCCommand;
import com.example.a236333_hw3.RunEnvironment.Program.RCProgram;

public class RCProgramExecutor {

    private RCProgramLog    logger;
    private RCProgramStatus status;

    public RCProgramExecutor() {
    }

    public void runProgram(RCProgram program, ArduinoConnector connector) {
        // Reset variables
        logger = new RCProgramLog();
        status = new RCProgramStatus();

        RCCommand cmd = program.getCommands().get(0);
        int nextCmdIndex = -1;

        do {
            // execute command
            //cmd.execute(logger, status, connector);

            // get next command
            nextCmdIndex = cmd.getNextIndex();
        } while (nextCmdIndex != RCCommand.NOT_DEF);


    }

}
