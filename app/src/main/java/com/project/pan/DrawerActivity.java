package com.project.pan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.project.pan.R;
import com.project.pan.backstage.PanController;
import com.project.pan.ui.settings.SettingsFragment;
import com.project.pan.ui.viewpager.RecipeSaver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static androidx.navigation.ui.NavigationUI.setupWithNavController;

public class DrawerActivity extends AppCompatActivity implements View.OnClickListener {
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;


    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavController navHomeController;
    private NavGraph navGraph;
    NavArgument temperatureArg;
    private Bundle mTemperature;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;
    public BluetoothSocket mBluetoothSocket = null;
    private BluetoothDevice mBluetoothDevice = null;
    private String mAddress = null;
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        if(this.getIntent().getExtras() != null){
            mTemperature = this.getIntent().getExtras();
            temperatureArg = new NavArgument.Builder().setDefaultValue(mTemperature.getInt("set_temperature")).build();
            Log.d("===Drawer===", "get temperature bundle: "+ mTemperature.getInt("set_temperature")+" / "+ temperatureArg.getDefaultValue());
        }
        try{
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothAdapter.enable();
            mAddress = this.getIntent().getExtras().getString(DevicesListActivity.EXTRA_DEVICE_ADDRESS);
            Log.d("====addr====", this.getIntent().getExtras().getString(DevicesListActivity.EXTRA_DEVICE_ADDRESS));
            bluetoothConnect(mAddress);
        } catch (Exception e){
            e.printStackTrace();
        }

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_guide, R.id.nav_helps)
                .setDrawerLayout(drawer)
                .build();
        navHomeController = Navigation.findNavController(this, R.id.nav_home_fragment);
        navGraph = navHomeController.getNavInflater().inflate(R.navigation.home_navigation);
        navGraph.addArgument("set_temperature", temperatureArg);
        navHomeController.setGraph(navGraph);
        NavigationUI.setupActionBarWithNavController(this, navHomeController, mAppBarConfiguration);
        setupWithNavController(navigationView, navHomeController);

        View getHeader = navigationView.getHeaderView(0);
        ImageButton menuButton = getHeader.findViewById(R.id.header_menu_btn);
        menuButton.setOnClickListener(this);

        ImageButton setButton = findViewById(R.id.pan_setting);
        setButton.setOnClickListener(this);

    }

    public synchronized void bluetoothDisconnect() {
        try {
            if(mBluetoothSocket != null) {
                //Don't leave Bluetooth sockets open when leaving activity
                mBluetoothSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            //insert code to deal with this
        }
    }

    public synchronized void bluetoothConnect(String addr) {
        if(!checkBTState()) {
            return;
        }
        boolean condition;
        condition = addr != null;
        condition &= (mBluetoothAdapter != null) && (mBluetoothAdapter.isEnabled());
        condition &= (mBluetoothAdapter != null) && (mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) != BluetoothHeadset.STATE_CONNECTED);

        if(condition) {
            //create device and set the MAC address
            mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(addr);

            try {
                mBluetoothSocket = createBluetoothSocket(mBluetoothDevice);
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
            }
            // Establish the Bluetooth socket connection.
            try {
                mBluetoothSocket.connect();
            } catch (IOException e) {
                try {
                    mBluetoothSocket.close();
                } catch (IOException e2) {
                    //insert code to deal with this
                }
            }
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private boolean checkBTState() {

        if(mBluetoothAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
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

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.header_menu_btn:
                drawer.closeDrawers();
                break;
            case R.id.pan_setting:
                navHomeController.navigate(R.id.nav_settings, mTemperature);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Now is empty
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_home_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Subscribe
    public void onEvent(Bundle getBundle){
        for (String key: getBundle.keySet()) {
            switch (key){
                case "recipe":
                    navHomeController.navigate(R.id.nav_recipe, getBundle);
                    Log.d("===Drawer===", "get recipe bundle: "+key+" / "+getBundle.getInt("recipe_img")+" / "+getBundle.getString("recipe_text"));
                    break;
                case "set_temperature":
                    Log.d("===Drawer===", "get temperature bundle: "+key+" / "+getBundle.getInt("set_temperature"));
                    break;
                case "food_fish":
                    Log.d("===Drawer===", "get food_fish bundle: "+key+"/"+getBundle.getBoolean("food_fish"));
                    if(getBundle.getBoolean("food_fish")){
                        navHomeController.navigate(R.id.fish_fragemnt);
                    }
                    break;
                default:
                    Log.d("===Drawer===", "get bundle: null");
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        bluetoothConnect(mAddress);
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bluetoothDisconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
