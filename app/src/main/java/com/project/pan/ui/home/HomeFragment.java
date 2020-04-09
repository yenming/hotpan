package com.project.pan.ui.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.project.pan.R;
import com.project.pan.ui.viewpager.RecipeSaver;
import com.project.pan.ui.viewpager.ViewPagerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Objects;


public class HomeFragment extends Fragment {

    private ArrayList<RecipeSaver> mSavedRecipe = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initImgResource();
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(this.getContext(), mSavedRecipe);

        ViewPager mPager = root.findViewById(R.id.imageViewPager);
        mPager.setAdapter(mAdapter);
        mPager.setPadding(100, 0, 100, 0);

        return root;
    }

    private void initImgResource() {
        mSavedRecipe.add(new RecipeSaver(R.drawable.dong_po_rou_1080x676, getString(R.string.recipe_mushroom_risotto)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.fennelandherbbarbecu_67598_16x9, getString(R.string.recipe_mushroom_risotto)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.fgoeufs_brouilles_6, getString(R.string.recipe_mushroom_risotto)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.fish_curry_09718_16x9, getString(R.string.recipe_mushroom_risotto)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.honey_orange_roast_sea_bass_with_lentils, getString(R.string.recipe_mushroom_risotto)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.recipe_image_legacy_id_423533_12, getString(R.string.recipe_mushroom_risotto)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.sauteed_fish_platter, getString(R.string.recipe_mushroom_risotto)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.smoky_hake_beans_greens_with_quick_garlic_mayonnaise, getString(R.string.recipe_mushroom_risotto)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.softened_sweet_onion_and_80481_16x9, getString(R.string.recipe_mushroom_risotto)));
    }
}
