package com.example.a236333_hw3.Compiler;

import java.util.ArrayList;

public class EnumsToCommandConverter {

    public RCProgram getCommandArray(ArrayList<QREnums> enums) {

        ArrayList<RCCommand> commands = new ArrayList<>();
        int jump_1_index = -1, jump_2_index = -1, jump_3_index = -1;

        boolean usedJump[] = {false, false, false};

        int spinalsCount                = 1;
        ArrayList<RCCommand> prevLine   = new ArrayList<RCCommand>();
        ArrayList<RCCommand> currLine   = new ArrayList<RCCommand>();

        int currentLineIndex = 0;

        while (currentLineIndex < 8) {
            QREnums enumsRow[] = {   enums.get(currentLineIndex*6),
                                     enums.get(currentLineIndex*6+1),
                                     enums.get(currentLineIndex*6+2),
                                     enums.get(currentLineIndex*6+3),
                                     enums.get(currentLineIndex*6+4),
                                     enums.get(currentLineIndex*6+5)    };

            // encode curr line
            for (int startIndex = 0; startIndex < 6; startIndex++) {
                if (enumsRow[startIndex] == QREnums.NaN)
                    continue;
                else if (!isSpinalCard(enumsRow[startIndex])) {
                    // ERROR
                    // TODO : throw new exception - command canot start with non-spinal card
                } else {
                    RCCommand cmd = readCommand(startIndex, enumsRow);

                    // Set the command spinal index
                    cmd.setSpinalIndex(startIndex);
                    startIndex += cmd.getLength()-1;

                    // Add to commands queue, and save index
                    commands.add(cmd);
                    cmd.setIndex(commands.size()-1);

                    // add to curr line collection
                    currLine.add(cmd);
                }
            }

            // check only for first line
            if (currentLineIndex == 0) {
                if (currLine.size() != 1) {
                    // ERROR
                    // TODO : throw new exception - to many commands in first line
                }
            } else {
                // update indexes in prev line
                for (RCCommand prevCmd : prevLine) {
                    if (prevCmd instanceof RCExcuteCommand) {
                        for (RCCommand cmd : currLine) {
                            if (cmd.getSpinalIndex() == prevCmd.getSpinalIndex()) {
                                ((RCExcuteCommand) prevCmd).setNextIndex(cmd.getIndex());
                                cmd.setReachable(true);
                                break;
                            }
                        }
                    } else if (prevCmd instanceof RCJumpCommand) {
                        for (RCCommand cmd : currLine) {
                            if (cmd.getSpinalIndex() == prevCmd.getSpinalIndex()) {
                                ((RCExcuteCommand) prevCmd).setNextIndex(cmd.getIndex());
                                cmd.setReachable(true);
                                break;
                            }
                        }
                    } else if (prevCmd instanceof RCIfCommand) {
                        for (RCCommand cmd : currLine) {
                            if (cmd.getSpinalIndex() == prevCmd.getSpinalIndex()) {
                                ((RCExcuteCommand) prevCmd).setNextIndex(cmd.getIndex());
                                cmd.setReachable(true);
                            }
                        }

                    }
                }

                // check reachable
            }

            // prev line <= curr line
            prevLine   = currLine;
            currLine   = new ArrayList<RCCommand>();

            currentLineIndex++;
        }

        RCProgram prog = new RCProgram();
        prog.setCommands(commands);
        prog.setJump_1_index(jump_1_index);
        prog.setJump_1_index(jump_2_index);
        prog.setJump_1_index(jump_3_index);
        return prog;
    }

    private RCCommand readCommand(int startIndex, QREnums[] enumsRow) {


        return null;
    }

    private QREnums spinalCards[] = {
                                        QREnums.CMD_TURN_LEFT,
                                        QREnums.CMD_TURN_RIGHT,
                                        QREnums.CMD_TURN_AROUND,
                                        QREnums.CMD_GO_FORWARD,
                                        QREnums.CMD_GO_BACKWARD,
                                        QREnums.CMD_FORKLIFT_UP,
                                        QREnums.CMD_FORKLIFT_DOWN,
                                        QREnums.JMP_FROM_1,
                                        QREnums.JMP_TO_1,
                                        QREnums.JMP_FROM_2,
                                        QREnums.JMP_TO_2,
                                        QREnums.JMP_FROM_3, 
                                        QREnums.JMP_TO_3,
                                        QREnums.CONDITION
                                    };

    private boolean isSpinalCard(QREnums value) {
        for (int i=0; i<spinalCards.length; i++) if (spinalCards[i] == value) return true;
        return false;
    }
}
