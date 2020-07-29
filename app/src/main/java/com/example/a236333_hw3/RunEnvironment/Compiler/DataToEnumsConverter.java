package com.example.a236333_hw3.RunEnvironment.Compiler;

import java.util.ArrayList;
import java.util.LinkedList;

public class DataToEnumsConverter {

    private String get_enum_without_space(String val) {
        if (val.contains(" ")) {
            val = val.substring(val.indexOf(" ") + 1, val.length());
        }
        return val;
    }

    private LinkedList<String> getArrayVals(String arr_as_string) {
        LinkedList<String> arr_vals = new LinkedList<String>();
//        String with_out_brackets =  arr_as_string.substring(1, arr_as_string.length()-1);
        String with_out_brackets = arr_as_string;

        String val = "";
        while (with_out_brackets.contains(",")) {
            val = get_enum_without_space(with_out_brackets.substring(0, with_out_brackets.indexOf(",")));
            with_out_brackets = with_out_brackets.substring(with_out_brackets.indexOf(",") + 1, with_out_brackets.length());
            arr_vals.add(val);
        }

        val = get_enum_without_space(with_out_brackets);
        arr_vals.add(val);
        return arr_vals;
    }

    public ArrayList<QREnums> getQrEnumsFromStrAsArray(String data) {
        LinkedList<String> qr_enum_vals = getArrayVals(data);
        ArrayList<QREnums> arr_qr = new ArrayList<QREnums>();

        QREnums val;
        String val_str = "";
        for (int i = 0; i < qr_enum_vals.size(); i++) {
            val_str = qr_enum_vals.get(i);

            switch (val_str) {
                case "0":
                    arr_qr.add(QREnums.VAR_0);
                    break;
                case "1":
                    arr_qr.add(QREnums.VAR_1);
                    break;
                case "2":
                    arr_qr.add(QREnums.VAR_2);
                    break;
                case "3":
                    arr_qr.add(QREnums.VAR_3);
                    break;
                case "4":
                    arr_qr.add(QREnums.VAR_4);
                    break;
                case "5":
                    arr_qr.add(QREnums.VAR_5);
                    break;
                case "6":
                    arr_qr.add(QREnums.VAR_6);
                    break;
                case "7":
                    arr_qr.add(QREnums.VAR_7);
                    break;
                case "8":
                    arr_qr.add(QREnums.VAR_8);
                    break;
                case "9":
                    arr_qr.add(QREnums.VAR_9);
                    break;
                //-----------
                case "CND":
                    arr_qr.add(QREnums.CONDITION);
                    break;
                //-----------
                case "JMP_F1":
                    arr_qr.add(QREnums.JMP_FROM_1);
                    break;
                case "JMP_T1":
                    arr_qr.add(QREnums.JMP_TO_1);
                    break;
                case "JMP_F2":
                    arr_qr.add(QREnums.JMP_FROM_2);
                    break;
                case "JMP_T2":
                    arr_qr.add(QREnums.JMP_TO_2);
                    break;
                case "JMP_F3":
                    arr_qr.add(QREnums.JMP_FROM_3);
                    break;
                case "JMP_T3":
                    arr_qr.add(QREnums.JMP_TO_3);
                    break;
                //-----------
                case "T_L":
                    arr_qr.add(QREnums.CMD_TURN_LEFT);
                    break;
                case "T_R":
                    arr_qr.add(QREnums.CMD_TURN_RIGHT);
                    break;
                case "T_U":
                    arr_qr.add(QREnums.CMD_TURN_AROUND);
                    break;
                case "G_FW":
                    arr_qr.add(QREnums.CMD_GO_FORWARD);
                    break;
                case "G_BK":
                    arr_qr.add(QREnums.CMD_GO_BACKWARD);
                    break;
                case "F_U":
                    arr_qr.add(QREnums.CMD_FORKLIFT_UP);
                    break;
                case "F_D":
                    arr_qr.add(QREnums.CMD_FORKLIFT_DOWN);
                    break;
                case "STP":
                    arr_qr.add(QREnums.CMD_STOP);
                    break;
                //-----------
                case "CL_R":
                    arr_qr.add(QREnums.VAR_COLOR_RED);
                    break;
                case "CL_BL":
                    arr_qr.add(QREnums.VAR_COLOR_BLUE);
                    break;
                case "CL_G":
                    arr_qr.add(QREnums.VAR_COLOR_GREEN);
                    break;
                case "CL_Y":
                    arr_qr.add(QREnums.VAR_COLOR_YELLOW);
                    break;
                case "CL_W":
                    arr_qr.add(QREnums.VAR_COLOR_WHITE);
                    break;
                case "CL_BK":
                    arr_qr.add(QREnums.VAR_COLOR_BLACK);
                    break;
                //-----------
                case "TILE":
                    arr_qr.add(QREnums.TILE);
                    break;
                case "BOX":
                    arr_qr.add(QREnums.BOX);
                    break;
                case "FN":
                    arr_qr.add(QREnums.FENCE);
                    break;
                default: //this is for "NaN" string
                    arr_qr.add(QREnums.NaN);
            }
        }
        return arr_qr;
    }


}
