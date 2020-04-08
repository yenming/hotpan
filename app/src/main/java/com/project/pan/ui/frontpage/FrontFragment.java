package com.project.pan.ui.frontpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.project.pan.DrawerActivity;
import com.project.pan.R;
import com.project.pan.ui.guide.GuideViewModel;

import java.util.Objects;

public class FrontFragment extends Fragment {

    private NavController navFrontController;
    private Intent getIntent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_frontpage, container, false);
        navFrontController = Navigation.findNavController(Objects.requireNonNull(this.getActivity()), R.id.nav_frontpage_fragment);
        getIntent = new Intent(this.getActivity(), DrawerActivity.class);

        Button skipBtn = (Button) root.findViewById(R.id.skipPage);
        Button nextDevice = (Button) root.findViewById(R.id.frontPage);

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntent);
            }
        });

        nextDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navFrontController.navigate(R.id.nav_devices);
            }
        });

        return root;
    }

}
