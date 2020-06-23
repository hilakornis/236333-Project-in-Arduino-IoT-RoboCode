package com.example.a236333_hw3.Compiler;

import com.example.a236333_hw3.RCProgram.RCProgram;

public class RCCompiler {

    public RCCompiler() {
    }

    public RCProgram Compile(String inputStream) throws RCCompilerException {
        DataToEnumsConverter StreamToEnums = new DataToEnumsConverter();
        EnumsToCommandConverter EnumsToCommands = new EnumsToCommandConverter();
        return EnumsToCommands.getProgram(StreamToEnums.getQrEnumsFromStrAsArray(inputStream));
    }
}
