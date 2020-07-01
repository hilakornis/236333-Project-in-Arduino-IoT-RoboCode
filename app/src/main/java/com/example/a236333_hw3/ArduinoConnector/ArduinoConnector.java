package com.example.a236333_hw3.ArduinoConnector;

import com.example.a236333_hw3.ArduinoConnector.Bluetooth.ConnectedThread;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.app.Activity;
import java.io.IOException;
import android.os.Message;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.UUID;

/* TODO : IMPORTANT !!

    Dont forget to add the current code to the calling activity:

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ArduinoConnector.REQUEST_ENABLE_BT) {
            showToast("resultCode is " + resultCode);
            < ArduinoConnector instance name >.connectBlutooth(this.getActivity());
        }
    }

*/

public class ArduinoConnector {

    private static final String MODULE_MAC = "98:D3:41:F9:55:D8";
    public  static final int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    BluetoothAdapter bta;                 //bluetooth stuff
    BluetoothSocket mmSocket;             //bluetooth stuff
    BluetoothDevice mmDevice;             //bluetooth stuff
    ConnectedThread btt = null;           //Our custom thread
    public Handler mHandler;              //this receives messages from thread

    public ArrayDeque<String> getResults() {
        return results;
    }

    ArrayDeque<String> results;

    public ArduinoConnector() {

        this.bta = BluetoothAdapter.getDefaultAdapter();
        this.results = new ArrayDeque<String>();
    }

    public void connectBlutooth(Activity currentContext) {
        //if bluetooth is not enabled then create Intent for user to turn it on
        if(!bta.isEnabled()) {

            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            currentContext.startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
        }else {
            initiateBluetoothProcess();
        }
    }

    public void connectBlutooth(Fragment currentContext) {
        //if bluetooth is not enabled then create Intent for user to turn it on
        if(!bta.isEnabled()) {

            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            currentContext.startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
        }else {
            initiateBluetoothProcess();
        }
    }

    private void initiateBluetoothProcess() {
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
                // TODO : showToast("[BLUETOOTH] Connected to: "+ mmDevice.getName());
            }catch(IOException e){
                try{mmSocket.close();}catch(IOException c){return;}
                // TODO : showToast("[ERR BLUETOOTH] message: "+ e.getMessage());
            }

            Log.i("[BLUETOOTH]", "Creating handler");
            mHandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.what == ConnectedThread.RESPONSE_MESSAGE) {
                        String str = ((String)msg.obj).split("-", 2)[0];
                        if (!str.equals("")) results.push(str);
                    }
                }
            };

            Log.i("[BLUETOOTH]", "Creating and running Thread");
            btt = new ConnectedThread(mmSocket,mHandler);
            btt.start();
        }
    }

    public void trySendData(final String data) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("[BLUETOOTH]", "Attempting to send data: " + data);
                //if we have connection to the bluetoothmodule
                if (mmSocket.isConnected() && btt != null) {
                    btt.write(data.getBytes());
                } else {
                    // TODO : handel this showToast("Something went wrong attempting to send data: " + data);
                }
            }
        });
        th.run();
    }

    public boolean disconnect() {
        try {
            mmSocket.close();
            return true;
        }catch(IOException e) {
            // TODO : Log this!
            return false;
        }
    }

    public boolean isConnected() {
        return mmSocket != null && mmSocket.isConnected();
    }
}
