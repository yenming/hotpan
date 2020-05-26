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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.pan.R;

import java.util.Arrays;
import java.util.List;


public class RecipeFragment extends Fragment {
    private List<String> mRecipeSteps;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_recipe, container, false);
        ImageView mRecipeImg = root.findViewById(R.id.recipe_img);
        TextView mRecipeTitle = root.findViewById(R.id.recipe_title);
        TextView mRecipeMaterial = root.findViewById(R.id.recipe_material);
        TextView mRecipeQuantity = root.findViewById(R.id.recipe_quantity);
        TextView mRecipeText = root.findViewById(R.id.recipe_text);

        Log.d("===Recipe Fragment===", "get recipe bundle");
        getSteps();
        mRecipeImg.setImageResource(getArguments().getInt("recipe_img"));
        mRecipeTitle.setText(getArguments().getString("recipe_title"));
        mRecipeMaterial.setText(getArguments().getString("recipe_material"));
        mRecipeQuantity.setText(getArguments().getString("recipe_quantity"));

        RecyclerView mRecipeStep = root.findViewById(R.id.recipe_recycle_text);
        mRecipeStep.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        mRecipeStep.setLayoutManager(layoutManager);
        mAdapter = new RecipeStepAdapter(this.getContext(), mRecipeSteps);

        if(mRecipeSteps != null){
            mRecipeStep.setVisibility(View.VISIBLE);
            mRecipeText.setVisibility(View.GONE);
            mRecipeStep.setAdapter(mAdapter);
        } else {
            mRecipeStep.setVisibility(View.GONE);
            mRecipeText.setVisibility(View.VISIBLE);
            mRecipeText.setText(getArguments().getString("recipe_text"));
        }

        return root;
    }

    private void getSteps(){
        String rawText = getArguments().getString("recipe_text");
        if("".equals(rawText)){
            return;
        } else {
            String[] getSteps = rawText.split("\n");
            this.mRecipeSteps = Arrays.asList(getSteps);
        }
    }

}
