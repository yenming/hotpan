package com.project.pan.ui.helps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.project.pan.R;

public class HelpshowFragment extends Fragment {

    private HelpshowViewModel helpshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        helpshowViewModel =
                ViewModelProviders.of(this).get(HelpshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_helps, container, false);
        final TextView textView = root.findViewById(R.id.text_helps);
        helpshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
