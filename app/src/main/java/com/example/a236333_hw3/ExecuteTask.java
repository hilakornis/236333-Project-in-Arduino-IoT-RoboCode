package com.example.a236333_hw3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

        if (RoboCodeSettings.USE_CLUDE) {
            FirebaseMessaging.getInstance().subscribeToTopic(RoboCodeSettings.getInstance().currentAnswerTopic);
        } else {
            if (RoboCodeSettings.getInstance().current.ID == 1)
                step1_result_code = id1_result_code;
            else if (RoboCodeSettings.getInstance().current.ID == 4)
                step1_result_code = id4_result_code;
            else if (RoboCodeSettings.getInstance().current.ID == 5)
                step1_result_code = id5_result_code;
            else //if (RoboCodeSettings.getInstance().current.ID == 6)
                step1_result_code = id6_result_code;

            Step1_over();
        }
    }

    // Capture Service ============================================================================
    private BroadcastReceiver messageHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //String userId = intent.getExtras().get("title").toString();
            String data = intent.getExtras().get("message").toString();
            //showToast(data);
            arrivals.add(data);
            arrived++;
            //showToast(arrived + ": " + data);

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

                // TODO : merge all results, save under step1_result_code and call Step1_over();
                step1_result_code = arrivals.get(0);
                Step1_over();
            }
        }
    };

    // ============================================================================================
    // here the output of step 1 is saved
    private String step1_result_code;

    // horizon
    private String id6_result_code =
            "NaN,"      +     "JMP_T1," +   "CND,"  +  "BOX,"     +     "CL_R,"   +       "NaN,"  +
            "NaN,"      +     "G_FW,"   +   "NaN,"  +  "F_U,"     +     "NaN,"    +       "NaN,"  +
            "NaN,"      +     "JMP_F1," +   "NaN,"  +  "NaN,"     +     "NaN,"    +       "NaN,"  +
            "NaN,"      +     "NaN,"    +   "NaN,"  +  "NaN,"     +     "NaN,"    +       "NaN,"  +
            "NaN,"      +     "NaN,"    +   "NaN,"  +  "NaN,"     +     "NaN,"    +       "NaN,"  +
            "NaN,"      +     "NaN,"    +   "NaN,"  +  "NaN,"     +     "NaN,"    +       "NaN,"  +
            "NaN,"      +     "NaN,"    +   "NaN,"  +  "NaN,"     +     "NaN,"    +       "NaN,"  +
            "NaN,"      +     "NaN,"    +   "NaN,"  +  "NaN,"     +     "NaN,"    +       "NaN";


    // The floor is lava
    private String id5_result_code =  "NaN,"      +     "JMP_T1,"  +   "CND,"+  "TILE,"+     "CL_BL,"+       "NaN,"  +
                                        "NaN,"      +     "STP,"+     "NaN,"+     "G_FW,"+     "NaN,"+       "NaN,"  +
                                        "NaN,"      +     "NaN,"+     "NaN,"+     "JMP_F1,"+     "NaN,"+       "NaN,"  +
                                        "NaN,"      +     "NaN,"+     "NaN,"+     "NaN,"+     "NaN,"+       "NaN,"  +
                                        "NaN,"      +     "NaN,"+     "NaN,"+     "NaN,"+     "NaN,"+       "NaN,"  +
                                        "NaN,"      +     "NaN,"+     "NaN,"+     "NaN,"+     "NaN,"+       "NaN,"  +
                                        "NaN,"      +     "NaN,"+     "NaN,"+     "NaN,"+     "NaN,"+       "NaN,"  +
                                        "NaN,"      +     "NaN,"+     "NaN,"+     "NaN,"+     "NaN,"+       "NaN";

    // Solve a maze
   private String id4_result_code =  "NaN,"      +     "NaN,"  +   "JMP_T1,"+  "CND,"+     "BOX,"+       "CL_R,"  +
                                       "NaN,"      +     "NaN,"  +   "T_R,"+     "NaN,"+     "F_U,"+       "NaN,"  +
                                       "NaN,"      +     "NaN,"  +   "CND,"+     "FN,"+      "NaN,"+       "NaN,"  +
                                       "JMP_T2,"   +     "G_FW,"  +  "NaN,"+     "JMP_T3,"+  "T_L,"+       "NaN," +
                                       "JMP_F1,"+        "NaN,"+     "NaN,"+     "CND,"+     "FN," +       "NaN,"      +
                                       "NaN,"+           "NaN,"+     "JMP_F2,"+  "NaN,"+     "JMP_F3,"  +  "NaN,"      +
                                       "NaN,"      +     "NaN,"+     "NaN,"+     "NaN,"+     "NaN,"+       "NaN,"  +
                                       "NaN,"      +     "NaN,"+     "NaN,"+     "NaN,"+     "NaN,"+       "NaN";

    private String id1_result_code =
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
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Step2_over();

                } catch (RCCompilerException e) {
                    e.printStackTrace();
                    errorMsg = "Compilation falied.\n" +
                               "try to look at row " + e.getRowId() +1+ " cell number " + e.getColId() +1+ ".\n" + e.getMessage();

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
                            RoboCodeSettings.getInstance().current.stepsLimit == RCProgramExecutor.NO_STEPS_LIMIT ?
                                    RCProgramExecutor.NO_STEPS_LIMIT :
                                    RoboCodeSettings.getInstance().current.stepsLimit );

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
        DocumentReference tasks = FirebaseFirestore.getInstance().collection("Users").
                document(RoboCodeSettings.getInstance().user.getEmail());

        tasks.get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String,Integer> FinishedTasks =
                                    (Map<String,Integer>)documentSnapshot.get("FinishedTasks");
                            int Grade = documentSnapshot.getLong("Grade").intValue();

                            FinishedTasks.put(String.valueOf(RoboCodeSettings.getInstance().current.ID),
                                         (RoboCodeSettings.getInstance().current.ID));
                            Grade += RoboCodeSettings.getInstance().current.Points;

                            Map<String,Object> user = new HashMap<>();

                            user.put("Name",RoboCodeSettings.getInstance().userNickname);
                            user.put("UID",RoboCodeSettings.getInstance().user.getUid());
                            user.put("Grade",Grade);
                            user.put("FinishedTasks",FinishedTasks);

                            FirebaseFirestore.getInstance().collection("Users")
                                    .document(RoboCodeSettings.getInstance().user.getEmail())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.w("Execute Activity", "Update Score: success");
                                            RoboCodeSettings.getInstance().current.Accomplished = true;
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Execute Activity", "Update Score: failure", e);
                                        }
                                    });
                        }
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {    }
                               });


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
                        errorText.setVisibility(View.VISIBLE);
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
