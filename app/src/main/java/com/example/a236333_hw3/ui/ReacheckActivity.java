package com.example.a236333_hw3.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
//import android.widget.DataImageButton;
import android.widget.Toast;


import com.example.a236333_hw3.R;
import com.example.a236333_hw3.RunEnvironment.Compiler.DataToEnumsConverter;
import com.example.a236333_hw3.RunEnvironment.Compiler.QREnums;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ReacheckActivity<DataDataImageButton> extends AppCompatActivity implements View.OnClickListener {

    public static String step1_result_code =
            "T_L,NaN,NaN,NaN,NaN,NaN,"  +
            "T_R,NaN,NaN,NaN,NaN,NaN,"  +
            "T_U,NaN,NaN,NaN,NaN,NaN,"  +
            "G_FW,NaN,NaN,NaN,NaN,NaN," +
            "G_BK,NaN,NaN,NaN,NaN,NaN," +
            "F_U,NaN,NaN,NaN,NaN,NaN,"  +
            "F_D,NaN,NaN,NaN,NaN,NaN,"  +
            "NaN,NaN,NaN,NaN,NaN,NaN";

    private static Semaphore reacheckSemaphore;

    public static Semaphore get_reacheckSemaphore() {
        if (reacheckSemaphore == null) reacheckSemaphore = new Semaphore(0);
        return reacheckSemaphore;
    }



    private DataImageButton[][]  table_buttons;


    private Button done_button;

    private ArrayList<QREnums> enums_in_table ;

    private String[] listCards = new String[]{
            "0", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "CND", "JMP_F1", "JMP_T1",
            "JMP_F2", "JMP_T2", "JMP_F3", "JMP_T3", "T_L", "T_R",
            "T_U", "G_FW", "G_BK", "F_U", "F_D", "STP",
            "CL_R", "CL_BL", "CL_G", "CL_Y", "CL_W", "CL_BK",
            "TILE", "BOX", "FN", "NaN"};

    public ArrayList<QREnums> getQrEnumsFromString(String input){
        DataToEnumsConverter StreamToEnums = new DataToEnumsConverter();
        ArrayList<QREnums> arr = StreamToEnums.getQrEnumsFromStrAsArray(input);
        return arr;
    }

    protected void setListenersForTableButtons(){
        // --- Row 0
        this.table_buttons[0][0].setOnClickListener(this);
        this.table_buttons[0][1].setOnClickListener(this);
        this.table_buttons[0][2].setOnClickListener(this);
        this.table_buttons[0][3].setOnClickListener(this);
        this.table_buttons[0][4].setOnClickListener(this);
        this.table_buttons[0][5].setOnClickListener(this);

        // --- Row 1
        this.table_buttons[1][0].setOnClickListener(this);
        this.table_buttons[1][1].setOnClickListener(this);
        this.table_buttons[1][2].setOnClickListener(this);
        this.table_buttons[1][3].setOnClickListener(this);
        this.table_buttons[1][4].setOnClickListener(this);
        this.table_buttons[1][5].setOnClickListener(this);

        // --- Row 2
        this.table_buttons[2][0].setOnClickListener(this);
        this.table_buttons[2][1].setOnClickListener(this);
        this.table_buttons[2][2].setOnClickListener(this);
        this.table_buttons[2][3].setOnClickListener(this);
        this.table_buttons[2][4].setOnClickListener(this);
        this.table_buttons[2][5].setOnClickListener(this);

        // --- Row 3
        this.table_buttons[3][0].setOnClickListener(this);
        this.table_buttons[3][1].setOnClickListener(this);
        this.table_buttons[3][2].setOnClickListener(this);
        this.table_buttons[3][3].setOnClickListener(this);
        this.table_buttons[3][4].setOnClickListener(this);
        this.table_buttons[3][5].setOnClickListener(this);

        // --- Row 4
        this.table_buttons[4][0].setOnClickListener(this);
        this.table_buttons[4][1].setOnClickListener(this);
        this.table_buttons[4][2].setOnClickListener(this);
        this.table_buttons[4][3].setOnClickListener(this);
        this.table_buttons[4][4].setOnClickListener(this);
        this.table_buttons[4][5].setOnClickListener(this);


        // --- Row 5
        this.table_buttons[5][0].setOnClickListener(this);
        this.table_buttons[5][1].setOnClickListener(this);
        this.table_buttons[5][2].setOnClickListener(this);
        this.table_buttons[5][3].setOnClickListener(this);
        this.table_buttons[5][4].setOnClickListener(this);
        this.table_buttons[5][5].setOnClickListener(this);

        // --- Row 6
        this.table_buttons[6][0].setOnClickListener(this);
        this.table_buttons[6][1].setOnClickListener(this);
        this.table_buttons[6][2].setOnClickListener(this);
        this.table_buttons[6][3].setOnClickListener(this);
        this.table_buttons[6][4].setOnClickListener(this);
        this.table_buttons[6][5].setOnClickListener(this);

        // --- Row 7
        this.table_buttons[7][0].setOnClickListener(this);
        this.table_buttons[7][1].setOnClickListener(this);
        this.table_buttons[7][2].setOnClickListener(this);
        this.table_buttons[7][3].setOnClickListener(this);
        this.table_buttons[7][4].setOnClickListener(this);
        this.table_buttons[7][5].setOnClickListener(this);

    }

    protected void setIdForTableButtons(){

        // --- Row 0
        this.table_buttons[0][0] =  ((DataImageButton)findViewById(R.id.DataImageButton00));

        this.table_buttons[0][1] =  ((DataImageButton)findViewById(R.id.DataImageButton01));
        this.table_buttons[0][2] =  (DataImageButton)findViewById(R.id.DataImageButton02);
        this.table_buttons[0][3] =  (DataImageButton)findViewById(R.id.DataImageButton03);
        this.table_buttons[0][4] =  (DataImageButton)findViewById(R.id.DataImageButton04);
        this.table_buttons[0][5] =  (DataImageButton)findViewById(R.id.DataImageButton05);

        // --- Row 1
        this.table_buttons[1][0] =  (DataImageButton)findViewById(R.id.DataImageButton10);
        this.table_buttons[1][1] =  (DataImageButton)findViewById(R.id.DataImageButton11);
        this.table_buttons[1][2] =  (DataImageButton)findViewById(R.id.DataImageButton12);
        this.table_buttons[1][3] =  (DataImageButton)findViewById(R.id.DataImageButton13);
        this.table_buttons[1][4] =  (DataImageButton)findViewById(R.id.DataImageButton14);
        this.table_buttons[1][5] =  (DataImageButton)findViewById(R.id.DataImageButton15);

        // --- Row 2
        this.table_buttons[2][0] =  (DataImageButton)findViewById(R.id.DataImageButton20);
        this.table_buttons[2][1] =  (DataImageButton)findViewById(R.id.DataImageButton21);
        this.table_buttons[2][2] =  (DataImageButton)findViewById(R.id.DataImageButton22);
        this.table_buttons[2][3] =  (DataImageButton)findViewById(R.id.DataImageButton23);
        this.table_buttons[2][4] =  (DataImageButton)findViewById(R.id.DataImageButton24);
        this.table_buttons[2][5] =  (DataImageButton)findViewById(R.id.DataImageButton25);

        // --- Row 3
        this.table_buttons[3][0] =  (DataImageButton)findViewById(R.id.DataImageButton30);
        this.table_buttons[3][1] =  (DataImageButton)findViewById(R.id.DataImageButton31);
        this.table_buttons[3][2] =  (DataImageButton)findViewById(R.id.DataImageButton32);
        this.table_buttons[3][3] =  (DataImageButton)findViewById(R.id.DataImageButton33);
        this.table_buttons[3][4] =  (DataImageButton)findViewById(R.id.DataImageButton34);
        this.table_buttons[3][5] =  (DataImageButton)findViewById(R.id.DataImageButton35);

        // --- Row 4
        this.table_buttons[4][0] =  (DataImageButton)findViewById(R.id.DataImageButton40);
        this.table_buttons[4][1] =  (DataImageButton)findViewById(R.id.DataImageButton41);
        this.table_buttons[4][2] =  (DataImageButton)findViewById(R.id.DataImageButton42);
        this.table_buttons[4][3] =  (DataImageButton)findViewById(R.id.DataImageButton43);
        this.table_buttons[4][4] =  (DataImageButton)findViewById(R.id.DataImageButton44);
        this.table_buttons[4][5] =  (DataImageButton)findViewById(R.id.DataImageButton45);


        // --- Row 5
        this.table_buttons[5][0] =  (DataImageButton)findViewById(R.id.DataImageButton50);
        this.table_buttons[5][1] =  (DataImageButton)findViewById(R.id.DataImageButton51);
        this.table_buttons[5][2] =  (DataImageButton)findViewById(R.id.DataImageButton52);
        this.table_buttons[5][3] =  (DataImageButton)findViewById(R.id.DataImageButton53);
        this.table_buttons[5][4] =  (DataImageButton)findViewById(R.id.DataImageButton54);
        this.table_buttons[5][5] =  (DataImageButton)findViewById(R.id.DataImageButton55);

        // --- Row 6
        this.table_buttons[6][0] =  (DataImageButton)findViewById(R.id.DataImageButton60);
        this.table_buttons[6][1] =  (DataImageButton)findViewById(R.id.DataImageButton61);
        this.table_buttons[6][2] =  (DataImageButton)findViewById(R.id.DataImageButton62);
        this.table_buttons[6][3] =  (DataImageButton)findViewById(R.id.DataImageButton63);
        this.table_buttons[6][4] =  (DataImageButton)findViewById(R.id.DataImageButton64);
        this.table_buttons[6][5] =  (DataImageButton)findViewById(R.id.DataImageButton65);

        // --- Row 7
        this.table_buttons[7][0] =  (DataImageButton)findViewById(R.id.DataImageButton70);
        this.table_buttons[7][1] =  (DataImageButton)findViewById(R.id.DataImageButton71);
        this.table_buttons[7][2] =  (DataImageButton)findViewById(R.id.DataImageButton72);
        this.table_buttons[7][3] =  (DataImageButton)findViewById(R.id.DataImageButton73);
        this.table_buttons[7][4] =  (DataImageButton)findViewById(R.id.DataImageButton74);
        this.table_buttons[7][5] =  (DataImageButton)findViewById(R.id.DataImageButton75);

    }

    protected String enumToString (QREnums enum_val ){

        String ret = "";

        switch (enum_val) {

            case VAR_0:
                ret = "0";
                break;
            case VAR_1:
                ret = "1";
                break;
            case VAR_2:
                ret = "2";
                break;
            case VAR_3:
                ret = "3";
                break;
            case VAR_4:
                ret = "4";
                break;
            case VAR_5:
                ret = "5";
                break;
            case VAR_6:
                ret = "6";
                break;
            case VAR_7:
                ret = "7";
                break;
            case VAR_8:
                ret = "8";
                break;
            case VAR_9:
                ret = "9";
                break;
            //-----------
            case CONDITION:
                ret = "CND";
                break;
            //-----------
            case JMP_FROM_1:
                ret = "JMP_F1";
                break;
            case JMP_TO_1:
                ret = "JMP_T1";
                break;
            case JMP_FROM_2:
                ret = "JMP_F2";
                break;
            case JMP_TO_2:
                ret = "JMP_T2";
                break;
            case JMP_FROM_3:
                ret = "JMP_F3";
                break;
            case JMP_TO_3:
                ret = "JMP_T3";
                break;
            //-----------
            case CMD_TURN_LEFT :
                ret = "T_L";
                break;
            case CMD_TURN_RIGHT:
                ret = "T_R";
                break;
            case CMD_TURN_AROUND:
                ret = "T_U";
                break;
            case CMD_GO_FORWARD:
                ret = "G_FW";
                break;
            case CMD_GO_BACKWARD:
                ret = "G_BK";
                break;
            case CMD_FORKLIFT_UP:
                ret = "F_U";
                break;
            case CMD_FORKLIFT_DOWN:
                ret = "F_D";
                break;
            case CMD_STOP:
                ret = "STP";
                break;
            //-----------
            case VAR_COLOR_RED:
                ret = "CL_R";
                break;
            case VAR_COLOR_BLUE:
                ret = "CL_BL";
                break;
            case VAR_COLOR_GREEN:
                ret = "CL_G";
                break;
            case VAR_COLOR_YELLOW:
                ret = "CL_Y";
                break;
            case VAR_COLOR_WHITE:
                ret = "CL_W";
                break;
            case VAR_COLOR_BLACK:
                ret = "CL_BK";
                break;
            //-----------
            case TILE:
                ret = "TILE";
                break;
            case BOX:
                ret = "BOX";
                break;
            case FENCE:
                ret = "FN";
                break;
            default: //this is for "NaN" string
                ret = "NaN";
        }

        return ret;
    }

    protected QREnums stringToEnum (String val_str ){

        QREnums qrEnum;

        switch (val_str) {
            case "0":
                qrEnum = QREnums.VAR_0;
                break;
            case "1":
                qrEnum = QREnums.VAR_1;
                break;
            case "2":
                qrEnum = QREnums.VAR_2;
                break;
            case "3":
                qrEnum = QREnums.VAR_3;
                break;
            case "4":
                qrEnum = QREnums.VAR_4;
                break;
            case "5":
                qrEnum = QREnums.VAR_5;
                break;
            case "6":
                qrEnum = QREnums.VAR_6;
                break;
            case "7":
                qrEnum = QREnums.VAR_7;
                break;
            case "8":
                qrEnum = QREnums.VAR_8;
                break;
            case "9":
                qrEnum = QREnums.VAR_9;
                break;
            //-----------
            case "CND":
                qrEnum = QREnums.CONDITION;
                break;
            //-----------
            case "JMP_F1":
                qrEnum = QREnums.JMP_FROM_1;
                break;
            case "JMP_T1":
                qrEnum = QREnums.JMP_TO_1;
                break;
            case "JMP_F2":
                qrEnum = QREnums.JMP_FROM_2;
                break;
            case "JMP_T2":
                qrEnum = QREnums.JMP_TO_2;
                break;
            case "JMP_F3":
                qrEnum = QREnums.JMP_FROM_3;
                break;
            case "JMP_T3":
                qrEnum = QREnums.JMP_TO_3;
                break;
            //-----------
            case "T_L":
                qrEnum = QREnums.CMD_TURN_LEFT;
                break;
            case "T_R":
                qrEnum = QREnums.CMD_TURN_RIGHT;
                break;
            case "T_U":
                qrEnum = QREnums.CMD_TURN_AROUND;
                break;
            case "G_FW":
                qrEnum = QREnums.CMD_GO_FORWARD;
                break;
            case "G_BK":
                qrEnum = QREnums.CMD_GO_BACKWARD;
                break;
            case "F_U":
                qrEnum = QREnums.CMD_FORKLIFT_UP;
                break;
            case "F_D":
                qrEnum = QREnums.CMD_FORKLIFT_DOWN;
                break;
            case "STP":
                qrEnum = QREnums.CMD_STOP;
                break;
            //-----------
            case "CL_R":
                qrEnum = QREnums.VAR_COLOR_RED;
                break;
            case "CL_BL":
                qrEnum = QREnums.VAR_COLOR_BLUE;
                break;
            case "CL_G":
                qrEnum = QREnums.VAR_COLOR_GREEN;
                break;
            case "CL_Y":
                qrEnum = QREnums.VAR_COLOR_YELLOW;
                break;
            case "CL_W":
                qrEnum = QREnums.VAR_COLOR_WHITE;
                break;
            case "CL_BK":
                qrEnum = QREnums.VAR_COLOR_BLACK;
                break;
            //-----------
            case "TILE":
                qrEnum = QREnums.TILE;
                break;
            case "BOX":
                qrEnum = QREnums.BOX;
                break;
            case "FN":
                qrEnum = QREnums.FENCE;
                break;
            default: //this is for "NaN" string
                qrEnum = QREnums.NaN;
        }

        return qrEnum;
    }

    protected void setAllButtonsOfTable(ArrayList<QREnums> vals){
        QREnums qrEnums;
        String qr_str;
        for (int i = 0 ; i < 8; i++){
            for(int j = 0; j < 6 ; j++){
                qrEnums = vals.get(i*6+j);
                qr_str = enumToString(qrEnums);

                //this.table_buttons[i][j].setText(qr_str);
                this.table_buttons[i][j].setImageResource(chosenImage(qr_str, this.table_buttons[i][j]));
                // this.table_buttons[i][j].setBackground(pairing.png);
                // TODO : Set image and not text
            }
        }

    }

    public String getImageCode(DataImageButton draw){
        String qr_str = "0";
        return draw.getData();
//        if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.n0).getConstantState())){
//            qr_str = "0";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.n1).getConstantState())){
//            qr_str = "1";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.n2).getConstantState())){
//            qr_str = "2";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.n3).getConstantState())){
//            qr_str = "3";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.n4).getConstantState())){
//            qr_str = "4";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.n5).getConstantState())){
//            qr_str = "5";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.n6).getConstantState())){
//            qr_str = "6";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.n7).getConstantState())){
//            qr_str = "7";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.n8).getConstantState())){
//            qr_str = "8";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.n9).getConstantState())){
//            qr_str = "9";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.cnd).getConstantState())){
//            qr_str = "CND";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.jmp_f1).getConstantState())){
//            qr_str = "JMP_F1";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.jmp_f2).getConstantState())){
//            qr_str = "JMP_F2";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.jmp_f3).getConstantState())){
//            qr_str = "JMP_F3";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.box).getConstantState())){
//            qr_str = "BOX";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.cl_bk).getConstantState())){
//            qr_str = "CL_BK";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.cl_bl).getConstantState())){
//            qr_str = "CL_CL";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.cl_g).getConstantState())){
//            qr_str = "CL_G";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.cl_r).getConstantState())){
//            qr_str = "CL_R";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.cl_w).getConstantState())){
//            qr_str = "CL_W";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.cl_y).getConstantState())){
//            qr_str = "CL_Y";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.f_d).getConstantState())){
//            qr_str = "F_D";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.f_u).getConstantState())){
//            qr_str = "F_U";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.fn).getConstantState())){
//            qr_str = "FN";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.g_bk).getConstantState())){
//            qr_str = "G_BK";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.g_fw).getConstantState())){
//            qr_str = "G_FW";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.jmp_t1).getConstantState())){
//            qr_str = "JMP_T1";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.jmp_t2).getConstantState())){
//            qr_str = "JMP_T2";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.jmp_t3).getConstantState())){
//            qr_str = "JMP_T3";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.nan).getConstantState())){
//            qr_str = "NaN";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.stp).getConstantState())){
//            qr_str = "STP";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.t_l).getConstantState())){
//            qr_str = "T_L";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.t_r).getConstantState())){
//            qr_str = "T_R";
//        }
//        else if (draw.getConstantState().equals(getResources().getDrawable(R.drawable.t_u).getConstantState())){
//            qr_str = "T_U";
//        }
//        return qr_str;
    }

    protected ArrayList<QREnums> updateEnumAccordingToTable(){
        QREnums qrEnums;
        String qr_str;

        step1_result_code = "";

        for (int i = 0 ; i < 8; i++) {
            for(int j = 0; j < 6 ; j++) {
                qr_str = "0";
                Drawable draw = this.table_buttons[i][j].getDrawable();

                qr_str = getImageCode(this.table_buttons[i][j]);
                //qr_str = this.table_buttons[i][j].getText().toString();
                qrEnums = stringToEnum(qr_str);
                this.enums_in_table.set(i*6+j, qrEnums);
                step1_result_code = step1_result_code + qr_str + ( i == 7 && j == 5 ? "" : ",");
            }
        }

        return enums_in_table;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reacheck);

        this.table_buttons = new DataImageButton[8][6];
        setIdForTableButtons();

        this.done_button = findViewById(R.id.done_button);
        this.done_button.setOnClickListener(this);


        setListenersForTableButtons();

        Log.i("Info","Setting the buttons according to step1_result_code variable");
        this.enums_in_table = getQrEnumsFromString(step1_result_code);
        setAllButtonsOfTable(this.enums_in_table);
    }


    public int chosenImage(String choice, DataImageButton current_button){
        int draw = R.drawable.nan;
        current_button.setData(choice);
        if(choice.equals("0")){
            draw = R.drawable.n0;
        }
        else if(choice.equals("1")){
            draw = R.drawable.n1;
        }
        else if(choice.equals("2")){
            draw = R.drawable.n2;
        }
        else if(choice.equals("3")){
            draw = R.drawable.n3;
        }
        else if(choice.equals("4")){
            draw = R.drawable.n4;
        }
        else if(choice.equals("5")){
            draw = R.drawable.n5;
        }
        else if(choice.equals("6")){
            draw = R.drawable.n6;
        }
        else if(choice.equals("7")){
            draw = R.drawable.n7;
        }
        else if(choice.equals("8")){
            draw = R.drawable.n8;
        }
        else if(choice.equals("9")){
            draw =  R.drawable.n9;
        }
        else if(choice.equals("CND")){
            draw =  R.drawable.cnd;
        }
        else if(choice.equals("JMP_F1")){
            draw =  R.drawable.jmp_f1;
        }
        else if(choice.equals("JMP_T1")){
            draw =  R.drawable.jmp_t1;
        }
        else if(choice.equals("JMP_F2")){
            draw =  R.drawable.jmp_f2;
        }
        else if(choice.equals("JMP_T2")){
            draw =  R.drawable.jmp_t2;
        }
        else if(choice.equals("JMP_F3")){
            draw =  R.drawable.jmp_f3;
        }
        else if(choice.equals("JMP_T3")){
            draw =  R.drawable.jmp_t3;
        }
        else if(choice.equals("T_L")){
            draw =  R.drawable.t_l;
        }
        else if(choice.equals("T_R")){
            draw =  R.drawable.t_r;
        }
        else if(choice.equals("T_U")){
            draw =  R.drawable.t_u;
        }
        else if(choice.equals("G_FW")){
            draw =  R.drawable.g_fw;
        }
        else if(choice.equals("G_BK")){
            draw =  R.drawable.g_bk;
        }
        else if(choice.equals("F_U")){
            draw =  R.drawable.f_u;
        }
        else if(choice.equals("F_D")){
            draw =  R.drawable.f_d;
        }
        else if(choice.equals("STP")){
            draw =  R.drawable.stp;
        }
        else if(choice.equals("CL_R")){
            draw =  R.drawable.cl_r;
        }
        else if(choice.equals("CL_BL")){
            draw =  R.drawable.cl_bl;
        }
        else if(choice.equals("CL_BG")){
            draw =  R.drawable.cl_g;
        }
        else if(choice.equals("CL_Y")){
            draw =  R.drawable.cl_y;
        }
        else if(choice.equals("CL_W")){
            draw =  R.drawable.cl_w;
        }
        else if(choice.equals("CL_BK")){
            draw =  R.drawable.cl_bk;
        }
        else if(choice.equals("TILE")){
            draw =  R.drawable.tile;
        }
        else if(choice.equals("BOX")){
            draw =  R.drawable.box;
        }
        else if(choice.equals("FN")){
            draw =  R.drawable.fn;
        }
        else if(choice.equals("NaN")){
            draw =  R.drawable.nan;
        }
        return draw;
    }

    protected void showDialog(final int line, final int col){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ReacheckActivity.this);
        mBuilder.setTitle("Choose The Correct Card");
        mBuilder.setSingleChoiceItems(listCards, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Here we set the button appearance according to the choice.
                // table_buttons[line][col].setText(listCards[which]);
                String choice = listCards[which];
                table_buttons[line][col].setImageResource(chosenImage(choice, table_buttons[line][col]));
                dialog.dismiss();
            }
        });
        mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //show Alert dialog
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_button:
                Log.i("Info","Updating the ArrayList<QREnums> enums_in_table according the table,\n" +
                        " this is the Table:");
                updateEnumAccordingToTable();
                Log.i("Info",this.enums_in_table.toString());
                Log.i("Info",this.step1_result_code);
                get_reacheckSemaphore().release();
                ReacheckActivity.this.finish();
                break;

            // --- Row 0
            case R.id.DataImageButton00: showDialog(0,0);
                break;
            case R.id.DataImageButton01: showDialog(0,1);
                break;
            case R.id.DataImageButton02: showDialog(0,2);
                break;
            case R.id.DataImageButton03: showDialog(0,3);
                break;
            case R.id.DataImageButton04: showDialog(0,4);
                break;
            case R.id.DataImageButton05: showDialog(0,5);
                break;

            // --- Row 1
            case R.id.DataImageButton10: showDialog(1,0);
                break;
            case R.id.DataImageButton11: showDialog(1,1);
                break;
            case R.id.DataImageButton12: showDialog(1,2);
                break;
            case R.id.DataImageButton13: showDialog(1,3);
                break;
            case R.id.DataImageButton14: showDialog(1,4);
                break;
            case R.id.DataImageButton15: showDialog(1,5);
                break;

            // --- Row 2
            case R.id.DataImageButton20: showDialog(2,0);
                break;
            case R.id.DataImageButton21: showDialog(2,1);
                break;
            case R.id.DataImageButton22: showDialog(2,2);
                break;
            case R.id.DataImageButton23: showDialog(2,3);
                break;
            case R.id.DataImageButton24: showDialog(2,4);
                break;
            case R.id.DataImageButton25: showDialog(2,5);
                break;

            // --- Row 3
            case R.id.DataImageButton30: showDialog(3,0);
                break;
            case R.id.DataImageButton31: showDialog(3,1);
                break;
            case R.id.DataImageButton32: showDialog(3,2);
                break;
            case R.id.DataImageButton33: showDialog(3,3);
                break;
            case R.id.DataImageButton34: showDialog(3,4);
                break;
            case R.id.DataImageButton35: showDialog(3,5);
                break;

            // --- Row 4
            case R.id.DataImageButton40: showDialog(4,0);
                break;
            case R.id.DataImageButton41: showDialog(4,1);
                break;
            case R.id.DataImageButton42: showDialog(4,2);
                break;
            case R.id.DataImageButton43: showDialog(4,3);
                break;
            case R.id.DataImageButton44: showDialog(4,4);
                break;
            case R.id.DataImageButton45: showDialog(4,5);
                break;

            // --- Row 5
            case R.id.DataImageButton50: showDialog(5,0);
                break;
            case R.id.DataImageButton51: showDialog(5,1);
                break;
            case R.id.DataImageButton52: showDialog(5,2);
                break;
            case R.id.DataImageButton53: showDialog(5,3);
                break;
            case R.id.DataImageButton54: showDialog(5,4);
                break;
            case R.id.DataImageButton55: showDialog(5,5);
                break;

            // --- Row 6
            case R.id.DataImageButton60: showDialog(6,0);
                break;
            case R.id.DataImageButton61: showDialog(6,1);
                break;
            case R.id.DataImageButton62: showDialog(6,2);
                break;
            case R.id.DataImageButton63: showDialog(6,3);
                break;
            case R.id.DataImageButton64: showDialog(6,4);
                break;
            case R.id.DataImageButton65: showDialog(6,5);
                break;

            // --- Row 7
            case R.id.DataImageButton70: showDialog(7,0);
                break;
            case R.id.DataImageButton71: showDialog(7,1);
                break;
            case R.id.DataImageButton72: showDialog(7,2);
                break;
            case R.id.DataImageButton73: showDialog(7,3);
                break;
            case R.id.DataImageButton74: showDialog(7,4);
                break;
            case R.id.DataImageButton75: showDialog(7,5);
                break;
            default:
                Toast.makeText(this, "a Button was clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onPanelClosed(int featureId, @NonNull Menu menu) {
        super.onPanelClosed(featureId, menu);
        // here release semaphore
    }
}