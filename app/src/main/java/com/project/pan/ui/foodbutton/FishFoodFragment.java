package com.project.pan.ui.foodbutton;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.pan.R;
import com.project.pan.ui.viewpager.RecipeSaver;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FishFoodFragment extends Fragment {
    private ArrayList<RecipeSaver> mRecipeSaver  = new ArrayList<>();
    private FoodGridViewAdapter mAdapter;
    private RecyclerView mRecipeGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fish, container, false);
        allResource();
        mRecipeGrid = (RecyclerView) root.findViewById(R.id.fish_grid);
        mRecipeGrid.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecipeGrid.setLayoutManager(gridLayoutManager);
        mAdapter = new FoodGridViewAdapter(this.getContext(), mRecipeSaver);
        mRecipeGrid.setAdapter(mAdapter);
        return root;
    }

    private void allResource() {
        mRecipeSaver.add(new RecipeSaver(R.drawable.landscape_gettyimages, getString(R.string.recipe_roast_salmon), "", "", "", 50, 10));
        mRecipeSaver.add(new RecipeSaver(R.drawable.softened_sweet_onion_and_80481_16x9, getString(R.string.recipe_onion_fried_fish), "", "","", 20, 10));
        mRecipeSaver.add(new RecipeSaver(R.drawable.smoky_hake_beans_greens_with_quick_garlic_mayonnaise, getString(R.string.recipe_smoke_hake_beans),"","","", 20));
        mRecipeSaver.add(new RecipeSaver(R.drawable.honey_orange_roast_sea_bass_with_lentils,getString(R.string.recipe_honey_orange_roast_sea_bass),"","","", 25));
        mRecipeSaver.add(new RecipeSaver(R.drawable.fennelandherbbarbecu_67598_16x9, getString(R.string.recipe_fennel_herb_barbecued_fish),"","","", 50));
        mRecipeSaver.add(new RecipeSaver(R.drawable.recipe_smoked_trout_fish_pies, getString(R.string.recipe_smoked_fish_pies),"","","", 50));
        mRecipeSaver.add(new RecipeSaver(R.drawable.fish_curry_09718_16x9, getString(R.string.recipe_fish_curry),"","","", 50));
        mRecipeSaver.add(new RecipeSaver(R.drawable.sauteed_fish_platter, getString(R.string.recipe_sauteed_fish_platter),"","","", 50));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
