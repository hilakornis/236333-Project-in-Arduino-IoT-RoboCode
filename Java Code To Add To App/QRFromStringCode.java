package OOP.Solution;

import java.util.ArrayList;
import java.util.LinkedList;

enum QREnums {
    VAR_1,
    VAR_2,
    VAR_3,
    VAR_4,
    VAR_5,
    VAR_6,
    VAR_7,
    VAR_8,
    VAR_9,

    CND,

    JMP_F1,
    JMP_T1,
    JMP_F2,
    JMP_T2,
    JMP_F3,
    JMP_T3,

     T_L,
     T_R,
     T_U,
     G_FW,
     G_BK,
     F_U,
     F_D,

     CL_R,
     CL_BL,
     CL_G,
     CL_Y,
     CL_W,
     CL_BK,

     TILE,
     BOX,
     NaN
}

public class QRFromStringCode {

    public static String get_enum_without_space(String val){
        if(val.contains(" ")) {
            val = val.substring(val.indexOf(" ")+1, val.length());
        }
        return val;
    }

    public static LinkedList<String> getArrayVals(String arr_as_string){
        LinkedList<String> arr_vals = new LinkedList<String>();
//        String with_out_brackets =  arr_as_string.substring(1, arr_as_string.length()-1);
        String with_out_brackets =  arr_as_string;

        String val = "";
        while (with_out_brackets.contains(",")){
            val = get_enum_without_space(with_out_brackets.substring(0,with_out_brackets.indexOf(",")));
            with_out_brackets = with_out_brackets.substring(with_out_brackets.indexOf(",")+1,with_out_brackets.length());
            arr_vals.add(val);
        }

        val = get_enum_without_space(with_out_brackets);
        arr_vals.add(val);
        return arr_vals;
    }

    public static ArrayList<QREnums> getQrEnumsFromStrAsArray(String qr_str_arr){
        LinkedList<String> qr_enum_vals = getArrayVals(qr_str_arr);
        ArrayList<QREnums> arr_qr =  new ArrayList<QREnums>();

        QREnums val;
        String val_str = "";
        for (int i = 0; i < qr_enum_vals.size(); i++) {
            val_str = qr_enum_vals.get(i);

            switch (val_str) {
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
                    arr_qr.add(QREnums.CND);
                    break;
                //-----------
                case "JMP_F1":
                    arr_qr.add(QREnums.JMP_F1);
                    break;
                case "JMP_T1":
                    arr_qr.add(QREnums.JMP_T1);
                    break;
                case "JMP_F2":
                    arr_qr.add(QREnums.JMP_F2);
                    break;
                case "JMP_T2":
                    arr_qr.add(QREnums.JMP_T2);
                    break;
                case "JMP_F3":
                    arr_qr.add(QREnums.JMP_F3);
                    break;
                case "JMP_T3":
                    arr_qr.add(QREnums.JMP_T3);
                    break;
                //-----------
                case "T_L":
                    arr_qr.add(QREnums.T_L);
                    break;
                case "T_R":
                    arr_qr.add(QREnums.T_R);
                    break;
                case "T_U":
                    arr_qr.add(QREnums.T_U);
                    break;
                case "G_FW":
                    arr_qr.add(QREnums.G_FW);
                    break;
                case "G_BK":
                    arr_qr.add(QREnums.G_BK);
                    break;
                case "F_U":
                    arr_qr.add(QREnums.F_U);
                    break;
                case "F_D":
                    arr_qr.add(QREnums.F_D);
                    break;
                //-----------
                case "CL_R":
                    arr_qr.add(QREnums.CL_R);
                    break;
                case "CL_BL":
                    arr_qr.add(QREnums.CL_BL);
                    break;
                case "CL_G":
                    arr_qr.add(QREnums.CL_G);
                    break;
                case "CL_Y":
                    arr_qr.add(QREnums.CL_Y);
                    break;
                case "CL_W":
                    arr_qr.add(QREnums.CL_W);
                    break;
                case "CL_BK":
                    arr_qr.add(QREnums.CL_BK);
                    break;
                //-----------
                case "TILE":
                    arr_qr.add(QREnums.TILE);
                    break;
                case "BOX":
                    arr_qr.add(QREnums.BOX);
                    break;
                default: //this is for "NaN" string
                    arr_qr.add(QREnums.NaN);
            }


        }
        return arr_qr;
    }

    public static void main(String[] args) {

        String qr_arr = "TILE, BOX, CND, 6, 8, NaN";
        ArrayList<QREnums> qr_enum_vals = getQrEnumsFromStrAsArray(qr_arr);

        System.out.println("This is arr_qr:");
        System.out.println(qr_enum_vals);

    }
}
