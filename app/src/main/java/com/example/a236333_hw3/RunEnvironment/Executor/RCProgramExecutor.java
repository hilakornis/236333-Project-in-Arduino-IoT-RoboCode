package com.example.a236333_hw3.RunEnvironment.Executor;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Log.RCProgramLog;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;
import com.example.a236333_hw3.RunEnvironment.Program.Command.RCCommand;
import com.example.a236333_hw3.RunEnvironment.Program.RCProgram;

public class RCProgramExecutor {

    public static final int NO_STEPS_LIMIT = -1;

    private static RCProgramExecutor _inst;

    private RCProgram program;
    private ArduinoConnector connector;
    private int stepsLimit;
    private Runnable successHanlder, errorHanlder;

    public static RCProgramExecutor getInstance() {
        if (_inst == null) _inst = new RCProgramExecutor();
        return _inst;
    }

    private RCProgramExecutor() {
    }


    public void runProgram(RCProgram _program, ArduinoConnector _connector, int _stepsLimit, Runnable _successHanlder, final Runnable _errorHanlder) {
        program=_program;
        connector=_connector;
        stepsLimit=_stepsLimit;
        successHanlder=_successHanlder;
        errorHanlder=_errorHanlder;
        final Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doTheProgramRunning();
                    if (successHanlder!=null) successHanlder.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    if (errorHanlder!=null) errorHanlder.run();
                }
            }
        });
        th.start();
    }

    private void doTheProgramRunning() throws InterruptedException {
        // Reset variables
        RCProgramStatus.getInstance().init();
        RCProgramLog.getInstance().init();

        int nextCmdIndex = 0;
        int steps = 0;

        do {
            // get next command
            RCCommand cmd = program.getCommands().get(nextCmdIndex);

            // execute command
            cmd.execute(connector);

            // get next command index
            nextCmdIndex = cmd.getNextActualIndex();
            steps++;
        } while (nextCmdIndex != RCCommand.NOT_DEF &&
                ((stepsLimit == NO_STEPS_LIMIT) || (steps <= stepsLimit)));

        // TODO : here we will return an object that will retrieve a tuple object { logger , status , steps }
        // TODO : in case we passed STEPS_LIMIT we need to return an error using _errorHanlder!!!
    }
}
