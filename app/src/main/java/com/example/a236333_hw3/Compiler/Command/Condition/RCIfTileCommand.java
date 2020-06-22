package com.example.a236333_hw3.Compiler.Command.Condition;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.Compiler.QREnums;

public class RCIfTileCommand extends RCIfCommand {
    private QREnums color;      // can only be VAR_COLOR_...

    public QREnums getColor() {
        return color;
    }

    public void setColor(QREnums color) {
        this.color = color;
    }

    @NonNull
    @Override
    public String toString() {
        return "======== TILE CONDITION " + "\n" +
                super.toString() +
                "color = " +
                (color == QREnums.VAR_COLOR_RED      ? "red" :
                color == QREnums.VAR_COLOR_BLUE      ? "blue" :
                color == QREnums.VAR_COLOR_GREEN     ? "green" :
                color == QREnums.VAR_COLOR_YELLOW    ? "yellow" :
                color == QREnums.VAR_COLOR_WHITE     ? "white" :
                /*color = QREnums.VAR_COLOR_BLACK?*/   "black" ) + "\n";
    }
}
