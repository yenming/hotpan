package com.project.pan.ui.guide;

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

public class GuideFragment extends Fragment {

    private GuideViewModel guideViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        guideViewModel =
                ViewModelProviders.of(this).get(GuideViewModel.class);
        View root = inflater.inflate(R.layout.fragment_guide, container, false);
        final TextView textView = root.findViewById(R.id.text_guides);
        guideViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
