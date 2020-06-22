package com.example.a236333_hw3.Compiler;

import com.example.a236333_hw3.Compiler.Command.RCCommand;
import com.example.a236333_hw3.Compiler.Command.RCExcuteCommand;
import com.example.a236333_hw3.Compiler.Command.RCIfCommand;
import com.example.a236333_hw3.Compiler.Command.RCJumpCommand;

import java.util.ArrayList;



public class EnumsToCommandConverter {

    private final int ROWS = 8, COLS = 6, NO_JUMP = -1;

    private int     JumpToIndex[]   = {NO_JUMP, NO_JUMP, NO_JUMP};             // the index of the command to jump to
    private int     JumpFromIndex[] = {NO_JUMP, NO_JUMP, NO_JUMP};           // the index of the command to jump to
    ArrayList<RCCommand> commands;

    public RCProgram getCommandArray(ArrayList<QREnums> enums) {
        // helps creating the current RCProgram
        commands = new ArrayList<>();

        // helps tracing jump to & from
        JumpToIndex[0]      = NO_JUMP;
        JumpToIndex[1]      = NO_JUMP;
        JumpToIndex[2]      = NO_JUMP;
        JumpFromIndex[0]    = NO_JUMP;
        JumpFromIndex[1]    = NO_JUMP;
        JumpFromIndex[2]    = NO_JUMP;

        ArrayList<RCCommand> prevLine   = new ArrayList<RCCommand>();
        ArrayList<RCCommand> currLine   = new ArrayList<RCCommand>();
        int currentLineIndex = 0;

        while (currentLineIndex < ROWS) {
            QREnums enumsRow[] = {   enums.get(currentLineIndex*COLS),   enums.get(currentLineIndex*COLS+1),
                                     enums.get(currentLineIndex*COLS+2), enums.get(currentLineIndex*COLS+3),
                                     enums.get(currentLineIndex*COLS+4), enums.get(currentLineIndex*COLS+5) };

            // encode curr line
            for (int startIndex = 0; startIndex < COLS; startIndex++) {
                if (enumsRow[startIndex] == QREnums.NaN)
                    continue;
                else if (!contains(spinalCards, enumsRow[startIndex])) {
                    // ERROR
                    // TODO : throw new exception - command canot start with non-spinal card
                } else {
                    RCCommand cmd = readCommand(startIndex, enumsRow);
                    startIndex += cmd.getLength()-1;

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
                                ((RCJumpCommand) prevCmd).setNextIndex(cmd.getIndex());
                                cmd.setReachable(true);
                                break;
                            }
                        }
                    } else if (prevCmd instanceof RCIfCommand) {
                        for (RCCommand cmd : currLine) {
                            if (cmd.getSpinalIndex() == prevCmd.getSpinalIndex() + 1) {
                                ((RCIfCommand) prevCmd).setNextTrue(cmd.getIndex());
                                cmd.setReachable(true);
                            } else if (cmd.getSpinalIndex() + cmd.getLength() == prevCmd.getSpinalIndex()) {
                                ((RCIfCommand) prevCmd).setNextFalse(cmd.getIndex());
                                cmd.setReachable(true);
                            }
                        }
                        if (((RCIfCommand) prevCmd).getNextFalse() == -1 ||
                            ((RCIfCommand) prevCmd).getNextTrue()  == -1)  {
                            // TODO : throw new exception - unhandled branches
                        }
                    }
                }

                // check reachable
                for (RCCommand cmd : currLine) {
                    if (!cmd.isReachable()) {
                        // TODO : throw new exception - unreachable command
                    }
                }
            }

            // prev line <= curr line
            prevLine   = currLine;
            currLine   = new ArrayList<RCCommand>();

            currentLineIndex++;
        }

        // TODO  - make sure that last line does not contain an if command

        // TODO - add check to jump_from & jump_to vectors equality and update indexes in jump_from

        RCProgram prog = new RCProgram();
        prog.setCommands(commands);
        return prog;
    }

    private RCCommand readCommand(int startIndex, QREnums[] enumsRow) {
        int commandIndex = commands.size();
        int currIndex = startIndex;
        int length = 0;
        RCCommand cmd;
        boolean startsWithJumpTo = false;

        while (contains(JumpToCards, enumsRow[currIndex]) && currIndex < COLS) {
            startsWithJumpTo = true;
            int i = enumsRow[currIndex] == QREnums.JMP_TO_1 ? 0 :
                    enumsRow[currIndex] == QREnums.JMP_TO_2 ? 1 :
                    /*enumsRow[currIndex] = QREnums.JMP_TO_3*/2;
            JumpToIndex[i] = commandIndex;
            currIndex++;
            length++;
        }

        if (currIndex == COLS || !contains(spinalCards, enumsRow[currIndex])) {
            // TODO - throw exception - jumping to destination without spinal card
        }

        // notice that in this point, length will contain the amount of JMP_TO cards in the
        // beginning of the current command.
        if (enumsRow[currIndex] == QREnums.CONDITION) {
            cmd = readCondCommand(enumsRow, currIndex, length);
        } else if (contains(JumpFromCards, enumsRow[currIndex])) {
            int i = enumsRow[currIndex] == QREnums.JMP_FROM_1 ? 0 :
                    enumsRow[currIndex] == QREnums.JMP_FROM_2 ? 1 :
                    /*enumsRow[currIndex] = QREnums.JMP_FORM_3*/2;
            JumpFromIndex[i] = currIndex;
            cmd = readJumpCommand(enumsRow, currIndex, length);
        } else {
            cmd = readExcuteCommand(enumsRow, currIndex, length);
        }

        // Add to commands queue, and save command index & spinal index
        commands.add(cmd);
        cmd.setIndex(commands.size()-1);
        cmd.setSpinalIndex(startIndex);

        // if command starts with jump_to -> set reachable to true (will cause a known issue bug)
        if (startsWithJumpTo) cmd.setReachable(true);

        return cmd;
    }

    private RCIfCommand readCondCommand(QREnums[] enumsRow, int currIndex, int currLength) {
        return null;
    }

    private RCJumpCommand readJumpCommand(QREnums[] enumsRow, int currIndex, int currLength) {
        return null;
    }

    private RCExcuteCommand readExcuteCommand(QREnums[] enumsRow, int currIndex, int currLength) {
        return null;
    }

    private QREnums JumpToCards[] = {
            QREnums.JMP_TO_1,
            QREnums.JMP_TO_2,
            QREnums.JMP_TO_3
    };
    private QREnums JumpFromCards[] = {
            QREnums.JMP_FROM_1,
            QREnums.JMP_FROM_2,
            QREnums.JMP_FROM_3
    };

    private QREnums spinalCards[] = {
            QREnums.CMD_TURN_LEFT,
            QREnums.CMD_TURN_RIGHT,
            QREnums.CMD_TURN_AROUND,
            QREnums.CMD_GO_FORWARD,
            QREnums.CMD_GO_BACKWARD,
            QREnums.CMD_FORKLIFT_UP,
            QREnums.CMD_FORKLIFT_DOWN,
            QREnums.CMD_STOP,
            QREnums.JMP_FROM_1,
            QREnums.JMP_TO_1,
            QREnums.JMP_FROM_2,
            QREnums.JMP_TO_2,
            QREnums.JMP_FROM_3,
            QREnums.JMP_TO_3,
            QREnums.CONDITION
    };

    private boolean contains( QREnums arr[], QREnums value) {
        for (int i=0; i<arr.length; i++) if (arr[i] == value) return true;
        return false;
    }
}
