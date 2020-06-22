package com.example.a236333_hw3.Compiler.Command.Execute;

import androidx.annotation.NonNull;

public class RCExecuteRepsTurnUTurnCommand extends RCExecuteRepsCommand {
    @NonNull
    @Override
    public String toString() {
        return "======== U TURN COMMAND " + "\n" + super.toString();
    }
}
