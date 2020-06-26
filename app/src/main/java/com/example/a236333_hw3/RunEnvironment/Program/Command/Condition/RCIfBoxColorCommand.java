package com.example.a236333_hw3.RunEnvironment.Program.Command.Condition;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Program.Color;

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
                (color == Color.RED ? "red" :
                 color == Color.BLUE ? "blue" :
                 color == Color.GREEN ? "green" :
                 color == Color.YELLOW ? "yellow" :
                 color == Color.WHITE ? "white" :
                 /*color = QREnums.VAR_COLOR_BLACK?*/   "black" ) + "\n";
    }

    @Override
    public void execute(ArduinoConnector connector) {
        // TODO : implement
    }

    @Override
    public int getNextNoJumpIndex() {
        // TODO : Complete
        return -1;
    }
}
