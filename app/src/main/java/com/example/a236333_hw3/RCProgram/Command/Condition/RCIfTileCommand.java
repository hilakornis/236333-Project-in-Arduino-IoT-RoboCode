package com.example.a236333_hw3.RCProgram.Command.Condition;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.RCProgram.Color;

public class RCIfTileCommand extends RCIfCommand {
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
        return "======== TILE CONDITION " + "\n" +
                super.toString() +
                "color = " +
                (color == Color.COLOR_RED      ? "red" :
                color == Color.COLOR_BLUE      ? "blue" :
                color == Color.COLOR_GREEN     ? "green" :
                color == Color.COLOR_YELLOW    ? "yellow" :
                color == Color.COLOR_WHITE     ? "white" :
                /*color = Color.COLOR_BLACK?*/   "black" ) + "\n";
    }
}
