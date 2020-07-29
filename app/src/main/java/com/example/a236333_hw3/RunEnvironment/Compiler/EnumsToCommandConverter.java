package com.example.a236333_hw3.RunEnvironment.Compiler;

import com.example.a236333_hw3.RunEnvironment.Program.Color;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Execute.RCExecuteForkDownCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Execute.RCExecuteForkUpCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Execute.RCExecuteRepsCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Execute.RCExecuteRepsGoBackwardCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Execute.RCExecuteRepsGoForwardCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Execute.RCExecuteRepsTurnLeftCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Execute.RCExecuteRepsTurnRightCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Execute.RCExecuteRepsTurnUTurnCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Execute.RCExecuteStartProgram;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Execute.RCExecuteStopCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.RCCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Execute.RCExecuteCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Condition.RCIfBoxColorCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Condition.RCIfBoxNumberCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Condition.RCIfCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Condition.RCIfFenceCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Condition.RCIfTileCommand;
import com.example.a236333_hw3.RunEnvironment.Program.Command.Jump.RCJumpCommand;
import com.example.a236333_hw3.RunEnvironment.Program.RCProgram;

import java.util.ArrayList;

public class EnumsToCommandConverter {

   /* QREnums[] enumsRow =
        {   QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN,
            QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN,
            QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN,
            QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN,
            QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN,
            QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN,
            QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN,
            QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN, QREnums.NaN    }; */


    // the board proportions & other constants
    private final int ROWS = 8, COLS = 6, NO_JUMP = -1, TOTAL_JUMPS = 3;

    // this are variables that are used during the calculation
    private int     JumpToIndex[]   = {NO_JUMP, NO_JUMP, NO_JUMP}; // cmd index to jump to
    private int     JumpFromIndex[] = {NO_JUMP, NO_JUMP, NO_JUMP}; // cmd index to jump from
    int currentLineIndex = 0;
    private ArrayList<RCCommand> commands;

