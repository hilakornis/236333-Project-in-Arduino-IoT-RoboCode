package com.example.a236333_hw3.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.a236333_hw3.R;
import com.example.a236333_hw3.RunEnvironment.Compiler.DataToEnumsConverter;
import com.example.a236333_hw3.RunEnvironment.Compiler.QREnums;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ReacheckActivity extends AppCompatActivity implements View.OnClickListener {

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



    private Button[][]  table_buttons;

    private Button done_button;

    private ArrayList<QREnums> enums_in_table ;

    private String[] listCards = new String[]{
            "0", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "CND", "JMP_F1", "JMP_T1",
            "JMP_F2", "JMP_T2", "JMP_F3", "JMP_T3", "T_L", "T_R",
            "T_U", "G_FW", "G_BK", "F_U", "F_D", "STP",
            "CL_R", "CL_BL", "CL_G", "CL_Y", "CL_W", "CL_BK",
            "TILE", "BOX", "FN"};

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
        this.table_buttons[0][0] =  findViewById(R.id.button00);
        this.table_buttons[0][1] =  findViewById(R.id.button01);
        this.table_buttons[0][2] =  findViewById(R.id.button02);
        this.table_buttons[0][3] =  findViewById(R.id.button03);
        this.table_buttons[0][4] =  findViewById(R.id.button04);
        this.table_buttons[0][5] =  findViewById(R.id.button05);

        // --- Row 1
        this.table_buttons[1][0] =  findViewById(R.id.button10);
        this.table_buttons[1][1] =  findViewById(R.id.button11);
        this.table_buttons[1][2] =  findViewById(R.id.button12);
        this.table_buttons[1][3] =  findViewById(R.id.button13);
        this.table_buttons[1][4] =  findViewById(R.id.button14);
        this.table_buttons[1][5] =  findViewById(R.id.button15);

        // --- Row 2
        this.table_buttons[2][0] =  findViewById(R.id.button20);
        this.table_buttons[2][1] =  findViewById(R.id.button21);
        this.table_buttons[2][2] =  findViewById(R.id.button22);
        this.table_buttons[2][3] =  findViewById(R.id.button23);
        this.table_buttons[2][4] =  findViewById(R.id.button24);
        this.table_buttons[2][5] =  findViewById(R.id.button25);

        // --- Row 3
        this.table_buttons[3][0] =  findViewById(R.id.button30);
        this.table_buttons[3][1] =  findViewById(R.id.button31);
        this.table_buttons[3][2] =  findViewById(R.id.button32);
        this.table_buttons[3][3] =  findViewById(R.id.button33);
        this.table_buttons[3][4] =  findViewById(R.id.button34);
        this.table_buttons[3][5] =  findViewById(R.id.button35);

        // --- Row 4
        this.table_buttons[4][0] =  findViewById(R.id.button40);
        this.table_buttons[4][1] =  findViewById(R.id.button41);
        this.table_buttons[4][2] =  findViewById(R.id.button42);
        this.table_buttons[4][3] =  findViewById(R.id.button43);
        this.table_buttons[4][4] =  findViewById(R.id.button44);
        this.table_buttons[4][5] =  findViewById(R.id.button45);


        // --- Row 5
        this.table_buttons[5][0] =  findViewById(R.id.button50);
        this.table_buttons[5][1] =  findViewById(R.id.button51);
        this.table_buttons[5][2] =  findViewById(R.id.button52);
        this.table_buttons[5][3] =  findViewById(R.id.button53);
        this.table_buttons[5][4] =  findViewById(R.id.button54);
        this.table_buttons[5][5] =  findViewById(R.id.button55);

        // --- Row 6
        this.table_buttons[6][0] =  findViewById(R.id.button60);
        this.table_buttons[6][1] =  findViewById(R.id.button61);
        this.table_buttons[6][2] =  findViewById(R.id.button62);
        this.table_buttons[6][3] =  findViewById(R.id.button63);
        this.table_buttons[6][4] =  findViewById(R.id.button64);
        this.table_buttons[6][5] =  findViewById(R.id.button65);

        // --- Row 7
        this.table_buttons[7][0] =  findViewById(R.id.button70);
        this.table_buttons[7][1] =  findViewById(R.id.button71);
        this.table_buttons[7][2] =  findViewById(R.id.button72);
        this.table_buttons[7][3] =  findViewById(R.id.button73);
        this.table_buttons[7][4] =  findViewById(R.id.button74);
        this.table_buttons[7][5] =  findViewById(R.id.button75);

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
                this.table_buttons[i][j].setText(qr_str);
                // TODO : Set image and not text
            }
        }

    }

    protected ArrayList<QREnums> updateEnumAccordingToTable(){
        QREnums qrEnums;
        String qr_str;

        step1_result_code = "";

        for (int i = 0 ; i < 8; i++) {
            for(int j = 0; j < 6 ; j++) {
                qr_str = this.table_buttons[i][j].getText().toString();
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

        this.table_buttons = new Button[8][6];
        setIdForTableButtons();

        this.done_button = findViewById(R.id.done_button);
        this.done_button.setOnClickListener(this);


        setListenersForTableButtons();

        Log.i("Info","Setting the buttons according to step1_result_code variable");
        this.enums_in_table = getQrEnumsFromString(step1_result_code);
        setAllButtonsOfTable(this.enums_in_table);




    }

    protected void showDialog(final int line, final int col){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ReacheckActivity.this);
        mBuilder.setTitle("Choose The Correct Card");
        mBuilder.setSingleChoiceItems(listCards, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Here we set the button appearance according to the choice.
                table_buttons[line][col].setText(listCards[which]);
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
            case R.id.button00: showDialog(0,0);
                break;
            case R.id.button01: showDialog(0,1);
                break;
            case R.id.button02: showDialog(0,2);
                break;
            case R.id.button03: showDialog(0,3);
                break;
            case R.id.button04: showDialog(0,4);
                break;
            case R.id.button05: showDialog(0,5);
                break;

            // --- Row 1
            case R.id.button10: showDialog(1,0);
                break;
            case R.id.button11: showDialog(1,1);
                break;
            case R.id.button12: showDialog(1,2);
                break;
            case R.id.button13: showDialog(1,3);
                break;
            case R.id.button14: showDialog(1,4);
                break;
            case R.id.button15: showDialog(1,5);
                break;

            // --- Row 2
            case R.id.button20: showDialog(2,0);
                break;
            case R.id.button21: showDialog(2,1);
                break;
            case R.id.button22: showDialog(2,2);
                break;
            case R.id.button23: showDialog(2,3);
                break;
            case R.id.button24: showDialog(2,4);
                break;
            case R.id.button25: showDialog(2,5);
                break;

            // --- Row 3
            case R.id.button30: showDialog(3,0);
                break;
            case R.id.button31: showDialog(3,1);
                break;
            case R.id.button32: showDialog(3,2);
                break;
            case R.id.button33: showDialog(3,3);
                break;
            case R.id.button34: showDialog(3,4);
                break;
            case R.id.button35: showDialog(3,5);
                break;

            // --- Row 4
            case R.id.button40: showDialog(4,0);
                break;
            case R.id.button41: showDialog(4,1);
                break;
            case R.id.button42: showDialog(4,2);
                break;
            case R.id.button43: showDialog(4,3);
                break;
            case R.id.button44: showDialog(4,4);
                break;
            case R.id.button45: showDialog(4,5);
                break;

            // --- Row 5
            case R.id.button50: showDialog(5,0);
                break;
            case R.id.button51: showDialog(5,1);
                break;
            case R.id.button52: showDialog(5,2);
                break;
            case R.id.button53: showDialog(5,3);
                break;
            case R.id.button54: showDialog(5,4);
                break;
            case R.id.button55: showDialog(5,5);
                break;

            // --- Row 6
            case R.id.button60: showDialog(6,0);
                break;
            case R.id.button61: showDialog(6,1);
                break;
            case R.id.button62: showDialog(6,2);
                break;
            case R.id.button63: showDialog(6,3);
                break;
            case R.id.button64: showDialog(6,4);
                break;
            case R.id.button65: showDialog(6,5);
                break;

            // --- Row 7
            case R.id.button70: showDialog(7,0);
                break;
            case R.id.button71: showDialog(7,1);
                break;
            case R.id.button72: showDialog(7,2);
                break;
            case R.id.button73: showDialog(7,3);
                break;
            case R.id.button74: showDialog(7,4);
                break;
            case R.id.button75: showDialog(7,5);
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