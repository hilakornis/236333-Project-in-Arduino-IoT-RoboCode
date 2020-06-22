package com.example.a236333_hw3.Compiler.Command.Condition;
import com.example.a236333_hw3.Compiler.QREnums;

public class RCIfBoxColorCommand extends RCIfCommand {
    private QREnums color;      // can only be VAR_COLOR_...

    public QREnums getColor() {
        return color;
    }

    public void setColor(QREnums color) {
        this.color = color;
    }
}
