package com.project.pan.ui.frontpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.pan.R;

public class BackstageFragment extends Fragment {
    @Nullable
    @Override
    //Optional: Move Backstage UI to here by using fragment control.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.backstage_main, container, false);

        return root;
    }
}
