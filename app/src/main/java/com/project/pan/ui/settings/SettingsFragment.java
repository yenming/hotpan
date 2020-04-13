package com.project.pan.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.project.pan.R;

public class SettingsFragment extends Fragment {

    private EditText mSettingTemperature;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        ImageButton settingTemperature = root.findViewById(R.id.setting_temperature);
        mSettingTemperature = root.findViewById(R.id.input_temperature);

        settingTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), mSettingTemperature.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
