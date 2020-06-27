package com.example.a236333_hw3.RunEnvironment.Program.Command;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Program.Color;
import com.example.a236333_hw3.RunEnvironment.Program.RCProgram;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;

public abstract class RCCommand {

    public static final int NOT_DEF = -1;
    protected static final String CMD_FORWARDS    = "1";
    protected static final String CMD_BACKWARDS   = "2";
    protected static final String CMD_LEFT        = "3";
    protected static final String CMD_RIGHT       = "4";
    protected static final String CMD_U_TURN      = "5";
    protected static final String CMD_FORK_UP     = "6";
    protected static final String CMD_FORK_DOWN   = "7";

    private int commandIndex;
    private int spinalIndex;
    private int lineIndex;
    private int length;
    private boolean reachable;

    public RCCommand() {
        setCommandIndex(NOT_DEF);
        setSpinalIndex(NOT_DEF);
        setLineIndex(NOT_DEF);
        setLength(0);
        setReachable(false);
    }

    public abstract int getNextNoJumpIndex();

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSpinalIndex() {
        return spinalIndex;
    }

    public void setSpinalIndex(int spinalIndex) {
        this.spinalIndex = spinalIndex;
    }

    public int getCommandIndex() {
        return commandIndex;
    }

    public void setCommandIndex(int commandIndex) {
        this.commandIndex = commandIndex;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    @NonNull
    @Override
    public String toString() {
        return "commandIndex = " + getCommandIndex() + "\n" +
                "spinalIndex = " + getSpinalIndex()  + "\n" +
                "lineIndex = " + getLineIndex()  + "\n" +
                "length = " + getLength()  + "\n" +
                "reachable = " + (isReachable() ? "yes" : "no") + "\n";
    }

    public abstract void execute(ArduinoConnector connector) throws InterruptedException;

    protected void updateStatus(ArduinoConnector connector) throws InterruptedException {
        // TODO : solve not receiving answer from robot issue (bug)
       /* while (connector.getResults().size() == 0) {
            Thread.sleep(5);
        }*/

        if (connector.getResults().size() > 0) {
            String statusString = connector.getResults().pop();

            RCProgramStatus.getInstance().
                    setBoxId(Integer.parseInt(statusString.split("|")[3]));

            RCProgramStatus.getInstance().
                    setForkLiftUp(statusString.split("|")[4] == "1" ? true : false);

            RCProgramStatus.getInstance().
                    setClr(convert(
                            Integer.parseInt(statusString.split("|")[0]),
                            Integer.parseInt(statusString.split("|")[1]),
                            Integer.parseInt(statusString.split("|")[2])));
        }
    }

    protected Color convert(int r, int g, int b) {
        // TODO : Set correct ranges
        return Color.BLUE;
    }
}
