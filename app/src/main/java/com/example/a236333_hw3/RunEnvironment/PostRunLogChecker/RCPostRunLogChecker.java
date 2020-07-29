package com.example.a236333_hw3.RunEnvironment.PostRunLogChecker;

import com.example.a236333_hw3.RunEnvironment.Executor.RCProgramExecutor;
import com.example.a236333_hw3.RunEnvironment.Log.Item.RCProgramLogItem;
import com.example.a236333_hw3.RunEnvironment.Log.Item.RCProgramLogItemMovement;
import com.example.a236333_hw3.RunEnvironment.Log.Item.RCProgramLogItemMovementType;
import com.example.a236333_hw3.Tools.RoboCodeSettings;

import java.util.ArrayList;

public class RCPostRunLogChecker {
    private static RCPostRunLogChecker _inst;

    public static RCPostRunLogChecker getInstance() {
        if (_inst == null) _inst = new RCPostRunLogChecker();
        return _inst;
    }

    public void CheckRunLog(ArrayList<RCProgramLogItem> logs, int stepsCount) throws RCPostRunLogCheckerException {

        // step limit testing
        if (RoboCodeSettings.getInstance().current.stepsLimit != RCProgramExecutor.NO_STEPS_LIMIT) {
            if (stepsCount > RoboCodeSettings.getInstance().current.stepsLimit) {
                throw new RCPostRunLogCheckerException(
                        "RoboCode has excided the maximal number of steps allowed, of " +
                        String.valueOf(RoboCodeSettings.getInstance().current.stepsLimit) +
                        " steps. Recheck your solution and try again!");
            }
        }

        // Exact testing
        if (RoboCodeSettings.getInstance().current.checkExact) {
            // check exact
            ArrayList<RCProgramLogItemMovementType> expected =
                    getExpectedListFromString(RoboCodeSettings.getInstance().current.checkExactValue);
            ArrayList<RCProgramLogItemMovementType> actual =
                    getExpectedListFromLogs(logs);

            if (expected.size() > actual.size()) {
                throw new RCPostRunLogCheckerException(
                        "It seems like RoboCode didn't do enough actions in order to solve the task. Recheck your solution and try again!");
            } else if (expected.size() < actual.size()) {
                throw new RCPostRunLogCheckerException(
                        "It seems like RoboCode did too much actions in order to solve the task. Recheck your solution and try again!");
            } else {
                for (int i=0; i<actual.size(); i++) {
                    if (expected.get(i) != actual.get(i)) {
                        throw new RCPostRunLogCheckerException(
                                "It seems like RoboCode didn't do what it was suppose to do." +
                                    " At a certain point RoboCode was suppose to " +
                                        convToNowString(expected.get(i)) +
                                     " but instead it " +
                                        convToPastString(expected.get(i)) +
                                    ". Recheck your solution and try again!" );
                    }
                }
            }
        }

        if (RoboCodeSettings.getInstance().current.checkCond) {
            // TODO : check cond
        }

    }

    private String convToNowString(RCProgramLogItemMovementType type) {
        if      (type == RCProgramLogItemMovementType.TURN_LEFT)          return "turn left";
        else if (type == RCProgramLogItemMovementType.TURN_RIGHT)         return "turn right";
        else if (type == RCProgramLogItemMovementType.TURN_AROUND)        return "turn around";
        else if (type == RCProgramLogItemMovementType.GO_FORWARD)         return "go forward";
        else if (type == RCProgramLogItemMovementType.GO_BACKWARD)        return "go backward";
        else if (type == RCProgramLogItemMovementType.FORKLIFT_UP)        return "pick up a box";
        else  /*(type == RCProgramLogItemMovementType.FORKLIFT_DOWN)*/    return "place down a box";
    }

    private String convToPastString(RCProgramLogItemMovementType type) {
        if      (type == RCProgramLogItemMovementType.TURN_LEFT)          return "turned left";
        else if (type == RCProgramLogItemMovementType.TURN_RIGHT)         return "turned right";
        else if (type == RCProgramLogItemMovementType.TURN_AROUND)        return "turned around";
        else if (type == RCProgramLogItemMovementType.GO_FORWARD)         return "went forward";
        else if (type == RCProgramLogItemMovementType.GO_BACKWARD)        return "went backward";
        else if (type == RCProgramLogItemMovementType.FORKLIFT_UP)        return "picked up a box";
        else  /*(type == RCProgramLogItemMovementType.FORKLIFT_DOWN)*/    return "placed down a box";
    }

    private ArrayList<RCProgramLogItemMovementType> getExpectedListFromLogs(ArrayList<RCProgramLogItem> logs) {
        ArrayList<RCProgramLogItemMovementType> lst = new ArrayList<RCProgramLogItemMovementType>();
        for (RCProgramLogItem curr : logs) {
            if (curr instanceof RCProgramLogItemMovement) {
                lst.add(((RCProgramLogItemMovement)curr).getType());
            }
        }
        return lst;
    }

    private ArrayList<RCProgramLogItemMovementType> getExpectedListFromString(String checkExactValue) {
        ArrayList<RCProgramLogItemMovementType> lst = new ArrayList<RCProgramLogItemMovementType>();
        for (String curr : checkExactValue.split(",")) {
            if      (curr.equals("TURN_LEFT"))      lst.add(RCProgramLogItemMovementType.TURN_LEFT   );
            else if (curr.equals("TURN_RIGHT"))     lst.add(RCProgramLogItemMovementType.TURN_RIGHT  );
            else if (curr.equals("TURN_AROUND"))    lst.add(RCProgramLogItemMovementType.TURN_AROUND );
            else if (curr.equals("GO_FORWARD"))     lst.add(RCProgramLogItemMovementType.GO_FORWARD  );
            else if (curr.equals("GO_BACKWARD"))    lst.add(RCProgramLogItemMovementType.GO_BACKWARD );
            else if (curr.equals("FORKLIFT_UP"))    lst.add(RCProgramLogItemMovementType.FORKLIFT_UP );
            else if (curr.equals("FORKLIFT_DOWN"))  lst.add(RCProgramLogItemMovementType.FORKLIFT_DOWN);
        }
        return lst;
    }
}