    // Main converter function
    public RCProgram getProgram(ArrayList<QREnums> enums) throws RCCompilerException {
        // helps creating the current RCProgram
        commands = new ArrayList<>();

        RCExecuteStartProgram startCmd = new RCExecuteStartProgram();
        startCmd.setNextIndex(1);
        commands.add(startCmd);

        // helps tracing jump to & from
        JumpToIndex[0]      = NO_JUMP;
        JumpToIndex[1]      = NO_JUMP;
        JumpToIndex[2]      = NO_JUMP;
        JumpFromIndex[0]    = NO_JUMP;
        JumpFromIndex[1]    = NO_JUMP;
        JumpFromIndex[2]    = NO_JUMP;

        ArrayList<RCCommand> prevLine   = new ArrayList<RCCommand>();
        ArrayList<RCCommand> currLine   = new ArrayList<RCCommand>();
        currentLineIndex = 0;

        while (currentLineIndex < ROWS) {
            QREnums enumsRow[] = {   enums.get(currentLineIndex*COLS),   enums.get(currentLineIndex*COLS+1),
                                     enums.get(currentLineIndex*COLS+2), enums.get(currentLineIndex*COLS+3),
                                     enums.get(currentLineIndex*COLS+4), enums.get(currentLineIndex*COLS+5) };

            // encode curr line
            for (int startIndex = 0; startIndex < COLS; startIndex++) {
                if (enumsRow[startIndex] == QREnums.NaN)
                    continue;
                else if (!contains(spinalCards, enumsRow[startIndex])) {
                    // ERROR - command cannot start with non-spinal card
                    throw new RCCompilerException("Command cannot start with non-spinal card!", startIndex, currentLineIndex);
                } else {
                    RCCommand cmd = readCommand(startIndex, enumsRow);
                    startIndex += cmd.getLength()-1;  // this should be getLength()-1, because the loop will increase the start index by one in the next iteration

                    // add to curr line collection
                    currLine.add(cmd);
                }
            }

            // check only for first line
            if (currentLineIndex == 0) {
                if (currLine.size() != 1) {
                    if (currLine.size() == 0) {
                        throw new RCCompilerException(
                                "The first command should be placed in the first line!",
                                0, 0);
                    } else {
                        throw new RCCompilerException(
                                "There are too many commands in the first line... where should we start from?",
                                0, 0);
                    }
                } else {
                    currLine.get(0).setReachable(true);
                }
            } else {
                // update indexes in prev line
                for (RCCommand prevCmd : prevLine) {
                    if (prevCmd instanceof RCExecuteCommand) {
                        for (RCCommand cmd : currLine) {
                            if (cmd.getSpinalIndex() == prevCmd.getSpinalIndex()) {
                                ((RCExecuteCommand) prevCmd).setNextIndex(cmd.getCommandIndex());
                                cmd.setReachable(true);
                                break;
                            }
                        }
                    } else if (prevCmd instanceof RCJumpCommand) {
                        for (RCCommand cmd : currLine) {
                            if (cmd.getSpinalIndex() == prevCmd.getSpinalIndex()) {
                                ((RCJumpCommand) prevCmd).setNextNoJumpIndex(cmd.getCommandIndex());
                                cmd.setReachable(true);
                                break;
                            }
                        }
                    } else if (prevCmd instanceof RCIfCommand) {
                        for (RCCommand cmd : currLine) {
                            if (cmd.getSpinalIndex() == prevCmd.getSpinalIndex() + 1) {
                                ((RCIfCommand) prevCmd).setNextTrue(cmd.getCommandIndex());
                                cmd.setReachable(true);
                            } else if (cmd.getSpinalIndex() + cmd.getLength() == prevCmd.getSpinalIndex()) {
                                ((RCIfCommand) prevCmd).setNextFalse(cmd.getCommandIndex());
                                cmd.setReachable(true);
                            }
                        }
                        if (((RCIfCommand) prevCmd).getNextFalse() == -1 ||
                            ((RCIfCommand) prevCmd).getNextTrue()  == -1)  {

                            // ERROR - command cannot start with non-spinal card
                            throw new RCCompilerException(
                                    "Both cases of a condition should be handled!",
                                    prevCmd.getSpinalIndex(),
                                    prevCmd.getLineIndex());
                        }
                    }
                }

                // check reachable
                for (RCCommand cmd : currLine) {
                    if (!cmd.isReachable()) {
                        throw new RCCompilerException(
                                "This command does not seem to be reachable! are you missing anything?",
                                cmd.getSpinalIndex(),
                                cmd.getLineIndex());
                    }
                }
            }

            // prev line <= curr line
            prevLine   = currLine;
            currLine   = new ArrayList<RCCommand>();

            currentLineIndex++;
        }

        // make sure that last line does not contain an if command
        for (RCCommand prevCmd : prevLine) {
            if (prevCmd instanceof RCIfCommand) {
                throw new RCCompilerException(
                        "Both cases of a condition should be handled! can you do that in the last line?",
                        prevCmd.getSpinalIndex(), prevCmd.getLineIndex());
            }
        }

        for (int i=0; i<TOTAL_JUMPS; i++) {
            // check to jump_from & jump_to vectors equality
            if ((JumpFromIndex[i] == NO_JUMP) && (JumpToIndex[i] != NO_JUMP)) {
                // Jump to is defined but no Jump from
                throw new RCCompilerException(
                        "Jump destination is defined, but no source defined!",
                        commands.get(JumpToIndex[i]).getSpinalIndex(),
                        commands.get(JumpToIndex[i]).getLineIndex());

            } else if ((JumpToIndex[i] == NO_JUMP) && (JumpFromIndex[i] != NO_JUMP)) {
                // Jump from is defined but no Jump to
                throw new RCCompilerException(
                        "Jump source is defined, but no destination!",
                        commands.get(JumpFromIndex[i]).getSpinalIndex(),
                        commands.get(JumpFromIndex[i]).getLineIndex());
            }
            // update indexes in jump_from
            else if (JumpFromIndex[i] != NO_JUMP) {
                ((RCJumpCommand) commands.get(JumpFromIndex[i])).setNextJumpIndex(JumpToIndex[i]);
            }
        }

        // TODO - after reachable marked, make sure that the program is all connected

        RCProgram prog = new RCProgram();
        prog.setCommands(commands);
        return prog;
    }

