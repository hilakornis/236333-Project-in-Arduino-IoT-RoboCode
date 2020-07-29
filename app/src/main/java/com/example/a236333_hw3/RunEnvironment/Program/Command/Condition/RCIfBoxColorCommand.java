package com.example.a236333_hw3.RunEnvironment.Program.Command.Condition;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Program.Color;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;

import androidx.annotation.NonNull;

public class RCIfBoxColorCommand extends RCIfCommand {
    private Color color;      // can only be VAR_COLOR_...
    private int actual_next_jump_index;

    // TODO : update boxes colors constants
    private final Color boxesColors[] = { Color.NON_COLOR /*no box with id 0*/, Color.RED };


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public RCIfBoxColorCommand() {
        actual_next_jump_index = NOT_DEF;
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
                 color == Color.BLACK ? "black" : "unknown") + "\n";
    }

    @Override
    public void execute(ArduinoConnector connector) {
        actual_next_jump_index =
                (RCProgramStatus.getInstance().getBoxId() != RCProgramStatus.NO_BOX &&
                    boxesColors[RCProgramStatus.getInstance().getBoxId()] == getColor() ? getNextTrue() : getNextFalse());
    }

    @Override
    public int getNextActualIndex() {
        return actual_next_jump_index;
    }
}
