package com.example.a236333_hw3.RunEnvironment.Program.Command.Condition;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Program.Color;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;

public class RCIfTileCommand extends RCIfCommand {
    private Color color;      // can only be VAR_COLOR_...
    private int actual_next_jump_index;


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public RCIfTileCommand() {
        actual_next_jump_index = NOT_DEF;
    }

    @NonNull
    @Override
    public String toString() {
        return "======== TILE CONDITION " + "\n" +
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
        actual_next_jump_index = (RCProgramStatus.getInstance().getClr() == getColor() ? getNextTrue() : getNextFalse());
    }

    @Override
    public int getNextActualIndex() {
        return actual_next_jump_index;
    }
}