    private RCCommand readCommand(int startIndex, QREnums[] enumsRow) throws RCCompilerException {
        int commandIndex = commands.size();
        int currIndex = startIndex;
        int length = 0;
        RCCommand cmd;
        boolean startsWithJumpTo = false;
        int jumpToCardsCount = 0;

        while (contains(JumpToCards, enumsRow[currIndex]) && currIndex < COLS) {
            startsWithJumpTo = true;
            int i = enumsRow[currIndex] == QREnums.JMP_TO_1 ? 0 :
                    enumsRow[currIndex] == QREnums.JMP_TO_2 ? 1 :
                    /*enumsRow[currIndex] = QREnums.JMP_TO_3*/2;
            JumpToIndex[i] = commandIndex;
            currIndex++;
            length++;
            jumpToCardsCount++;
        }

        if (currIndex >= COLS || !contains(spinalCards, enumsRow[currIndex])) {
            throw new RCCompilerException("An attempt to jump to a destination without a spinal card!",
                    currIndex, currentLineIndex);
        }

        // notice that in this point, length will contain the amount of JMP_TO cards in the
        // beginning of the current command.
        if (enumsRow[currIndex] == QREnums.CONDITION) {
            cmd = readCondCommand(enumsRow, currIndex, length);

            // Add to commands queue, and save command index & spinal index
            commands.add(cmd);
            cmd.setCommandIndex(commands.size()-1);
            cmd.setSpinalIndex(startIndex + jumpToCardsCount);
        } else
        {
            if (contains(JumpFromCards, enumsRow[currIndex])) {
                int i = enumsRow[currIndex] == QREnums.JMP_FROM_1 ? 0 :
                        enumsRow[currIndex] == QREnums.JMP_FROM_2 ? 1 :
                        /*enumsRow[currIndex] = QREnums.JMP_FORM_3*/2;
                JumpFromIndex[i] = commandIndex;
                cmd = readJumpCommand(enumsRow, currIndex, length);
            } else {
                cmd = readExcuteCommand(enumsRow, currIndex, length);
            }

            // Add to commands queue, and save command index & spinal index
            commands.add(cmd);
            cmd.setCommandIndex(commands.size()-1);
            cmd.setSpinalIndex(startIndex);
        }

        cmd.setLineIndex(currentLineIndex);

        // if command starts with jump_to -> set reachable to true (will cause a known issue bug)
        if (startsWithJumpTo) cmd.setReachable(true);

        return cmd;
    }

    //region Signal command analysis

