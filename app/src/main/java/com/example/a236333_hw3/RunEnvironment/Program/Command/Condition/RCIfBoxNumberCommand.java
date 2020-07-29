package com.example.a236333_hw3.RunEnvironment.Program.Command.Condition;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;

public class RCIfBoxNumberCommand extends RCIfCommand {
    private int boxId;
    private int actual_next_jump_index;

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    @NonNull
    @Override
    public String toString() {
        return "======== BOX NUMBER CONDITION " + "\n" +
                super.toString() +
                "box id = " + getBoxId() + "\n";
    }

    @Override
    public void execute(ArduinoConnector connector) {
        actual_next_jump_index = (RCProgramStatus.getInstance().getBoxId() != RCProgramStatus.NO_BOX ? getNextTrue() : getNextFalse());
    }

    @Override
    public int getNextActualIndex() {
        return actual_next_jump_index;
    }

}
