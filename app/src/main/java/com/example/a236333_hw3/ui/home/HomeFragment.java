package com.example.a236333_hw3.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.a236333_hw3.R;
import com.example.a236333_hw3.Tools.RoboCodeSettings;
import com.example.a236333_hw3.Tools.SuccessFailureHandler;
import com.example.a236333_hw3.Tools.roboCodeTask;
import com.example.a236333_hw3.roboCodeTaskActivity;
import com.example.a236333_hw3.roboCodeTaskButton;
import com.example.a236333_hw3.ui.Tasks.TasksFragment;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class HomeFragment extends Fragment {

    TextView userNicknameTextView;
    LinearLayout homeFragment_finishLoadLayout, homeFragment_doLoadLayout;

    // Bluetooth code ----------------------------------------------------------------------------->
    final String ON = "1";
    final String OFF = "1";
    final String FORWARDS   = "1";
    final String BACKWARDS  = "2";
    final String RIGHT      = "3";
    final String LEFT       = "4";

    BluetoothSPP bluetooth;

    Button connectBtn;
    Button backwardsBtn;
    Button forwardsBtn;
    Button leftBtn;
    Button rightBtn;
    TextView status;
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
        bluetooth = new BluetoothSPP(this.getActivity());

        connectBtn      = v.findViewById(R.id.homeFragment_bluetoothConnect);
        backwardsBtn    = v.findViewById(R.id.homeFragment_bluetoothBackwards);
        forwardsBtn     = v.findViewById(R.id.homeFragment_bluetoothForwards);
        leftBtn         = v.findViewById(R.id.homeFragment_bluetoothLeft);
        rightBtn        = v.findViewById(R.id.homeFragment_bluetoothRight);
        status          = v.findViewById(R.id.homeFragment_bluetoothStatus);

        if (!bluetooth.isBluetoothAvailable()) {
            showToast("Bluetooth not available!");
        }

        bluetooth.setupService(); // setup bluetooth service
        bluetooth.startService(true); // start bluetooth service

        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            @Override
            public void onDeviceConnected(String name, String address) {
                status.setText("connected to: " + name);
            }

            @Override
            public void onDeviceDisconnected() {
                status.setText("connection lost!");
            }

            @Override
            public void onDeviceConnectionFailed() {
                status.setText("unable to connect!");
            }
        });

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bluetooth.disconnect();
                } else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        backwardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { bluetooth.send(BACKWARDS, true); }
        });

        forwardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { bluetooth.send(FORWARDS, true); }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { bluetooth.send(LEFT, true); }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { bluetooth.send(RIGHT, true); }
        });

        // Bluetooth code <-------------------------------------------------------------------------

        return v;
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
        bluetooth.stopService();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        showToast( "Got here!!!");
        try {


            if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
                if (resultCode == Activity.RESULT_OK)
                    bluetooth.connect(data);
            } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
                if (resultCode == Activity.RESULT_OK) {
                    bluetooth.setupService();
                } else {
                    showToast("Bluetooth was not enabled.");
                }
            }
        } catch (Exception ex) {
            showToast( ex.getMessage());
        }
    }
}
