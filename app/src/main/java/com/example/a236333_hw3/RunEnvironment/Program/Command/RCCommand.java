package com.example.a236333_hw3.RunEnvironment.Program.Command;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Log.Item.RCProgramLogItemStatus;
import com.example.a236333_hw3.RunEnvironment.Log.RCProgramLog;
import com.example.a236333_hw3.RunEnvironment.Program.Color;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;

public abstract class RCCommand {

    public static final int NOT_DEF = -1;
    protected static final String CMD_FORWARDS      = "1";
    protected static final String CMD_BACKWARDS     = "2";
    protected static final String CMD_LEFT          = "3";
    protected static final String CMD_RIGHT         = "4";
    protected static final String CMD_U_TURN        = "5";
    protected static final String CMD_FORK_UP       = "6";
    protected static final String CMD_FORK_DOWN     = "7";
    protected static final String CMD_CHECK_EDGE    = "8";
    protected static final String CMD_CHECK_STATUS  = "9";

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

    public abstract int getNextActualIndex();

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
        // first, send a status request
        connector.trySendData(CMD_CHECK_STATUS);

        // wait until an answer comes back
        while (connector.getResults().size() == 0) Thread.sleep(5);

        // asset (connector.getResults().size() > 0)

        // take the status string
        String statusString = connector.getResults().pop();

        // get the data!
        int boxId = Integer.parseInt(statusString.split("\\|")[3]);
        boolean lifterUp = statusString.split("\\|")[4] == "1" ? true : false;
        Color color = getColor(
                Integer.parseInt(statusString.split("\\|")[0]),
                Integer.parseInt(statusString.split("\\|")[1]),
                Integer.parseInt(statusString.split("\\|")[2]));

        // update status!
        RCProgramStatus.getInstance().setBoxId(boxId);
        RCProgramStatus.getInstance().setForkLiftUp(lifterUp);
        RCProgramStatus.getInstance().setClr(color);

    }

    protected Color getColor(int r, int g, int b) {
        // TODO : white and yellow are very similar. can we do something about it?
        if      (r >= 10 && r <= 20 && g >= 10 && g < 15 && b >= 10 && b <= 20)         return Color.WHITE;
        else if (r >= 10 && r <= 20 && g >= 15 && g <= 20 && b >= 10 && b <= 20)        return Color.YELLOW;
        else if (r >= 20 && r <= 30 && g >= 15 && g <= 30 && b >= 20 && b <= 40)        return Color.BLUE;
        else if (r >= 20 && r <= 30 && g >= 70 && g <= 100 && b >= 15 && b <= 35)       return Color.RED;
        else if (r >= 30 && r <= 45 && g >= 15 && g <= 35 && b >= 30 && b <= 50)        return Color.GREEN;
        else if (r >= 135 && r <= 170 && g >= 130 && g <= 170 && b >= 130 && b <= 170)  return Color.BLACK;
        else                                                                            return Color.NON_COLOR;
    }

    protected void logStatus() {
        RCProgramLog.getInstance().log(new RCProgramLogItemStatus(
            RCProgramStatus.getInstance().getBoxId(),
            RCProgramStatus.getInstance().isForkLiftUp(),
            RCProgramStatus.getInstance().getClr()
        ));
    }
}
