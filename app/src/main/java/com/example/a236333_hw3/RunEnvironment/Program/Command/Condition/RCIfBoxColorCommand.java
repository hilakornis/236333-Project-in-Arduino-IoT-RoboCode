package com.example.a236333_hw3.RunEnvironment.Program.Command.Condition;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Log.RCProgramLog;
import com.example.a236333_hw3.RunEnvironment.Program.Color;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;

import androidx.annotation.NonNull;

public class RCIfBoxColorCommand extends RCIfCommand {
    private Color color;      // can only be VAR_COLOR_...

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @NonNull
    @Override
    public String toString() {
        return "======== BOX COLOR CONDITION " + "\n" +
                super.toString() +
                "color = " +
                (color == Color.COLOR_RED      ? "red" :
                 color == Color.COLOR_BLUE      ? "blue" :
                 color == Color.COLOR_GREEN     ? "green" :
                 color == Color.COLOR_YELLOW    ? "yellow" :
                 color == Color.COLOR_WHITE     ? "white" :
                 /*color = QREnums.VAR_COLOR_BLACK?*/   "black" ) + "\n";
    }

    @Override
    public void execute(RCProgramLog logger, RCProgramStatus status, ArduinoConnector connector) {
        // TODO : implement
    }

    @Override
    public int getNextNoJumpIndex() {
        // TODO : Complete
        return -1;
    }
}
