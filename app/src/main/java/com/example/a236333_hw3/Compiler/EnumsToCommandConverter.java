package com.example.a236333_hw3.Compiler;

import com.example.a236333_hw3.Compiler.Command.Execute.RCExecuteForkDownCommand;
import com.example.a236333_hw3.Compiler.Command.Execute.RCExecuteForkUpCommand;
import com.example.a236333_hw3.Compiler.Command.Execute.RCExecuteRepsCommand;
import com.example.a236333_hw3.Compiler.Command.Execute.RCExecuteRepsGoBackwardCommand;
import com.example.a236333_hw3.Compiler.Command.Execute.RCExecuteRepsGoForwardCommand;
import com.example.a236333_hw3.Compiler.Command.Execute.RCExecuteRepsTurnLeftCommand;
import com.example.a236333_hw3.Compiler.Command.Execute.RCExecuteRepsTurnRightCommand;
import com.example.a236333_hw3.Compiler.Command.Execute.RCExecuteRepsTurnUTurnCommand;
import com.example.a236333_hw3.Compiler.Command.Execute.RCExecuteStopCommand;
import com.example.a236333_hw3.Compiler.Command.RCCommand;
import com.example.a236333_hw3.Compiler.Command.Execute.RCExecuteCommand;
import com.example.a236333_hw3.Compiler.Command.Condition.RCIfBoxColorCommand;
import com.example.a236333_hw3.Compiler.Command.Condition.RCIfBoxNumberCommand;
import com.example.a236333_hw3.Compiler.Command.Condition.RCIfCommand;
import com.example.a236333_hw3.Compiler.Command.Condition.RCIfFenceCommand;
import com.example.a236333_hw3.Compiler.Command.Condition.RCIfTileCommand;
import com.example.a236333_hw3.Compiler.Command.Jump.RCJumpCommand;

import java.util.ArrayList;

public class EnumsToCommandConverter {

    private final int ROWS = 8, COLS = 6, NO_JUMP = -1;

