package com.project.pan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.opencsv.CSVReader;
import com.project.pan.R;
import com.project.pan.backstage.LogWriter;
import com.project.pan.backstage.PanController;
import com.project.pan.backstage.TextInputDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class BackstageMain extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";

    Button stopButton, logNewButton, logStartButton, plateButton, powerButton, replayButton, replayOpenButton;
    EditText powerEditText, plateEditText;
    SeekBar seekBar;
    TextView deviceNameTextView;
    TextView powerTextView, powerMaxTextView, tubeTextView, tubeMaxTextView, plateTextView, plateMaxTextView, triacTextView, triacMaxTextView, kpTextView, gapMaxTextView, pidModeTextView, logTextView, replayTextView;

    View.OnClickListener triacMaxOnClickListener;
    View.OnClickListener tubeMaxOnClickListener;
    View.OnClickListener gapMaxOnClickListener;
    View.OnClickListener kpOnClickListener;

    Handler bluetoothIn;

    final int handlerState = 0;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    private BluetoothDevice btDevice = null;

    private ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address = null;

    private boolean logToCSV = false;
    @SuppressLint("SimpleDateFormat")
    private String filename = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.'csv'").format(Calendar.getInstance().getTime());
    public LogWriter logWriter = new LogWriter(filename);

    private PanController pan = new PanController();

    private AsyncTask replay = null;

    private Drawable btn_default_drawable = null;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backstage_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        deviceNameTextView = findViewById(R.id.deviceNameTextView);
        powerTextView = findViewById(R.id.powerTextView);
        powerMaxTextView = findViewById(R.id.powerMaxTextView);
        tubeTextView = findViewById(R.id.tubeTextView);
        tubeMaxTextView = findViewById(R.id.tubeMaxTextView);
        plateTextView = findViewById(R.id.plateTextView);
        plateMaxTextView = findViewById(R.id.plateMaxTextView);
        triacTextView = findViewById(R.id.triacTextView);
        triacMaxTextView = findViewById(R.id.triacMaxTextView);
        kpTextView = findViewById(R.id.kpTextView);
        gapMaxTextView = findViewById(R.id.gapMaxTextView);
        pidModeTextView = findViewById(R.id.pidModeTextView);
        seekBar = findViewById(R.id.seekBar);
        powerEditText = findViewById(R.id.powerEditText);
        powerButton = findViewById(R.id.powerButton);
        plateEditText = findViewById(R.id.plateEditText);
        plateButton = findViewById(R.id.plateButton);
        stopButton = findViewById(R.id.stopButton);
        logTextView = findViewById(R.id.logTextView);
        logNewButton = findViewById(R.id.logNewButton);
        logStartButton = findViewById(R.id.logStartButton);
        replayTextView = findViewById(R.id.replayTextView);
        replayOpenButton = findViewById(R.id.replayOpenButton);
        replayButton = findViewById(R.id.replayButton);

        tubeMaxTextView.setPaintFlags(tubeMaxTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        triacMaxTextView.setPaintFlags(triacMaxTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        gapMaxTextView.setPaintFlags(gapMaxTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        kpTextView.setPaintFlags(kpTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        logTextView.setText(filename);
        seekBar.setMax(PanController.POWER_MAX);
        btn_default_drawable = logNewButton.getBackground();

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(btAdapter == null) {
            Toast.makeText(this, "Device won't support Bluetooth", Toast.LENGTH_SHORT).show();
        }

        deviceNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list(view);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pan.setPower(seekBar.getProgress());
            }
        });

        tubeMaxOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputDialog textInputDialog = new TextInputDialog(getApplicationContext(), "Input a new max temp of tube", true);

                textInputDialog.setListener(new TextInputDialog.Listener() {
                    @Override
                    public boolean onOkClicked(String s) {
                        if(s.length() > 0) {
                            int val = Integer.parseInt(s);
                            if(val < 0) {
                                Toast.makeText(getApplicationContext(), "Please input a positive number", Toast.LENGTH_SHORT).show();
                            } else if(val > 500) {
                                Toast.makeText(getApplicationContext(), "Please input a positive number less than 500", Toast.LENGTH_SHORT).show();
                            } else {
                                pan.setTubeMax(val);
                                return true;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please input a number", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                textInputDialog.show();
            }
        };

        triacMaxOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputDialog textInputDialog = new TextInputDialog(getApplicationContext(), "Input a new max temp of TRIAC", true);

                textInputDialog.setListener(new TextInputDialog.Listener() {
                    @Override
                    public boolean onOkClicked(String s) {
                        if(s.length() > 0) {
                            int val = Integer.parseInt(s);
                            if(val < 0) {
                                Toast.makeText(getApplicationContext(), "Please input a positive number", Toast.LENGTH_SHORT).show();
                            } else if(val > 500) {
                                Toast.makeText(getApplicationContext(), "Please input a positive number less than 500", Toast.LENGTH_SHORT).show();
                            } else {
                                pan.setTriacMax(val);
                                return true;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please input a number", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                textInputDialog.show();
            }
        };

        gapMaxOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputDialog textInputDialog = new TextInputDialog(getApplicationContext(), "Input a new max temp of gap", true);

                textInputDialog.setListener(new TextInputDialog.Listener() {
                    @Override
                    public boolean onOkClicked(String s) {
                        if(s.length() > 0) {
                            int val = Integer.parseInt(s);
                            if(val < 0) {
                                Toast.makeText(getApplicationContext(), "Please input a positive number", Toast.LENGTH_SHORT).show();
                            } else if(val > 300) {
                                Toast.makeText(getApplicationContext(), "Please input a positive number less than 300", Toast.LENGTH_SHORT).show();
                            } else {
                                pan.setGapMax(val);
                                return true;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please input a number", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                textInputDialog.show();
            }
        };

        kpOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputDialog textInputDialog = new TextInputDialog(getApplicationContext(), "Input a new max temp of KP", true);

                textInputDialog.setListener(new TextInputDialog.Listener() {
                    @Override
                    public boolean onOkClicked(String s) {
                        if(s.length() > 0) {
                            double val = Double.parseDouble(s);
                            if(val < 0) {
                                Toast.makeText(getApplicationContext(), "Please input a positive number", Toast.LENGTH_SHORT).show();
                            } else if(val > 20) {
                                Toast.makeText(getApplicationContext(), "Please input a positive number less than 20", Toast.LENGTH_SHORT).show();
                            } else {
                                pan.setKP(val);
                                return true;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please input a number", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                textInputDialog.show();
            }
        };

        tubeMaxTextView.setOnClickListener(tubeMaxOnClickListener);
        triacMaxTextView.setOnClickListener(triacMaxOnClickListener);
        gapMaxTextView.setOnClickListener(gapMaxOnClickListener);
        kpTextView.setOnClickListener(kpOnClickListener);

        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pan.setPower(Integer.parseInt(((EditText)findViewById(R.id.powerEditText)).getText().toString()));
            }
        });

        plateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("set_temperature", Integer.parseInt(plateEditText.getText().toString()));
                Log.d("===set_temperature===", "get temperature bundle: "+ Integer.parseInt(plateEditText.getText().toString()));
                EventBus.getDefault().post(bundle);
                pan.setPlateTemp(Double.parseDouble(plateEditText.getText().toString()));
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pan.stop();
            }
        });

        logStartButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(logToCSV){
                    logToCSV = false;
                    logStartButton.setText("Start");
                    logStartButton.setBackgroundColor(0x00000000);
                    logStartButton.setBackground(btn_default_drawable);
                } else {
                    logToCSV = true;
                    logStartButton.setText("Pause");
                    logStartButton.setBackgroundColor(0x8000ff00);
                }
            }
        });

        logNewButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View view) {
                filename = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.'csv'").format(Calendar.getInstance().getTime());
                logTextView.setText(filename);
                logWriter = new LogWriter(filename);
            }
        });

        logTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Title");

// Set up the input
                final EditText input = new EditText(getApplicationContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(filename);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filename = input.getText().toString();
                        if(logWriter != null) {
                            logWriter.rename(filename);
                        }
                        logTextView.setText(filename);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        pan.setPanControllerListener(new PanController.PanControllerListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onModeChanged(int mode, int val) {
                stopButton.setText("停止");
                powerButton.setText("設定火力(0~15625)");
                plateButton.setText("設定溫度(0~200.0)");
                stopButton.setBackgroundColor(0x00000000);
                powerButton.setBackgroundColor(0x00000000);
                plateButton.setBackgroundColor(0x00000000);
                stopButton.setBackground(btn_default_drawable);
                powerButton.setBackground(btn_default_drawable);
                plateButton.setBackground(btn_default_drawable);
                switch (mode) {
                    case PanController.MODE_IDLE:
                        stopButton.setText("閒置中");
                        stopButton.setBackgroundColor(0x8000ff00);
                        break;
                    case PanController.MODE_POWER:
                        powerButton.setText("已設定火力("+val+")");
                        powerButton.setBackgroundColor(0x8000ff00);
                        break;
                    case PanController.MODE_TEMP:
                        double temp = val / 10.0;
                        plateButton.setText("已設定溫度("+temp+")");
                        plateButton.setBackgroundColor(0x8000ff00);
                        break;
                }
            }

            @Override
            public void onPowerValueCallback(int power) {
                powerTextView.setText(String.valueOf(power));
                if(power == PanController.POWER_NO) {
                    powerTextView.setBackgroundColor(0xff00ff00);
                } else if(power <= PanController.POWER_MIN) {
                    powerTextView.setBackgroundColor(0xff40ff00);
                } else if(power <= PanController.POWER_MED) {
                    powerTextView.setBackgroundColor(0xff80ff00);
                } else {
                    powerTextView.setBackgroundColor(0xffffff00);
                }
            }

            @Override
            public void onPowerValueMaxCallback(int powerMax) {
                powerMaxTextView.setText(String.valueOf(powerMax));
            }

            @Override
            public void onTubeCallback(double tube) {
                tubeTextView.setText(String.valueOf(tube));
            }

            @Override
            public void onTubeMaxCallback(int tubeMax) {
                tubeMaxTextView.setText(String.valueOf(tubeMax));
            }

            @Override
            public void onPlateCallback(double plate) {
                plateTextView.setText(String.valueOf(plate));
            }

            @Override
            public void onPlateMaxCallback(int plateMax) {
                plateMaxTextView.setText(String.valueOf(plateMax));
            }

            @Override
            public void onTriacCallback(double triac) {
                triacTextView.setText(String.valueOf(triac));
            }

            @Override
            public void onTriacMaxCallback(int triacMax) {
                triacMaxTextView.setText(String.valueOf(triacMax));
            }

            @Override
            public void onGapMaxCallback(int gapMax) {
                gapMaxTextView.setText(String.valueOf(gapMax));
            }

            @Override
            public void onPIDModeCallback(int mode) {
                pidModeTextView.setText(String.valueOf(mode));
            }

            @Override
            public void onKPCallback(double kp) {
                kpTextView.setText(String.valueOf(kp));
            }

            @Override
            public void onOverheatCallback(int overheat) {
                switch (overheat) {
                    case PanController.OVERHEAT_NO:
                        tubeTextView.setBackgroundColor(0x00000000);
                        plateTextView.setBackgroundColor(0x00000000);
                        triacTextView.setBackgroundColor(0x00000000);
                        break;
                    case PanController.OVERHEAT_TUBE:
                        tubeTextView.setBackgroundColor(0x80ff0000);
                        break;
                    case PanController.OVERHEAT_TRIAC:
                        triacTextView.setBackgroundColor(0x80ff0000);
                        break;
                    case PanController.OVERHEAT_TRIAC|PanController.OVERHEAT_TUBE:
                        tubeTextView.setBackgroundColor(0x80ff0000);
                        triacTextView.setBackgroundColor(0x80ff0000);
                        break;
                    case PanController.OVERHEAT_TUBE_PLATE:
                    case PanController.OVERHEAT_TUBE_PLATE|PanController.OVERHEAT_TUBE:
                        tubeTextView.setBackgroundColor(0x80ff0000);
                        plateTextView.setBackgroundColor(0x80ff0000);
                        break;
                    case PanController.OVERHEAT_TUBE_PLATE|PanController.OVERHEAT_TRIAC:
                    case PanController.OVERHEAT_TUBE_PLATE|PanController.OVERHEAT_TRIAC|PanController.OVERHEAT_TUBE:
                        triacTextView.setBackgroundColor(0x80ff0000);
                        tubeTextView.setBackgroundColor(0x80ff0000);
                        plateTextView.setBackgroundColor(0x80ff0000);
                        break;
                    default:
                        break;
                }
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

        replayOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
            }
        });

        replayButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"StaticFieldLeak", "SetTextI18n"})
            @Override
            public void onClick(View view) {
                if(replay == null) {
                    final File f = new File(replayTextView.getText().toString());
                    if(f.exists()&&!f.isDirectory()) {
                        replay = new AsyncTask<Void, Void, Void>() {
                            private String error = null;

                            @Override
                            protected Void doInBackground(Void... voids) {
                                try {
                                    long startTimestamp = Calendar.getInstance().getTimeInMillis();
                                    CSVReader reader = new CSVReader(new FileReader(f));

                                    String[] labels = null;

                                    int elapsedIndex = 0, plateTempIndex = 0;


                                    for(String[] values : reader) {
                                        if(labels == null) {
                                            labels = values;
                                            int findCount = 0;
                                            for(int i = 0; i < labels.length; i++) {
                                                if(labels[i].equals("pid_Setpoint")) {
                                                    plateTempIndex = i;
                                                    findCount++;
                                                }
                                                if(labels[i].equals("elapsed")) {
                                                    elapsedIndex = i;
                                                    findCount++;
                                                }
                                            }
                                            if(findCount != 2) {
                                                error = "File is not available";
                                                return null;
                                            }
                                            continue;
                                        }
                                        long elapsed = Long.parseLong(values[elapsedIndex]);
                                        double plateTemp = Double.parseDouble(values[plateTempIndex]);
                                        long nextTimestamp = startTimestamp + elapsed;
                                        while(Calendar.getInstance().getTimeInMillis() < nextTimestamp) {
                                            try {
                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (replay.isCancelled())
                                            break;
                                        pan.setPlateTemp(plateTemp);
                                    }

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                replayButton.setText("Stop");
                                replayButton.setBackgroundColor(0x8000ff00);
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                replayButton.setText("Replay");
                                replayButton.setBackgroundColor(0x00000000);
                                replayButton.setBackground(btn_default_drawable);
                                if(error != null) {
                                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.execute();
                    }
                } else {
                    replay.cancel(true);
                    replay = null;
                    replayButton.setText("Replay");
                    replayButton.setBackgroundColor(0x00000000);
                    replayButton.setBackground(btn_default_drawable);
                }
            }
        });

        bluetoothIn = new Handler() {
            public void handleMessage(@NonNull android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                      //keep appending to string until ~
                    int endOfLineIndex;                  // determine the end-of-line

                    while ((endOfLineIndex = recDataString.toString().indexOf('\n')) >= 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        boolean isValidDatain = pan.dataIn(dataInPrint);
                        if(isValidDatain && logToCSV) {
                            logWriter.write(dataInPrint);
                        }
                        recDataString.delete(0, endOfLineIndex+1);                    //clear all string data
                    }
                    Log.d("DEBUG", recDataString.length()
                            + " "
                            + endOfLineIndex);
                }
            }
        };
        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        address = settings.getString("address", null);

        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        checkBTState();
    }
    /*
        public String adcToTemp(int adc) {
            double v = adc * 5.0 / 1023.0;
            double r = 10000.0 * v / (5.0 - v);
            double t = 3950.0 / (Math.log(r) + 1.73543945201787) - 273.15;
            return new DecimalFormat("0.00").format(t);
        }
    */
    public void list(View v) {
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            final String[] list = new String[pairedDevices.size()];
            final String[] addrList = new String[pairedDevices.size()];
            int i = 0;
            for (BluetoothDevice bt : pairedDevices) {
                list[i] = bt.getName();
                addrList[i] = bt.getAddress();
                i++;
            }

            Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setItems(list, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    bluetoothConnect(addrList[i]);
                    dialogInterface.cancel();
                }
            }).create().show();

        } else {
            Toast.makeText(getApplicationContext(), "No paired devices", Toast.LENGTH_SHORT).show();
        }
    }

    public void bluetoothReconnect() {
        bluetoothDisconnect();
        bluetoothConnect(address);
    }

    public synchronized void bluetoothDisconnect() {
        deviceNameTextView.setBackgroundColor(0x80ff0000);
        try {
            if(btSocket != null) {
                //Don't leave Bluetooth sockets open when leaving activity
                btSocket.close();
            }

        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    public synchronized void bluetoothConnect(String addr) {
        if(!checkBTState()) {
            return;
        }
        address = addr;
        boolean condition;
        condition = address != null;
        condition &= (btAdapter != null) && (btAdapter.isEnabled());
        condition &= (btAdapter != null) && (btAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) != BluetoothHeadset.STATE_CONNECTED);

        if(condition) {

            deviceNameTextView.setText("連線中");
            //create device and set the MAC address
            btDevice = btAdapter.getRemoteDevice(address);

            try {
                btSocket = createBluetoothSocket(btDevice);
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
            }
            // Establish the Bluetooth socket connection.
            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    //insert code to deal with this
                }
            }
            deviceNameTextView.setText(btDevice.getName());
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private boolean checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (!btAdapter.isEnabled()) {
                btAdapter.enable();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
        return true;
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
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
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }

        void cancel() {
            try {
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*
        @Override
        protected void onResume() {
            super.onResume();
            bluetoothConnect(address);
        }
    */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(action, BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                BluetoothDevice device = Objects.requireNonNull(intent.getExtras())
                        .getParcelable(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && device.getAddress().equals(btDevice.getAddress())) {
                    deviceNameTextView.setBackgroundColor(0x80ff0000);
                    mConnectedThread.cancel();
                    mConnectedThread = null;
                    bluetoothConnect(address);
                }
            } else if (TextUtils.equals(action, BluetoothDevice.ACTION_ACL_CONNECTED)){
                BluetoothDevice device = Objects.requireNonNull(intent.getExtras())
                        .getParcelable(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && device.getAddress().equals(btDevice.getAddress())) {
                    deviceNameTextView.setBackgroundColor(0x8000ff00);
                    mConnectedThread = new ConnectedThread(btSocket);
                    mConnectedThread.start();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        if(address != null) editor.putString("address", address);
        editor.apply();
        /*
        if (mConnectedThread != null)
        {
            pan.setPower(0);
        }
        bluetoothDisconnect();
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {
            boolean isImageFromGoogleDrive = false;

            Uri uri = data.getData();

            String realPath_1 = null;

            if (DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
                if (uri != null) {
                    if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                        String docId = DocumentsContract.getDocumentId(uri);
                        String[] split = docId.split(":");
                        String type = split[0];

                        if ("primary".equalsIgnoreCase(type)) {
                            realPath_1 = Environment.getExternalStorageDirectory() + "/" + split[1];
                        } else {
                            Pattern DIR_SEPORATOR = Pattern.compile("/");
                            Set<String> rv = new HashSet<>();
                            String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
                            String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
                            String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
                            if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
                                if (TextUtils.isEmpty(rawExternalStorage)) {
                                    rv.add("/storage/sdcard0");
                                } else {
                                    rv.add(rawExternalStorage);
                                }
                            } else {
                                String rawUserId;
                                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                                String[] folders = DIR_SEPORATOR.split(path);
                                String lastFolder = folders[folders.length - 1];
                                boolean isDigit = false;
                                try {
                                    Integer.valueOf(lastFolder);
                                    isDigit = true;
                                } catch (NumberFormatException ignored) {
                                }
                                rawUserId = isDigit ? lastFolder : "";
                                if (TextUtils.isEmpty(rawUserId)) {
                                    rv.add(rawEmulatedStorageTarget);
                                } else {
                                    rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
                                }
                            }
                            if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
                                String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
                                Collections.addAll(rv, rawSecondaryStorages);
                            }
                            String[] temp = rv.toArray(new String[rv.size()]);
                            for (String s : temp) {
                                File tempf = new File(s + "/" + split[1]);
                                if (tempf.exists()) {
                                    realPath_1 = s + "/" + split[1];
                                }
                            }
                        }
                    } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                        String id = DocumentsContract.getDocumentId(uri);
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                        String column = "_data";
                        String[] projection = {column};
                        try (Cursor cursor = getApplicationContext().getContentResolver().query(contentUri, projection, null, null, null)) {
                            if (cursor != null && cursor.moveToFirst()) {
                                int column_index = cursor.getColumnIndexOrThrow(column);
                                realPath_1 = cursor.getString(column_index);
                            }
                        }
                    } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                        String docId = DocumentsContract.getDocumentId(uri);
                        String[] split = docId.split(":");
                        String type = split[0];

                        Uri contentUri = null;
                        if ("image".equals(type)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(type)) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(type)) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        String selection = "_id=?";
                        String[] selectionArgs = new String[]{split[1]};

                        String column = "_data";
                        String[] projection = {column};
                        if (contentUri != null) {
                            try (Cursor cursor = getApplicationContext().getContentResolver().query(contentUri, projection, selection, selectionArgs, null)) {
                                if (cursor != null && cursor.moveToFirst()) {
                                    int column_index = cursor.getColumnIndexOrThrow(column);
                                    realPath_1 = cursor.getString(column_index);
                                }
                            }
                        }
                    } else if ("com.google.android.apps.docs.storage".equals(uri.getAuthority())) {
                        isImageFromGoogleDrive = true;
                    }
                }
            } else if (uri != null) {
                if ("content".equalsIgnoreCase(uri.getScheme())) {

                    String column = "_data";
                    String[] projection = {column};
                    try (Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null)) {
                        if (cursor != null && cursor.moveToFirst()) {
                            int column_index = cursor.getColumnIndexOrThrow(column);
                            realPath_1 = cursor.getString(column_index);
                        }
                    }
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    realPath_1 = uri.getPath();
                }
            }

            replayTextView.setText(realPath_1);
        }
    }
}
