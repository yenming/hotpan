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


public class RecipeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_recipe, container, false);

        ImageView mRecipeImg = root.findViewById(R.id.recipe_img);
        TextView mRecipeTitle = root.findViewById(R.id.recipe_title);
        TextView mRecipeMaterial = root.findViewById(R.id.recipe_material);
        TextView mRecipeQuantity = root.findViewById(R.id.recipe_quantity);
        TextView mRecipeText = root.findViewById(R.id.recipe_text);

        Log.d("===Recipe Fragment===", "get recipe bundle");
        mRecipeImg.setImageResource(getArguments().getInt("recipe_img"));
        mRecipeTitle.setText(getArguments().getString("recipe_title"));
        mRecipeMaterial.setText(getArguments().getString("recipe_material"));
        mRecipeQuantity.setText(getArguments().getString("recipe_quantity"));
        mRecipeText.setText(getArguments().getString("recipe_text"));

        return root;
    }

}
