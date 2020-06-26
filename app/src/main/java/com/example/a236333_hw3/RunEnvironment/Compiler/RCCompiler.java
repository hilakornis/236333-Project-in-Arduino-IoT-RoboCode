package com.example.a236333_hw3.RunEnvironment.Compiler;

import com.example.a236333_hw3.RunEnvironment.Program.RCProgram;
import com.example.a236333_hw3.RunEnvironment.Status.RCProgramStatus;

public class RCCompiler {

    private static RCCompiler _inst;

    public static RCCompiler getInstance() {
        if (_inst == null) _inst = new RCCompiler();
        return _inst;
    }

    private RCCompiler() {
    }

    public RCProgram Compile(String inputStream) throws RCCompilerException {
        DataToEnumsConverter StreamToEnums = new DataToEnumsConverter();
        EnumsToCommandConverter EnumsToCommands = new EnumsToCommandConverter();
        return EnumsToCommands.getProgram(StreamToEnums.getQrEnumsFromStrAsArray(inputStream));
    }
}
