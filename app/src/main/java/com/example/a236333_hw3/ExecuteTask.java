package com.example.a236333_hw3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a236333_hw3.RunEnvironment.Compiler.RCCompiler;
import com.example.a236333_hw3.RunEnvironment.Compiler.RCCompilerException;
import com.example.a236333_hw3.RunEnvironment.Executor.RCProgramExecutor;
import com.example.a236333_hw3.RunEnvironment.Program.RCProgram;
import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.example.a236333_hw3.ui.ReacheckActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ExecuteTask extends AppCompatActivity {


    private int arrived = 0;
    ArrayList<String> arrivals;

    LinearLayout Step1_cloud, Step2_compile, Step3_run, Step4_success, Step4_fail;
    Button backButton;
    TextView errorText;
    //TextView r1, r2, r3, r4, r5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_task);

        // register to receive notifications and capture requests
        LocalBroadcastManager.getInstance(ExecuteTask.this).
                registerReceiver(messageHandler,
                        new IntentFilter("com.example.a236333_hw3_CaptureMessage"));

        arrived = 0;
        arrivals = new ArrayList<String>();

        Step1_cloud = findViewById(R.id.Step1Panel);
        Step2_compile = findViewById(R.id.Step2Panel);
        Step3_run = findViewById(R.id.Step3Panel);
        Step4_success = findViewById(R.id.Step4SucPanel);
        Step4_fail = findViewById(R.id.StepFailPanel);
        backButton = findViewById(R.id.backToTasksButton);
        errorText = findViewById(R.id.hint_textView);
        /*r1 = findViewById(R.id.res_1);
        r2 = findViewById(R.id.res_2);
        r3 = findViewById(R.id.res_3);
        r4 = findViewById(R.id.res_4);
        r5 = findViewById(R.id.res_5);*/
        Step1_cloud.setVisibility(View.VISIBLE);
        Step2_compile.setVisibility(View.GONE);
        Step3_run.setVisibility(View.GONE);
        Step4_success.setVisibility(View.GONE);
        Step4_fail.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecuteTask.this.finish();
            }
        });

        // TODO : retrive this when working with firebase server!!!
        //FirebaseMessaging.getInstance().subscribeToTopic(RoboCodeSettings.getInstance().currentAnswerTopic);
        Step1_over(); // TODO : Remove this !!
    }

    // Capture Service ============================================================================
    private BroadcastReceiver messageHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //String userId = intent.getExtras().get("title").toString();
            String data = intent.getExtras().get("message").toString();
            showToast(data);
            arrivals.add(data);
            arrived++;
            showToast(arrived + ": " + data);

            if (arrived >= RoboCodeSettings.NUM_OF_CAPTURES) {
                try {
                    FirebaseMessaging.getInstance().
                            unsubscribeFromTopic(RoboCodeSettings.getInstance().currentAnswerTopic);
                    } catch (Exception ex) {
                }

                ExecuteTask.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Step1_cloud.setVisibility(View.GONE);
                        Step2_compile.setVisibility(View.VISIBLE);
                        /*r1.setText(arrivals.get(0));
                        r2.setText(arrivals.get(1));
                        r3.setText(arrivals.get(2));
                        r4.setText(arrivals.get(3));
                        r5.setText(arrivals.get(4));*/
                    }
                });

                // TOTDO : merge all results, save under step1_result_code and call Step1_over();
                // step1_result_code = ...
                Step1_over();
            }
        }
    };

    // ============================================================================================
    // here the output of step 1 is saved
    //private String step1_result_code =  "NaN,"      +     "NaN,"  +   "JMP_T1,"+  "CND,"+     "BOX,"+       "CL_R,"  +
    //                                    "NaN,"      +     "NaN,"  +   "T_R,"+     "NaN,"+     "F_U,"+       "NaN,"  +
    //                                    "NaN,"      +     "NaN,"  +   "CND,"+     "FN,"+      "NaN,"+       "NaN,"  +
    //                                    "JMP_T2,"   +     "G_FW,"  +  "NaN,"+     "JMP_T3,"+  "T_L,"+       "NaN," +
    //                                    "JMP_F1,"+        "NaN,"+     "NaN,"+     "CND,"+     "FN," +       "NaN,"      +
    //                                    "NaN,"+           "NaN,"+     "JMP_F2,"+  "NaN,"+     "JMP_F3,"  +  "NaN,"      +
    //                                    "NaN,"      +     "NaN,"+     "NaN,"+     "NaN,"+     "NaN,"+       "NaN,"  +
    //                                    "NaN,"      +     "NaN,"+     "NaN,"+     "NaN,"+     "NaN,"+       "NaN";

    private String step1_result_code =
            "JMP_T1,"   +     "G_FW,"   +   "2,"      +  "NaN,"   +   "NaN,"  +       "NaN,"  +
            "T_U,"      +     "NaN,"    +   "NaN,"    +  "NaN,"   +   "NaN,"  +       "NaN,"  +
            "JMP_F1,"   +     "2,"      +   "NaN,"    +  "NaN,"   +   "NaN,"  +       "NaN,"  +
            "T_L,"      +     "NaN,"    +   "NaN,"    +  "NaN,"   +   "NaN,"  +       "NaN,"  +
            "G_FW,"     +     "2,"      +   "NaN,"    +  "NaN,"  +    "NaN,"  +       "NaN,"  +
            "T_U,"      +     "NaN,"    +   "NaN,"    +  "NaN,"   +   "NaN,"  +       "NaN,"  +
            "NaN,"      +     "NaN,"    +   "NaN,"    +  "NaN,"   +   "NaN,"  +       "NaN,"  +
            "NaN,"      +     "NaN,"    +   "NaN,"    +  "NaN,"   +   "NaN,"  +       "NaN"  ;


    private void Step1_over() {
        Thread d = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO - its a fake sleep, remove this!
                /*try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                // calling debug screen - set the text for buttons
                ReacheckActivity.step1_result_code = step1_result_code;

                // call the activity
                ExecuteTask.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(ExecuteTask.this, ReacheckActivity.class));
                    }
                });

                try {
                    ReacheckActivity.get_reacheckSemaphore().acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // end debug screen - get fixed result
                step1_result_code = ReacheckActivity.step1_result_code;


                ExecuteTask.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ExecuteTask.this.Step1_cloud.setVisibility(View.GONE);
                        ExecuteTask.this.Step2_compile.setVisibility(View.VISIBLE);
                    }
                });

                try {

                   ExecuteTask.this.step2_compiledProgram =
                            RCCompiler.getInstance().Compile(ExecuteTask.this.step1_result_code);

                    // TODO - its a fake sleep, remove this!
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Step2_over();

                } catch (RCCompilerException e) {
                    e.printStackTrace();
                    errorMsg = "Compilation falied.\n" +
                               "try to look ar row " + e.getRowId() + "cell number " + e.getColId() + "\n" + e.getMessage();

                    Do_fail();

                }
            }
        });
        d.start();
    }

    // ============================================================================================
    // here the output of step 2 is saved - the actual program to run!
    RCProgram step2_compiledProgram;

    private void Step2_over() {
        Thread d = new Thread(new Runnable() {
            @Override
            public void run() {
                ExecuteTask.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ExecuteTask.this.Step2_compile.setVisibility(View.GONE);
                        ExecuteTask.this.Step3_run.setVisibility(View.VISIBLE);
                    }
                });

                if (RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector() == null) {
                    errorMsg = "Bluetooth connection is not defined - you cant solve the task without connecting to RoboCode...";
                } else if (!RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector().isConnected()) {
                    errorMsg = "Connection to RoboCode is lost... you need him in order to solve the task";
                } else {
                    // TODO : add run parameters according to task
                    RCProgramExecutor.getInstance().
                        runProgram(step2_compiledProgram,
                            RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector(),
                            RCProgramExecutor.NO_STEPS_LIMIT);

                    try {
                        RCProgramExecutor.getInstance().getSemaphoreForEndOfExecution().acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (RCProgramExecutor.getInstance().getErrorMessage().isEmpty()) {
                        // success
                        Do_success();
                    } else {
                        // error
                        errorMsg = RCProgramExecutor.getInstance().getErrorMessage();
                        Do_fail();
                    }
                }
            }
        });
        d.start();
    }

    // ============================================================================================
    // success

    private void Do_success() {
        // TODO : save on firebase that user solved the current task

        Thread d = new Thread(new Runnable() {
            @Override
            public void run() {
                ExecuteTask.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ExecuteTask.this.Step1_cloud.setVisibility(View.GONE);
                        ExecuteTask.this.Step2_compile.setVisibility(View.GONE);
                        ExecuteTask.this.Step3_run.setVisibility(View.GONE);
                        ExecuteTask.this.Step4_fail.setVisibility(View.GONE);
                        ExecuteTask.this.Step4_success.setVisibility(View.VISIBLE);
                        ExecuteTask.this.backButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        d.start();
    }

    // ============================================================================================
    // error
    private String errorMsg;

    private void Do_fail() {
        Thread d = new Thread(new Runnable() {
            @Override
            public void run() {
                ExecuteTask.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ExecuteTask.this.Step1_cloud.setVisibility(View.GONE);
                        ExecuteTask.this.Step2_compile.setVisibility(View.GONE);
                        ExecuteTask.this.Step3_run.setVisibility(View.GONE);
                        ExecuteTask.this.Step4_success.setVisibility(View.GONE);
                        ExecuteTask.this.Step4_fail.setVisibility(View.VISIBLE);
                        ExecuteTask.this.backButton.setVisibility(View.VISIBLE);
                        ExecuteTask.this.errorText.setText(errorMsg);
                    }
                });
            }
        });
        d.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            FirebaseMessaging.getInstance().
                    unsubscribeFromTopic(RoboCodeSettings.getInstance().currentAnswerTopic);
        } catch (Exception ex) {}
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void showToast(final String text) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
