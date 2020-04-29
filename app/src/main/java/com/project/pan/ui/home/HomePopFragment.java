package com.project.pan.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.project.pan.R;
import com.project.pan.ui.viewpager.RecipeSaver;
import com.project.pan.ui.viewpager.ViewPagerAdapter;

import java.util.ArrayList;


public class HomePopFragment extends Fragment {

    private ArrayList<RecipeSaver> mSavedRecipe = new ArrayList<>();
    private ViewPagerAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home_pop, container, false);

        if(mSavedRecipe.isEmpty()) {
            initImgResource();
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mAdapter = new ViewPagerAdapter(this.getContext(), mSavedRecipe);
        ViewPager mPager = root.findViewById(R.id.imageViewPager_Pop);
        mPager.setAdapter(mAdapter);
        mPager.setPadding(100, 0, 100, 0);

        return root;
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    private void initImgResource() {
        mSavedRecipe.add(new RecipeSaver(R.drawable.mushroom_risotto_recipe_1, getString(R.string.recipe_mushroom_risotto), getString(R.string.recipe_mushroom_risotto_material)
                , getString(R.string.recipe_mushroom_risotto_quantity), getString(R.string.recipe_mushroom_risotto_text)));
        /*mSavedRecipe.add(new RecipeSaver(R.drawable.softened_sweet_onion_and_80481_16x9, getString(R.string.recipe_braised_pork_belly), getString(R.string.recipe_braised_pork_belly_material)
                , getString(R.string.recipe_braised_pork_belly_quantity), getString(R.string.recipe_braised_pork_belly_text)));*/
        mSavedRecipe.add(new RecipeSaver(R.drawable.fgoeufs_brouilles_6, getString(R.string.recipe_french_scrambled_eggs), getString(R.string.recipe_french_scrambled_eggs_material)
                , getString(R.string.recipe_french_scrambled_eggs_quantity), getString(R.string.recipe_french_scrambled_eggs_text)));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.recipe_image_legacy_id_423533_12, "","","",""));
        mSavedRecipe.add(new RecipeSaver(R.drawable.fennelandherbbarbecu_67598_16x9, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.dong_po_rou_1080x676, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.fish_curry_09718_16x9, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.honey_orange_roast_sea_bass_with_lentils,"","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.sauteed_fish_platter, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.smoky_hake_beans_greens_with_quick_garlic_mayonnaise, "","","",""));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
