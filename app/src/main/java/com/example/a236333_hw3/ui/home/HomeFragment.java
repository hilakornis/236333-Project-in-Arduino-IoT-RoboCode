package com.example.a236333_hw3.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.a236333_hw3.ArduinoConnector.ArduinoConnector;
import com.example.a236333_hw3.R;
import com.example.a236333_hw3.RunEnvironment.Compiler.EnumsToCommandConverter;
import com.example.a236333_hw3.RunEnvironment.Compiler.QREnums;
import com.example.a236333_hw3.RunEnvironment.Compiler.RCCompiler;
import com.example.a236333_hw3.RunEnvironment.Compiler.RCCompilerException;
import com.example.a236333_hw3.RunEnvironment.Executor.RCProgramExecutor;
import com.example.a236333_hw3.RunEnvironment.Program.RCProgram;
import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.example.a236333_hw3.Tools.SuccessFailureHandler;



public class HomeFragment extends Fragment {

    TextView userNicknameTextView;
    LinearLayout homeFragment_finishLoadLayout, homeFragment_doLoadLayout;

    // Bluetooth code ----------------------------------------------------------------------------->
    final String CMD_FORWARDS    = "1";
    final String CMD_BACKWARDS   = "2";
    final String CMD_LEFT        = "3";
    final String CMD_RIGHT       = "4";
    final String CMD_U_TURN      = "5";
    final String CMD_FORK_UP     = "6";
    final String CMD_FORK_DOWN   = "7";

    Button backwardsBtn;
    Button forwardsBtn;
    Button leftBtn;
    Button rightBtn;
    Button uTurnBtn;
    Button forkliftUpBtn;
    Button forkliftDownBtn;
    Button testBtn;
    Button cloud;


    // Bluetooth code <-----------------------------------------------------------------------------

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        userNicknameTextView = v.findViewById(R.id.homeFragment_userIdTextView);
        homeFragment_finishLoadLayout = v.findViewById(R.id.homeFragment_finishLoadLayout);
        homeFragment_doLoadLayout = v.findViewById(R.id.homeFragment_doLoadLayout);

        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        homeFragment_finishLoadLayout.setVisibility(View.GONE);
                        homeFragment_doLoadLayout.setVisibility(View.VISIBLE);
                    }
                }
        );

        RoboCodeSettings.getInstance().getUserDataAsync(new HomeFragment.HandleTaskReqResult());

        // Bluetooth code ------------------------------------------------------------------------->
        Log.i("[BLUETOOTH]", "Creating listeners");

        backwardsBtn    = v.findViewById(R.id.homeFragment_bluetoothBackwards_NS);
        forwardsBtn     = v.findViewById(R.id.homeFragment_bluetoothForwards_NS);
        leftBtn         = v.findViewById(R.id.homeFragment_bluetoothLeft_NS);
        rightBtn        = v.findViewById(R.id.homeFragment_bluetoothRight_NS);
        uTurnBtn        = v.findViewById(R.id.homeFragment_bluetoothUTurn_NS);
        forkliftUpBtn   = v.findViewById(R.id.homeFragment_bluetoothLiftUp_NS);
        forkliftDownBtn = v.findViewById(R.id.homeFragment_bluetoothLiftDown_NS);
        testBtn         = v.findViewById(R.id.homeFragment_runTest);
        cloud           = v.findViewById((R.id.homeFragment_cloud_btn));

        if (RoboCodeSettings.USE_CLUDE == false) {
            cloud.setText("stand-alone mode on");
        } else {
            cloud.setText("stand-alone mode off");
        }

        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RoboCodeSettings.USE_CLUDE == false) {
                    RoboCodeSettings.USE_CLUDE = true;
                    cloud.setText("stand-alone mode on");
                } else {
                    RoboCodeSettings.USE_CLUDE = false;
                    cloud.setText("stand-alone mode off");
                }

            }
        });

        if (RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector() == null) {
            RoboCodeSettings.getInstance().setRoboCodeBluetoothConnector(new ArduinoConnector());
            RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector().connectBlutooth(this);
        }

        backwardsBtn     .setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector().trySendData(CMD_FORWARDS ); } });
        forwardsBtn      .setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector().trySendData(CMD_BACKWARDS); } });
        leftBtn          .setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector().trySendData(CMD_LEFT     ); } });
        rightBtn         .setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector().trySendData(CMD_RIGHT    ); } });
        uTurnBtn         .setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector().trySendData(CMD_U_TURN   ); } });
        forkliftUpBtn    .setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector().trySendData(CMD_FORK_UP  ); } });
        forkliftDownBtn  .setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector().trySendData(CMD_FORK_DOWN); } });
        testBtn          .setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    testBtn.setEnabled(false);
                                }
                            });

                            RCProgram commands = RCCompiler.getInstance().Compile("T_L,NaN,NaN,NaN,NaN,NaN,T_R,NaN,NaN,NaN,NaN,NaN,T_U,NaN,NaN,NaN,NaN,NaN,G_FW,NaN,NaN,NaN,NaN,NaN,G_BK,NaN,NaN,NaN,NaN,NaN,F_U,NaN,NaN,NaN,NaN,NaN,F_D,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN");
                            Log.i("[home fragment]", commands.toString());
                            RCProgramExecutor.getInstance().runProgram(commands,
                                    RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector(),
                                    (RoboCodeSettings.getInstance().current.stepsLimit != -1 ?
                                            RoboCodeSettings.getInstance().current.stepsLimit :
                                            RCProgramExecutor.NO_STEPS_LIMIT));
                        }  catch (RCCompilerException ex) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    testBtn.setEnabled(true);
                                }
                            });
                        }
                    }
                });

        // Bluetooth code <-------------------------------------------------------------------------

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ArduinoConnector.REQUEST_ENABLE_BT) {
            showToast("resultCode is " + resultCode);
            RoboCodeSettings.getInstance().getRoboCodeBluetoothConnector().connectBlutooth(this);
        }
    }

    private void showToast(final String text) {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private class HandleTaskReqResult implements SuccessFailureHandler {
        @Override
        public void onSuccess() {
            getActivity().runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            userNicknameTextView.setText(RoboCodeSettings.getInstance().userNickname);
                            homeFragment_finishLoadLayout.setVisibility(View.VISIBLE);
                            homeFragment_doLoadLayout.setVisibility(View.GONE);
                        }
                    }
            );
        }

        @Override
        public void onFailure() {
            // TODO - hanldle
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
