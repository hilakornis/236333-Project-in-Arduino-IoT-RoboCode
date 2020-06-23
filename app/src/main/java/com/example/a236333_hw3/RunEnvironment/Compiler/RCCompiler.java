package com.example.a236333_hw3.RunEnvironment.Compiler;

import com.example.a236333_hw3.RunEnvironment.Program.RCProgram;

public class RCCompiler {

    public RCCompiler() {
    }

    public RCProgram Compile(String inputStream) throws RCCompilerException {
        DataToEnumsConverter StreamToEnums = new DataToEnumsConverter();
        EnumsToCommandConverter EnumsToCommands = new EnumsToCommandConverter();
        return EnumsToCommands.getProgram(StreamToEnums.getQrEnumsFromStrAsArray(inputStream));
    }
}