    private int     JumpToIndex[]   = {NO_JUMP, NO_JUMP, NO_JUMP}; // cmd index to jump to
    private int     JumpFromIndex[] = {NO_JUMP, NO_JUMP, NO_JUMP}; // cmd index to jump from
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
                    startIndex += cmd.getLength();  // TODO : think if should be getLength()-1

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
                    if (prevCmd instanceof RCExecuteCommand) {
                        for (RCCommand cmd : currLine) {
                            if (cmd.getSpinalIndex() == prevCmd.getSpinalIndex()) {
                                ((RCExecuteCommand) prevCmd).setNextIndex(cmd.getIndex());
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

        if (currIndex >= COLS || !contains(spinalCards, enumsRow[currIndex])) {
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
        currIndex++;
        currLength++;

        if (currIndex >= COLS) {
            // TODO : throw exception - condition not defined
        }

        switch (enumsRow[currIndex]) {
            case FENCE: {
                // handle fence condition
                if (currIndex+1 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex+1])) {
                    RCIfFenceCommand cmd = new RCIfFenceCommand();
                    cmd.setLength(currLength + 1);
                    return cmd;
                } else {
                    // TODO : throw Exception - illegal fence condition seq
                }
                break;
            }
            case TILE: {
                // handle tile color condition
                if ((currIndex+1 < COLS && contains(colorCards, enumsRow[currIndex+1])) &&
                    (currIndex+2 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex+2]))) {
                    RCIfTileCommand cmd = new RCIfTileCommand();
                    cmd.setLength(currLength + 2);
                    cmd.setColor(enumsRow[currIndex+1]);
                    return cmd;
                } else {
                    // TODO : throw Exception - illegal tile condition seq
                }
                break;
            }
            case BOX: {
                // handle box color condition -> COND card + BOX card + COLOR card
                if ((currIndex+1 < COLS && contains(colorCards, enumsRow[currIndex+1])) &&
                    (currIndex+2 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex+2]))) {
                    RCIfBoxColorCommand cmd = new RCIfBoxColorCommand();
                    cmd.setLength(currLength + 2);
                    cmd.setColor(enumsRow[currIndex+1]);
                    return cmd;
                }
                // handle box number condition -> COND card + BOX card + DIGIT card ( + optional, one more DIGIT card)
                else if (currIndex+1 < COLS && contains(oneToNineCards, enumsRow[currIndex+1])) {
                    RCIfBoxNumberCommand cmd = new RCIfBoxNumberCommand();
                    cmd.setLength(currLength + 2);
                    cmd.setBoxId(getNum(enumsRow[currIndex+1]));

                    // chacking the case of number with two digits
                    if (currIndex+2 < COLS && contains(zeroToNineCards, enumsRow[currIndex+2]) &&
                        (currIndex+3 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex+3])) ) {
                        cmd.setLength(currLength + 3);
                        cmd.setBoxId(10*cmd.getBoxId()+getNum(enumsRow[currIndex+2]));
                        return cmd;
                    }
                    else if (currIndex+2 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex+2])) {
                        return cmd;
                    }
                    else {
                            // TODO : throw Exception - illegal numeric box condition
                    }
                } else {
                    // TODO : throw Exception - illegal box condition
                }
                break;
            }
            default:
                // TODO : throw Exception - illegal condition
                break;
        }
        return null; // we are not suppose to reach this point!
    }

    private RCJumpCommand readJumpCommand(QREnums[] enumsRow, int currIndex, int currLength) {
        currIndex++;
        currLength++;

        RCJumpCommand cmd = new RCJumpCommand();

        // first option - only jump, no Reps defined
        if (currIndex+1 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex+1])) {
            cmd.setLength(currLength);
            // notice - the reps are defined in the default ctor as "NOT_DEF" (=-1)
            return cmd;
        }
        // second option - jump with Reps defined
        else if (currIndex+1 < COLS && contains(oneToNineCards, enumsRow[currIndex+1])) {
            cmd.setLength(currLength + 1);
            cmd.setNumberOfRepsToExecute(getNum(enumsRow[currIndex + 1]));

            // chacking the case of number with two digits
            if (currIndex + 2 < COLS && contains(zeroToNineCards, enumsRow[currIndex + 2]) &&
                    (currIndex + 3 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex + 3]))) {
                cmd.setLength(currLength + 2);
                cmd.setNumberOfRepsToExecute(10 * cmd.getNumberOfRepsToExecute() + getNum(enumsRow[currIndex + 2]));
                return cmd;
            } else if (currIndex + 2 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex + 2])) {
                return cmd;
            } else {
                // TODO : throw Exception -  illegal jump (with reps) command
            }
        }
        else {
            // TODO : throw Exception - illegal jump command
        }

        return null;
    }

    private RCExecuteCommand readExcuteCommand(QREnums[] enumsRow, int currIndex, int currLength) {
        switch (enumsRow[currIndex]) {
            // handel all the commands that can not have a parameter after the last spinal card
            case CMD_FORKLIFT_UP:
            case CMD_FORKLIFT_DOWN:
            case CMD_STOP: {
                // handle CMD_STOP
                if (currIndex+1 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex+1])) {
                    RCExecuteCommand cmd = enumsRow[currIndex] == QREnums.CMD_FORKLIFT_UP   ?   new RCExecuteForkUpCommand() :
                                           enumsRow[currIndex] == QREnums.CMD_FORKLIFT_DOWN ?   new RCExecuteForkDownCommand() :
                                           /*enumsRow[currIndex] == QREnums.CMD_STOP*/          new RCExecuteStopCommand();
                    cmd.setLength(currLength + 1);
                    return cmd;
                } else {
                    // TODO : throw Exception - illegal stop / ForkUp / ForkDown  execute seq
                }
                break;
            }
            case CMD_TURN_LEFT:
            case CMD_TURN_RIGHT:
            case CMD_TURN_AROUND:
            case CMD_GO_FORWARD:
            case CMD_GO_BACKWARD: {
                RCExecuteRepsCommand cmd =  enumsRow[currIndex] == QREnums.CMD_TURN_LEFT         ?   new RCExecuteRepsTurnLeftCommand()  :
                                            enumsRow[currIndex] == QREnums.CMD_TURN_RIGHT        ?   new RCExecuteRepsTurnRightCommand() :
                                            enumsRow[currIndex] == QREnums.CMD_TURN_AROUND       ?   new RCExecuteRepsTurnUTurnCommand() :
                                            enumsRow[currIndex] == QREnums.CMD_GO_FORWARD        ?   new RCExecuteRepsGoForwardCommand() :
                                            /*enumsRow[currIndex] == QREnums.CMD_GO_BACKWARD*/       new RCExecuteRepsGoBackwardCommand();

                // first option - only execute, no Reps defined
                if (currIndex+1 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex+1])) {
                    cmd.setLength(currLength + 1);
                    return cmd;
                }
                // second option - execute with Reps defined
                else if (currIndex+1 < COLS && contains(oneToNineCards, enumsRow[currIndex+1]) &&
                        (currIndex + 2 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex + 2])) ) {
                    cmd.setLength(currLength + 2);
                    cmd.setNumberOfRepsToExcute(getNum(enumsRow[currIndex + 1]));
                    return cmd;
                }
                else {
                    // TODO : throw Exception - illegal jump command
                }
                break;
            }
            default:
                // TODO : throw Exception - illegal execute seq
        }

        return null;
    }

    private boolean contains( QREnums arr[], QREnums value) {
        for (int i=0; i<arr.length; i++) if (arr[i] == value) return true;
        return false;
    }

    //region Enum groups definition

    private int getNum(QREnums qrEnums) {
        return qrEnums == QREnums.VAR_0 ? 0 :
                qrEnums == QREnums.VAR_1 ? 1 :
                        qrEnums == QREnums.VAR_2 ? 2 :
                                qrEnums == QREnums.VAR_3 ? 3 :
                                        qrEnums == QREnums.VAR_4 ? 4 :
                                                qrEnums == QREnums.VAR_5 ? 5 :
                                                        qrEnums == QREnums.VAR_6 ? 6 :
                                                                qrEnums == QREnums.VAR_7 ? 7 :
                                                                        qrEnums == QREnums.VAR_8 ? 8 :
                                                                                qrEnums == QREnums.VAR_9 ? 9 : 0;
    }

    private QREnums zeroToNineCards[] = {
            QREnums.VAR_0,
            QREnums.VAR_1,
            QREnums.VAR_2,
            QREnums.VAR_3,
            QREnums.VAR_4,
            QREnums.VAR_5,
            QREnums.VAR_6,
            QREnums.VAR_7,
            QREnums.VAR_8,
            QREnums.VAR_9
    };

    private QREnums oneToNineCards[] = {
            QREnums.VAR_1,
            QREnums.VAR_2,
            QREnums.VAR_3,
            QREnums.VAR_4,
            QREnums.VAR_5,
            QREnums.VAR_6,
            QREnums.VAR_7,
            QREnums.VAR_8,
            QREnums.VAR_9
    };

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

    private QREnums colorCards[] = {
            QREnums.VAR_COLOR_RED,  // CL_R
            QREnums.VAR_COLOR_BLUE, // CL_BL
            QREnums.VAR_COLOR_GREEN,  // CL_G
            QREnums.VAR_COLOR_YELLOW,  // CL_Y
            QREnums.VAR_COLOR_WHITE,  // CL_W
            QREnums.VAR_COLOR_BLACK, // CL_BK
    };

    private QREnums spinalOrNaNCards[] = {
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
            QREnums.CONDITION,
            QREnums.NaN
    };

    //endregion
}
