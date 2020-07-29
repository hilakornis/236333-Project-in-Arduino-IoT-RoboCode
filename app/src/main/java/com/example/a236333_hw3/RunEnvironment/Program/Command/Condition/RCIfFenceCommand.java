package com.example.a236333_hw3.RunEnvironment.Program.Command.Condition;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Program.Color;
import com.example.a236333_hw3.Tools.RoboCodeSettings;

public class RCIfFenceCommand extends RCIfCommand {

    private int actual_next_jump_index;

    public RCIfFenceCommand() {
        this.actual_next_jump_index = NOT_DEF;
    }

    @NonNull
    @Override
    public String toString() {
        return "======== FENCE CONDITION " + "\n" +
                super.toString();
    }

    @Override
    public void execute(ArduinoConnector connector) throws InterruptedException {
        // first, send a status request
        connector.trySendData(CMD_CHECK_EDGE);

        // wait until an answer comes back
        while (connector.getResults().size() == 0) Thread.sleep(5);

        // take the status string
        String statusString = connector.getResults().pop();

        // get the data!
        Color color = getColor(
                Integer.parseInt(statusString.split("\\|")[0]),
                Integer.parseInt(statusString.split("\\|")[1]),
                Integer.parseInt(statusString.split("\\|")[2]));

        actual_next_jump_index =
                (color == Color.NON_COLOR ||
                 RoboCodeSettings.getInstance().current.FenceColors.contains(color) ?
                    getNextTrue() : getNextFalse());
    }

    @Override
    public int getNextActualIndex() {
        return actual_next_jump_index;
    }
}
