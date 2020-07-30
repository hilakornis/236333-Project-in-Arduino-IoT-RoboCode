package com.example.a236333_hw3.RunEnvironment.Executor;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Log.RCProgramLog;
import com.example.a236333_hw3.RunEnvironment.PostRunLogChecker.RCPostRunLogChecker;
import com.example.a236333_hw3.RunEnvironment.PostRunLogChecker.RCPostRunLogCheckerException;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;
import com.example.a236333_hw3.RunEnvironment.Program.Command.RCCommand;
import com.example.a236333_hw3.RunEnvironment.Program.RCProgram;

import java.util.concurrent.Semaphore;

public class RCProgramExecutor {

    public static final int NO_STEPS_LIMIT = -1;

    private static RCProgramExecutor _inst;

    private RCProgram program;
    private ArduinoConnector connector;
    private int stepsLimit;
    private String errorMessage;
    private Semaphore semaphoreForEndOfExecution;

    public static RCProgramExecutor getInstance() {
        if (_inst == null) _inst = new RCProgramExecutor();
        return _inst;
    }

    private RCProgramExecutor() {
    }

    public Semaphore getSemaphoreForEndOfExecution() {
        return semaphoreForEndOfExecution;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


    public void runProgram(RCProgram _program, ArduinoConnector _connector, int _stepsLimit) {
        program=_program;
        connector=_connector;
        stepsLimit=_stepsLimit;
        errorMessage="";
        semaphoreForEndOfExecution = new Semaphore(0);
        final Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doTheProgramRunning();
                    errorMessage = "";
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    errorMessage = "Oops... something went wrong! try again";
                }catch (RCPostRunLogCheckerException e) {
                    e.printStackTrace();
                    errorMessage = e.getMessage();
                }
                semaphoreForEndOfExecution.release();
            }
        });
        th.start();
    }

    private void doTheProgramRunning() throws InterruptedException, RCPostRunLogCheckerException {
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

        RCPostRunLogChecker.getInstance().CheckRunLog(RCProgramLog.getInstance().getLogger(), steps);

        //todo: Add a user compeleted tasks list and Update the user's completed tasks accordingly.
    }
}
