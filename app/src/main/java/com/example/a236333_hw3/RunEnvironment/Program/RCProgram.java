package com.example.a236333_hw3.RunEnvironment.Program;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.RunEnvironment.Program.Command.RCCommand;

import java.util.ArrayList;

public class RCProgram {
    private ArrayList<RCCommand> commands;
    private int jump_1_index;
    private int jump_2_index;
    private int jump_3_index;

    public ArrayList<RCCommand> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<RCCommand> commands) {
        this.commands = commands;
    }

    public int getJump_1_index() {
        return jump_1_index;
    }

    public void setJump_1_index(int jump_1_index) {
        this.jump_1_index = jump_1_index;
    }

    public int getJump_2_index() {
        return jump_2_index;
    }

    public void setJump_2_index(int jump_2_index) {
        this.jump_2_index = jump_2_index;
    }

    public int getJump_3_index() {
        return jump_3_index;
    }

    public void setJump_3_index(int jump_3_index) {
        this.jump_3_index = jump_3_index;
    }

    @NonNull
    @Override
    public String toString() {
        return "======== RC PROGRAM " + "\n" +
                commands.toString() +
                "getJump_1_index = " + getJump_1_index() + "\n" +
                "getJump_2_index = " + getJump_2_index() + "\n" +
                "getJump_3_index = " + getJump_3_index() + "\n" ;
    }

}
