package com.example.a236333_hw3.Tools;

import com.example.a236333_hw3.RunEnvironment.Program.Color;

import java.util.ArrayList;

public class roboCodeTask {
    public int      ID;
    public int      Points;
    public int      stepsLimit;
    public String   Title;
    public String   Description;
    public String   Hints;
    public String   Arrangement;
    public Boolean  Accomplished;
    public Boolean  Active;
    public Boolean  UseCarpet;

    // rules to check
    public ArrayList<Color> FenceColors;
    public Boolean  checkExact;
    public String   checkExactValue;
    public Boolean  checkCond;
    public String   checkCondValue;

    public roboCodeTask() {
        FenceColors = new ArrayList<Color>();
    }

    public void FillFenceColors(String colors) {
        for (String curr : colors.split(",")) {
            if      (curr.equals("WHITE"    )) FenceColors.add(Color.WHITE  );
            else if (curr.equals("YELLOW"   )) FenceColors.add(Color.YELLOW );
            else if (curr.equals("BLUE"     )) FenceColors.add(Color.BLUE   );
            else if (curr.equals("RED"      )) FenceColors.add(Color.RED    );
            else if (curr.equals("GREEN"    )) FenceColors.add(Color.GREEN  );
            else if (curr.equals("BLACK"    )) FenceColors.add(Color.BLACK  );
            else if (curr.equals("NON_COLOR")) FenceColors.add(Color.NON_COLOR);
        }
    }

    public String getFenceColorsString() {
        if (FenceColors.size() == 0) return "";
        String val = "";
        for (Color c: FenceColors) {
            val += ( c == Color.WHITE  ? "white, " :
                     c == Color.YELLOW ? "yellow, " :
                     c == Color.BLUE   ? "blue, " :
                     c == Color.RED    ? "red, " :
                     c == Color.GREEN  ? "green, " :
                     c == Color.BLACK  ? "black, " : "");
        }
        return val.substring(0, val.length()-3);
    }
}
