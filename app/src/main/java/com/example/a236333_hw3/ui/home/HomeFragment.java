package com.example.a236333_hw3.ui.home;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import com.example.a236333_hw3.Bluetooth.ConnectedThread;
import com.example.a236333_hw3.R;
import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.example.a236333_hw3.Tools.SuccessFailureHandler;

import java.io.IOException;
import java.util.UUID;


public class HomeFragment extends Fragment {

    TextView userNicknameTextView;
    LinearLayout homeFragment_finishLoadLayout, homeFragment_doLoadLayout;

    // Bluetooth code ----------------------------------------------------------------------------->
    final String ON = "1";
    final String OFF = "1";
    final String FORWARDS  = "1";
    final String BACKWARDS  = "2";
    final String RIGHT      = "3";
    final String LEFT       = "4";
    final String FORWARDS_NS  = "5";
    final String BACKWARDS_NS  = "6";
    final String RIGHT_NS      = "7";
    final String LEFT_NS       = "8";
    final String FREE       = "1";
    final String STOP       = "9";
    final String UP_NS      = "U";
    final String DOWN_NS    = "D";

    Button connectBtn;
    Button backwardsBtn;
    Button forwardsBtn;
    Button leftBtn;
    Button rightBtn;
    Button backwardsNsBtn;
    Button forwardsNsBtn;
    Button leftNsBtn;
    Button rightNsBtn;
    Button freeBtn;

    Button forkliftUpNsBtn;
    Button forkliftDownNsBtn;

    TextView status;

    public final static String MODULE_MAC = "98:D3:41:F9:55:D8";
    public final static int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    BluetoothAdapter bta;                 //bluetooth stuff
    BluetoothSocket mmSocket;             //bluetooth stuff
    BluetoothDevice mmDevice;             //bluetooth stuff

    ConnectedThread btt = null;           //Our custom thread
    public Handler mHandler;              //this receives messages from thread
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

        connectBtn      = v.findViewById(R.id.homeFragment_bluetoothConnect);
        backwardsBtn    = v.findViewById(R.id.homeFragment_bluetoothBackwards);
        forwardsBtn     = v.findViewById(R.id.homeFragment_bluetoothForwards);
        leftBtn         = v.findViewById(R.id.homeFragment_bluetoothLeft);
        rightBtn        = v.findViewById(R.id.homeFragment_bluetoothRight);
        backwardsNsBtn  = v.findViewById(R.id.homeFragment_bluetoothBackwards_NS);
        forwardsNsBtn   = v.findViewById(R.id.homeFragment_bluetoothForwards_NS);
        leftNsBtn       = v.findViewById(R.id.homeFragment_bluetoothLeft_NS);
        rightNsBtn      = v.findViewById(R.id.homeFragment_bluetoothRight_NS);
        status          = v.findViewById(R.id.homeFragment_bluetoothStatus);
        freeBtn         = v.findViewById(R.id.homeFragment_freeStyle);

        forkliftUpNsBtn     = v.findViewById(R.id.homeFragment_bluetoothLiftUp_NS);
        forkliftDownNsBtn   = v.findViewById(R.id.homeFragment_bluetoothLiftDown_NS);

        bta = BluetoothAdapter.getDefaultAdapter();

        //if bluetooth is not enabled then create Intent for user to turn it on
        if(!bta.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
        }else {
            initiateBluetoothProcess();
        }

        backwardsBtn.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { trySendData(BACKWARDS); } });
        forwardsBtn.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { trySendData(FORWARDS); } });
        leftBtn.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { trySendData(LEFT); } });
        rightBtn.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { trySendData(RIGHT); } });
        freeBtn.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { trySendData(FREE); } });

        backwardsNsBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handle_keyDownUp(event.getAction(), BACKWARDS_NS);
                return false;
            }
        });

        forwardsNsBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handle_keyDownUp(event.getAction(), FORWARDS_NS);
                return false;
            }
        });

        leftNsBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handle_keyDownUp(event.getAction(), LEFT_NS);
                return false;
            }
        });

        rightNsBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handle_keyDownUp(event.getAction(), RIGHT_NS);
                return false;
            }
        });


        forkliftUpNsBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handle_keyDownUp(event.getAction(), UP_NS);
                return false;
            }
        });

        forkliftDownNsBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handle_keyDownUp(event.getAction(), DOWN_NS);
                return false;
            }
        });

        // Bluetooth code <-------------------------------------------------------------------------

        return v;
    }

    private void handle_keyDownUp(int eventaction, String whatToDo) {
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                trySendData(whatToDo);
                break;
            case MotionEvent.ACTION_UP:
                trySendData(STOP);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT){
            showToast("resultCode is " + resultCode);
            initiateBluetoothProcess();
        }
    }



    private void trySendData(final String data) {
        Log.i("[BLUETOOTH]", "Attempting to send data: " + data);
        //if we have connection to the bluetoothmodule
        if (mmSocket.isConnected() && btt != null) {
            btt.write(data.getBytes());
        } else {
            showToast("Something went wrong attempting to send data: " + data);
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

    public void initiateBluetoothProcess(){
        if(bta.isEnabled()){
            //attempt to connect to bluetooth module
            BluetoothSocket tmp = null;
            mmDevice = bta.getRemoteDevice(MODULE_MAC);

            //create socket
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
                mmSocket = tmp;
                mmSocket.connect();
                Log.i("[BLUETOOTH]","Connected to: "+mmDevice.getName());
                showToast("[BLUETOOTH] Connected to: "+ mmDevice.getName());
            }catch(IOException e){
                try{mmSocket.close();}catch(IOException c){return;}
                showToast("[ERR BLUETOOTH] message: "+ e.getMessage());
            }

            Log.i("[BLUETOOTH]", "Creating handler");
            mHandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    //super.handleMessage(msg);
                    if(msg.what == ConnectedThread.RESPONSE_MESSAGE) {
                        String txt = (String)msg.obj;
                        if(status.getText().toString().length() >= 30){
                            status.setText("");
                            status.append(txt);
                        }else{
                            status.append("\n" + txt);
                        }
                    }
                }
            };

            Log.i("[BLUETOOTH]", "Creating and running Thread");
            btt = new ConnectedThread(mmSocket,mHandler);
            btt.start();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            mmSocket.close();
            showToast("[BLUETOOTH] closed connection to: "+ mmDevice.getName());
        }catch(IOException e) {
        }
    }
}
