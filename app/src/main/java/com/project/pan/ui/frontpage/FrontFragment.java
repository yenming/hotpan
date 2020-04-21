package com.project.pan.ui.frontpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.project.pan.DrawerActivity;
import com.project.pan.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;

public class FrontFragment extends Fragment {

    private NavController navFrontController;
    private Intent skipIntent, searchDevices , backstageIntent;
    private Bundle mBundle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_frontpage, container, false);
        navFrontController = Navigation.findNavController(Objects.requireNonNull(this.getActivity()), R.id.nav_frontpage_fragment);
        skipIntent = new Intent(this.getActivity(), DrawerActivity.class);
        searchDevices = new Intent(this.getActivity(), DevicesListActivity.class);
        backstageIntent = new Intent(this.getActivity(), BackstageMain.class);
        try{
            EventBus.getDefault().register(this);
        } catch (Exception e){
            e.printStackTrace();
        }


        Button skipBtn = root.findViewById(R.id.skipPage);
        Button nextDevice = root.findViewById(R.id.frontPage);
        Button backstageBtn = root.findViewById(R.id.backPage);

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    skipIntent.putExtras(mBundle);
                }catch (Exception e){
                    e.printStackTrace();
                }
                startActivity(skipIntent);
            }
        });

        backstageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(backstageIntent);
            }
        });

        nextDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    searchDevices.putExtras(mBundle);
                }catch (Exception e){
                    e.printStackTrace();
                }
                startActivity(searchDevices);
            }
        });

        return root;
    }

    @Subscribe
    public void onEvent(Bundle getBundle){
        Log.d("===Main1===", "get temperature bundle: "+ getBundle.getInt("set_temperature"));
        mBundle = getBundle;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
