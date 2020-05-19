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

import java.util.Arrays;
import java.util.List;


public class RecipeFragment extends Fragment {
    private List<String> mRecipeSteps;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_recipe, container, false);
        ImageView mRecipeImg = root.findViewById(R.id.recipe_img);
        TextView mRecipeTitle = root.findViewById(R.id.recipe_title);
        TextView mRecipeMaterial = root.findViewById(R.id.recipe_material);
        TextView mRecipeQuantity = root.findViewById(R.id.recipe_quantity);
        TextView mRecipeText = root.findViewById(R.id.recipe_text);

        getSteps();
        Log.d("===Recipe Fragment===", "get recipe bundle");
        mRecipeImg.setImageResource(getArguments().getInt("recipe_img"));
        mRecipeTitle.setText(getArguments().getString("recipe_title"));
        mRecipeMaterial.setText(getArguments().getString("recipe_material"));
        mRecipeQuantity.setText(getArguments().getString("recipe_quantity"));
        if(mRecipeSteps.size() > 0){

        } else {
            mRecipeText.setText(getArguments().getString("recipe_text"));
        }

        return root;
    }

    public void getSteps(){
        String rawText = getArguments().getString("recipe_text");
        if("".equals(rawText)){
            return;
        } else {
            String[] getSteps = rawText.split("\n");
            this.mRecipeSteps = Arrays.asList(getSteps);
        }
    }

}
