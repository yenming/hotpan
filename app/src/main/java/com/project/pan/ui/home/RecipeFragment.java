package com.project.pan.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.project.pan.R;
import com.project.pan.ui.viewpager.RecipeSaver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class RecipeFragment extends Fragment {

    private ImageView mRecipeImg;
    private TextView mRecipeText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_recipe, container, false);

        mRecipeImg = root.findViewById(R.id.recipe_img);
        mRecipeText = root.findViewById(R.id.recipe_text);

        Log.d("===Recipe Fragment===", "get recipe bundle");
        mRecipeImg.setImageResource(getArguments().getInt("recipe_img"));
        mRecipeText.setText(getArguments().getString("recipe_text"));

        return root;
    }

}
