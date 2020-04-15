package com.project.pan.ui.frontpage;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.project.pan.DrawerActivity;
import com.project.pan.R;

import java.util.Objects;

public class DevicesFragment extends Fragment {

    private NavController navDeviceController;
    private Intent getIntent;
    private ImageView mRippleView;
    Animation scaleAnimation;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_devices, container, false);
        navDeviceController = Navigation.findNavController(Objects.requireNonNull(this.getActivity()), R.id.nav_frontpage_fragment);
        getIntent = new Intent(this.getActivity(), DrawerActivity.class);
        mRippleView = root.findViewById(R.id.ripple_image);
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
}
