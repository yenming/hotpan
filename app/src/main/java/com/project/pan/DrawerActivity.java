package com.project.pan;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
import com.project.pan.ui.settings.SettingsFragment;
import com.project.pan.ui.viewpager.RecipeSaver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Objects;

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

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

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
            Log.d("====addr====", this.getIntent().getExtras().getString(DevicesListActivity.EXTRA_DEVICE_ADDRESS));
            connectDevice(this.getIntent(), true);
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
    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        Bundle extras = data.getExtras();
        if (extras == null) {
            return;
        }
        String address = extras.getString(DevicesListActivity.EXTRA_DEVICE_ADDRESS);
        Log.d("====addr====", address);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
        }
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
                default:
                    Log.d("===Drawer===", "get bundle: null");
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
