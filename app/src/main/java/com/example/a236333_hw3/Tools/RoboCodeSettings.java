package com.example.a236333_hw3.Tools;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class RoboCodeSettings {

    public static final int NUM_OF_CAPTURES = 1;
    public static boolean USE_CLUDE = false;

    private static RoboCodeSettings _inst;
    private FirebaseFirestore db;


    // Fields
    public FirebaseUser user;
    public String userNickname;
    public List<roboCodeTask> roboCodeTasks;

    // paring code
    public String a, b, c, d;
    public Boolean hasPairingCode;

    // current task
    public roboCodeTask current;
    public String       currentAnswerTopic;

    // connection to robot
    private ArduinoConnector roboCodeBluetoothConnector;

    public ArduinoConnector getRoboCodeBluetoothConnector() {
        return roboCodeBluetoothConnector;
    }

    public void setRoboCodeBluetoothConnector(ArduinoConnector roboCodeBluetoothConnector) {
        this.roboCodeBluetoothConnector = roboCodeBluetoothConnector;
    }

    public static RoboCodeSettings getInstance() {
        if (_inst == null) _inst = new RoboCodeSettings();
        return _inst;
    }

    private RoboCodeSettings() {
        db = FirebaseFirestore.getInstance();
        roboCodeTasks = null;
        userNickname = null;
        hasPairingCode = false;
//        roboCodeUsers = null;
    }

    public void getUserDataAsync(final SuccessFailureHandler handler) {
        if (userNickname != null) {
            handler.onSuccess();
        } else {
            DocumentReference tasks = db.collection("Users").document(user.getEmail());

            tasks.get().addOnSuccessListener(
                    new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                userNickname = documentSnapshot.getString("Name");
                            }
                            handler.onSuccess();
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           handler.onFailure();
                                       }
                                   }
            );
        }
    }

    public void getRoboCodeTasksAsync(final SuccessFailureHandler handler) {
        if (roboCodeTasks != null) {
            handler.onSuccess();
        } else {
            DocumentReference getUser = FirebaseFirestore.getInstance().collection("Users").
                    document(RoboCodeSettings.getInstance().user.getEmail());

            getUser.get().addOnSuccessListener(
                    new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            final Map<String,Integer> FinishedTasks =
                                    (Map<String,Integer>)documentSnapshot.get("FinishedTasks");

                            CollectionReference tasks = db.collection("Tasks");

                            tasks.get().addOnSuccessListener(
                                    new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot documentSnapshots) {
                                            roboCodeTasks = new Vector<roboCodeTask>();

                                            if (!documentSnapshots.isEmpty()) {
                                                for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                                    if (documentSnapshot.exists()) {
                                                        roboCodeTask task = new roboCodeTask();
                                                        task.ID             = documentSnapshot.getLong("ID").intValue();
                                                        task.Points         = documentSnapshot.getLong("Points").intValue();
                                                        task.Title          = documentSnapshot.getString("Title");
                                                        task.Description    = documentSnapshot.getString("Description");
                                                        task.Hints          = documentSnapshot.getString("Hints");
                                                        task.Arrangement    = documentSnapshot.getString("Arrangement");
                                                        task.Active         = documentSnapshot.getBoolean("Active");
                                                        task.UseCarpet      = documentSnapshot.getBoolean("useCarpet");

                                                        task.checkCond       = documentSnapshot.getBoolean("CheckCond");
                                                        task.checkCondValue  = documentSnapshot.getString("CheckCondValue");
                                                        task.checkExact      = documentSnapshot.getBoolean("CheckExact");
                                                        task.checkExactValue = documentSnapshot.getString("CheckExactValue");
                                                        task.stepsLimit      = documentSnapshot.getLong("StepsLimit").intValue();

                                                        task.FillFenceColors(documentSnapshot.getString("FenceColors"));

                                                        // Set Accomplished
                                                        task.Accomplished   = FinishedTasks.containsKey(String.valueOf(task.ID));

                                                        roboCodeTasks.add(task);
                                                    }
                                                }
                                            }
                                            /*roboCodeTasks = getTasksHardCoded();*/
                                            handler.onSuccess();
                                        }
                                    }
                            ).addOnFailureListener(new OnFailureListener() {
                                                       @Override
                                                       public void onFailure(@NonNull Exception e) {
                                                           handler.onFailure();
                                                       }
                                                   }
                            );

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        handler.onFailure();
                                                                    }
                                                                }
                     );


        }
    }
}

