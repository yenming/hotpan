package com.project.pan.ui.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.project.pan.BackstageMain;
import com.project.pan.DrawerActivity;
import com.project.pan.R;
import com.project.pan.backstage.PanController;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SettingsFragment extends Fragment {
    private EditText mSettingTemperature;
    private double mTemperature = 0;
    private PanController mPanController = new PanController();
    private TextView currentTemperature;
    private ConnectedThread mConnectedThread;
    private ImageButton settingTemperature;

    @SuppressLint("HandlerLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        ImageButton settingTemperature = root.findViewById(R.id.setButton);
        currentTemperature = root.findViewById(R.id.current_temperature);
        mSettingTemperature = root.findViewById(R.id.targetTemperature);
        BluetoothSocket mSocket = ((DrawerActivity)getActivity()).mBluetoothSocket;
        if(mSocket != null){
            mConnectedThread = new ConnectedThread(mSocket);
            mConnectedThread.start();
            initController();
            settingTemperature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTemperature = Double.parseDouble(mSettingTemperature.getText().toString());
                    if(mTemperature > 200)
                    {
                        mTemperature = 200;
                        mPanController.setPlateTemp(mTemperature);
                        Toast.makeText(getContext(), "Set "+mTemperature+" degree.", Toast.LENGTH_SHORT).show();
                    } else{
                        mPanController.setPlateTemp(mTemperature);
                        Toast.makeText(getContext(), "Set "+mTemperature+" degree.", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getContext(), mSettingTemperature.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            settingTemperature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTemperature = Double.parseDouble(mSettingTemperature.getText().toString());
                    if(mTemperature > 200)
                    {
                        mTemperature = 200;
                        Toast.makeText(getContext(), "Set "+mTemperature+" degree.", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(getContext(), "Set "+mTemperature+" degree.", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getContext(), mSettingTemperature.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return root;
    }

    public void initController(){
        mPanController.setPanControllerListener(new PanController.PanControllerListener() {
            @Override
            public void onModeChanged(int mode, int val) {

            }

            @Override
            public void onPowerValueCallback(int power) {

            }

            @Override
            public void onPowerValueMaxCallback(int powerMax) {

            }

            @Override
            public void onTubeCallback(double tube) {

            }

            @Override
            public void onTubeMaxCallback(int tubeMax) {

            }

            @Override
            public void onPlateCallback(double plate) {
                //Toast.makeText(getContext(), String.valueOf(plate), Toast.LENGTH_SHORT).show();
                currentTemperature.setText(String.valueOf(plate));
            }

            @Override
            public void onPlateMaxCallback(int plateMax) {

            }

            @Override
            public void onTriacCallback(double triac) {

            }

            @Override
            public void onTriacMaxCallback(int triacMax) {

            }

            @Override
            public void onGapMaxCallback(int gapMax) {

            }

            @Override
            public void onPIDModeCallback(int mode) {

            }

            @Override
            public void onKPCallback(double kp) {

            }

            @Override
            public void onOverheatCallback(int overheat) {

            }

            @Override
            public void onOtherDataCallback(String label, String data) {

            }

            @Override
            public void onSendRequest(String dataOut) {
                if(mConnectedThread != null)
                {
                    mConnectedThread.write(dataOut);
                }
            }
        });
    }

    final int handlerState = 0;  //used to identify handler message
    private StringBuilder recDataString = new StringBuilder();

    Handler bluetoothIn = new Handler() {
        public void handleMessage(@NonNull android.os.Message msg) {
            if (msg.what == handlerState) {                                     //if message is what we want
                String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                recDataString.append(readMessage);                                      //keep appending to string until ~
                int endOfLineIndex;                  // determine the end-of-line

                while ((endOfLineIndex = recDataString.toString().indexOf('\n')) >= 0) {                                           // make sure there data before ~
                    String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                    boolean isValidDatain = mPanController.dataIn(dataInPrint);
                    recDataString.delete(0, endOfLineIndex+1);                    //clear all string data
                }
                Log.d("DEBUG", recDataString.length()
                        + " "
                        + endOfLineIndex);
            }
        }
    };

    //create new class for connect thread
    public class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException ignored) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
                Log.d("DEBUG3", input);
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getContext(), "Connection Failure", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
