package com.example.a236333_hw3.RunEnvironment.Executor;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Log.RCProgramLog;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;
import com.example.a236333_hw3.RunEnvironment.Program.Command.RCCommand;
import com.example.a236333_hw3.RunEnvironment.Program.RCProgram;

public class RCProgramExecutor {

    public static final int NO_STEPS_LIMIT = -1;

    private RCProgramLog    logger;
    private RCProgramStatus status;

    public RCProgramExecutor() {
    }

    public int runProgram(RCProgram program, ArduinoConnector connector, int stepsLimit) {
        // Reset variables
        logger = new RCProgramLog();
        status = new RCProgramStatus();

        int nextCmdIndex = 0;
        int steps = 0;

        do {
            // get next command
            RCCommand cmd = program.getCommands().get(nextCmdIndex);

            // execute command
            cmd.execute(logger, status, connector);

            // get next command index
            nextCmdIndex = cmd.getNextNoJumpIndex();
            steps++;
        } while (nextCmdIndex != RCCommand.NOT_DEF &&
                 ((stepsLimit == NO_STEPS_LIMIT) || (steps <= stepsLimit)));

        // TODO : here we will return an object that will retrieve a tuple object { logger , status , steps }
        return steps;
    }
}
