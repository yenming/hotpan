package com.project.pan.ui.foodbutton;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.pan.R;
import com.project.pan.ui.viewpager.RecipeSaver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        mRecipeSaver.add(new RecipeSaver(R.drawable.mushroom_risotto_recipe_1, getString(R.string.recipe_mushroom_risotto), getString(R.string.recipe_mushroom_risotto_material)
                , getString(R.string.recipe_mushroom_risotto_quantity), getString(R.string.recipe_mushroom_risotto_text)));
        mRecipeSaver.add(new RecipeSaver(R.drawable.softened_sweet_onion_and_80481_16x9, getString(R.string.recipe_braised_pork_belly), getString(R.string.recipe_braised_pork_belly_material)
                , getString(R.string.recipe_braised_pork_belly_quantity), getString(R.string.recipe_braised_pork_belly_text)));
        mRecipeSaver.add(new RecipeSaver(R.drawable.recipe_image_legacy_id_423533_12, "","","",""));
        mRecipeSaver.add(new RecipeSaver(R.drawable.fennelandherbbarbecu_67598_16x9, "","","",""));
        mRecipeSaver.add(new RecipeSaver(R.drawable.dong_po_rou_1080x676, "","","",""));
        mRecipeSaver.add(new RecipeSaver(R.drawable.fish_curry_09718_16x9, "","","",""));
        mRecipeSaver.add(new RecipeSaver(R.drawable.honey_orange_roast_sea_bass_with_lentils,"","","",""));
        mRecipeSaver.add(new RecipeSaver(R.drawable.sauteed_fish_platter, "","","",""));
        mRecipeSaver.add(new RecipeSaver(R.drawable.smoky_hake_beans_greens_with_quick_garlic_mayonnaise, "","","",""));
        mRecipeSaver.add(new RecipeSaver(R.drawable.fgoeufs_brouilles_6, getString(R.string.recipe_french_scrambled_eggs), getString(R.string.recipe_french_scrambled_eggs_material)
                , getString(R.string.recipe_french_scrambled_eggs_quantity), getString(R.string.recipe_french_scrambled_eggs_text)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
