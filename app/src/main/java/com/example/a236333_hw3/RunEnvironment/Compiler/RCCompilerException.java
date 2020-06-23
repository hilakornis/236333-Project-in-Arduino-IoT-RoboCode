package com.example.a236333_hw3.RunEnvironment.Compiler;

public class RCCompilerException extends Exception {
    private int colId;
    private int rowId;

    public RCCompilerException(String message, int colId, int rowId) {
        super(message);
        this.colId = colId;
        this.rowId = rowId;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getColId() {
        return colId;
    }

    public void setColId(int colId) {
        this.colId = colId;
    }

}
