package com.project.pan.ui.frontpage;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.project.pan.DrawerActivity;
import com.project.pan.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class DevicesFragment extends Fragment {
    private Intent getIntent;
    private ImageView mRippleView;
    private ListView deviceListView;
    private ArrayList<String> mDeviceList = new ArrayList<String>();
    private Set<BluetoothDevice> pairedDevices;
    BluetoothAdapter mBluetoothAdapter;

    Animation scaleAnimation;

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_devices, container, false);
        getIntent = new Intent(this.getActivity(), DrawerActivity.class);
        if(getArguments() != null){
            getIntent.putExtras(getArguments());
        }
        mRippleView = root.findViewById(R.id.ripple_image);
        //deviceListView = root.findViewById(R.id.devices_list);
        getBluetoothList();
        scaleAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.scale_circle);
        mRippleView.startAnimation(scaleAnimation);

        Button skipBtn = root.findViewById(R.id.skipDevices);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntent);
            }
        });

        return root;
    }

    private void getBluetoothList(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();
        pairedDevices = mBluetoothAdapter.getBondedDevices();

        String info = getResources().getString(R.string.get_full_device);

        if(!mBluetoothAdapter.isEnabled()){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this.getActivity());
            alertBuilder.setCancelable(true);
            alertBuilder.setMessage("Do you want to enable bluetooth");
            alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mBluetoothAdapter.enable();
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        }
        for(BluetoothDevice bt : pairedDevices){
            String bindString = String.format(info, bt.getName(), bt.getAddress());
            Log.d("BT", bt.getName() + "(" + bt.getAddress()+")");
            mDeviceList.add(bt.getName() + "(" + bt.getAddress()+")");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
