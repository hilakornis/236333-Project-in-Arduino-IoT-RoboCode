package com.example.a236333_hw3.RunEnvironment.Log.Item;

public class RCProgramLogItemMovement extends RCProgramLogItem {
    private RCProgramLogItemMovementType type;

    public RCProgramLogItemMovementType getType() {
        return type;
    }

    public void setType(RCProgramLogItemMovementType type) {
        this.type = type;
    }

    public RCProgramLogItemMovement() {
        this.type = RCProgramLogItemMovementType.GO_FORWARD;
    }

    public RCProgramLogItemMovement(RCProgramLogItemMovementType type) {
        this.type = type;
    }
}