    private RCIfCommand readCondCommand(QREnums[] enumsRow, int currIndex, int currLength) throws RCCompilerException {
        currIndex++;
        currLength++;

        if (currIndex >= COLS) {
            throw new RCCompilerException("There seems to be no condition to check. dont forget to define a condition!",
                    currIndex-1, currentLineIndex);
        }

        switch (enumsRow[currIndex]) {
            case FENCE: {
                // handle fence condition
                if (currIndex+1 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex+1])) {
                    RCIfFenceCommand cmd = new RCIfFenceCommand();
                    cmd.setLength(currLength + 1);
                    return cmd;
                } else {
                    throw new RCCompilerException(
                            "A condition that checks if RoboCode reached the edge of the playground should not have any extra parameters",
                            currIndex-1, currentLineIndex);
                }
                //break; // code unreachable!
            }
            case TILE: {
                // handle tile color condition
                if ((currIndex+1 < COLS && contains(colorCards, enumsRow[currIndex+1])) &&
                    (currIndex+2 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex+2]))) {
                    RCIfTileCommand cmd = new RCIfTileCommand();
                    cmd.setLength(currLength + 2);
                    cmd.setColor(convertColor(enumsRow[currIndex+1]));
                    return cmd;
                } else {
                    throw new RCCompilerException(
                        "A tile condition can only check if the tile is of a certain color.",
                        currIndex-1, currentLineIndex);
                }
                //break; // code unreachable!
            }
            case BOX: {
                // handle box color condition -> COND card + BOX card + COLOR card
                if ((currIndex+1 < COLS && contains(colorCards, enumsRow[currIndex+1])) &&
                    (currIndex+2 >= COLS || contains(spinalOrNaNCards, enumsRow[currIndex+2]))) {
                    RCIfBoxColorCommand cmd = new RCIfBoxColorCommand();
                    cmd.setLength(currLength + 2);
                    cmd.setColor(convertColor(enumsRow[currIndex+1]));
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
                        throw new RCCompilerException(
                                "There seems to be an error with the way that the condition on the box number is defined!",
                                currIndex-1, currentLineIndex);
                    }
                } else {
                    throw new RCCompilerException(
                            "There seems to be an error with the way that the condition on the box is defined!",
                            currIndex-1, currentLineIndex);
                }
                //break; // code unreachable!
            }
            default:
                throw new RCCompilerException(
                        "There seems to be an error with the way that the condition is defined!",
                        currIndex-1, currentLineIndex);
                //break; // code unreachable!
        }
        //return null; // code unreachable!
    }

    private Color convertColor(QREnums qrEnums) {
        return (qrEnums == QREnums.VAR_COLOR_RED       ? Color.RED :
                qrEnums == QREnums.VAR_COLOR_BLUE      ? Color.BLUE :
                qrEnums == QREnums.VAR_COLOR_GREEN     ? Color.GREEN :
                qrEnums == QREnums.VAR_COLOR_YELLOW    ? Color.YELLOW :
                qrEnums == QREnums.VAR_COLOR_WHITE     ? Color.WHITE :
               /*qrEnums = QREnums.VAR_COLOR_BLACK?*/   Color.BLACK);
    }

    private RCJumpCommand readJumpCommand(QREnums[] enumsRow, int currIndex, int currLength) throws RCCompilerException {
        currLength++; // update the length. note we are keeping the curr index on the jump card!!!
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
                // illegal jump (with reps) command
                throw new RCCompilerException(
                        "There seems to be something wrong with the way that the jump command with repetition limits is defined...",
                        currIndex, currentLineIndex);
            }
        }
        else {
            // throw Exception - illegal jump command
            throw new RCCompilerException(
                    "There seems to be something wrong with the way that the jump command is defined...",
                    currIndex, currentLineIndex);
        }

        // return null; // unreachable
    }

    private RCExecuteCommand readExcuteCommand(QREnums[] enumsRow, int currIndex, int currLength) throws RCCompilerException {
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
                    // illegal stop / ForkUp / ForkDown  execute seq
                    throw new RCCompilerException(
                            ((enumsRow[currIndex] == QREnums.CMD_FORKLIFT_UP   ?   "Fork up " :
                              enumsRow[currIndex] == QREnums.CMD_FORKLIFT_DOWN ?   "Fork down " :
                              /*enumsRow[currIndex] == QREnums.CMD_STOP*/          "Stop ") +
                             " command cannot be repetitive or get any extra parameters."),
                            currIndex, currentLineIndex);
                }
                //break; // unreachable command
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
                    // illegal execute (with reps) command
                    throw new RCCompilerException(
                            "There seems to be something wrong with the way that the command is defined. are trying to define a repetition? are there any non essential parameters?",
                            currIndex, currentLineIndex);
                }
                //break; // unreachable code
            }
            default:
                // illegal command
                throw new RCCompilerException(
                        "There something wrong with the card arrangment. take a look again!",
                        currIndex, currentLineIndex);
        }

        //return null; // unreachable code
    }

    //endregion

    //region Enum groups definition

    private boolean contains( QREnums arr[], QREnums value) {
        for (int i=0; i<arr.length; i++) if (arr[i] == value) return true;
        return false;
    }

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
