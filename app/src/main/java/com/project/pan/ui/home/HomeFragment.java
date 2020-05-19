package com.project.pan.ui.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
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
import com.project.pan.ui.foodbutton.FishFoodFragment;
import com.project.pan.ui.viewpager.RecipeSaver;
import com.project.pan.ui.viewpager.ViewPagerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Objects;


public class HomeFragment extends Fragment implements View.OnClickListener{

    private ArrayList<RecipeSaver> mSavedRecipe = new ArrayList<>();
    private ArrayList<RecipeSaver> allRecipe = new ArrayList<>();
    private ViewPagerAdapter mAdapter;
    private ViewPager mPager;
    private Button mHomeFragmentBtn, mHomePopFragmentBtn, mHomeStepFragmentBtn;
    private Button foodFishBtn;
    private TextView mRecipeTitle;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        if(mSavedRecipe.isEmpty()) {
            initImgResource();
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mAdapter = new ViewPagerAdapter(this.getContext(), mSavedRecipe);
        mPager = root.findViewById(R.id.imageViewPager);
        mPager.setAdapter(mAdapter);
        mPager.setPadding(100, 0, 100, 0);

        mRecipeTitle = root.findViewById(R.id.recipe_name);
        mRecipeTitle.setText(mSavedRecipe.get(0).getRecipeTitle());
        mHomeFragmentBtn = (Button) root.findViewById(R.id.recommendable_btn);
        mHomePopFragmentBtn = (Button) root.findViewById(R.id.popular_btn);
        mHomeStepFragmentBtn = (Button) root.findViewById(R.id.steps_btn);
        foodFishBtn = (Button) root.findViewById(R.id.imagefish);
        mHomeFragmentBtn.setOnClickListener(this);
        mHomePopFragmentBtn.setOnClickListener(this);
        mHomeStepFragmentBtn.setOnClickListener(this);
        foodFishBtn.setOnClickListener(this);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mRecipeTitle.setText(mSavedRecipe.get(position).getRecipeTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return root;
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    private void initImgResource() {
        mSavedRecipe.add(new RecipeSaver(R.drawable.mushroom_risotto_recipe_1, getString(R.string.recipe_mushroom_risotto), getString(R.string.recipe_mushroom_risotto_material)
                , getString(R.string.recipe_mushroom_risotto_quantity), getString(R.string.recipe_mushroom_risotto_text)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.softened_sweet_onion_and_80481_16x9, getString(R.string.recipe_braised_pork_belly), getString(R.string.recipe_braised_pork_belly_material)
                , getString(R.string.recipe_braised_pork_belly_quantity), getString(R.string.recipe_braised_pork_belly_text)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.fgoeufs_brouilles_6, getString(R.string.recipe_french_scrambled_eggs), getString(R.string.recipe_french_scrambled_eggs_material)
                , getString(R.string.recipe_french_scrambled_eggs_quantity), getString(R.string.recipe_french_scrambled_eggs_text)));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.recipe_image_legacy_id_423533_12, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.fennelandherbbarbecu_67598_16x9, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.dong_po_rou_1080x676, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.fish_curry_09718_16x9, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.honey_orange_roast_sea_bass_with_lentils,"","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.sauteed_fish_platter, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.smoky_hake_beans_greens_with_quick_garlic_mayonnaise, "","","",""));
    }

    private void popularImgResource() {
        /*mSavedRecipe.add(new RecipeSaver(R.drawable.mushroom_risotto_recipe_1, getString(R.string.recipe_mushroom_risotto), getString(R.string.recipe_mushroom_risotto_material)
                , getString(R.string.recipe_mushroom_risotto_quantity), getString(R.string.recipe_mushroom_risotto_text)));*/
        mSavedRecipe.add(new RecipeSaver(R.drawable.softened_sweet_onion_and_80481_16x9, getString(R.string.recipe_braised_pork_belly), getString(R.string.recipe_braised_pork_belly_material)
                , getString(R.string.recipe_braised_pork_belly_quantity), getString(R.string.recipe_braised_pork_belly_text)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.fgoeufs_brouilles_6, getString(R.string.recipe_french_scrambled_eggs), getString(R.string.recipe_french_scrambled_eggs_material)
                , getString(R.string.recipe_french_scrambled_eggs_quantity), getString(R.string.recipe_french_scrambled_eggs_text)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.recipe_image_legacy_id_423533_12, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.fennelandherbbarbecu_67598_16x9, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.dong_po_rou_1080x676, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.fish_curry_09718_16x9, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.honey_orange_roast_sea_bass_with_lentils,"","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.sauteed_fish_platter, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.smoky_hake_beans_greens_with_quick_garlic_mayonnaise, "","","",""));
    }

    private void stepsImgResource() {
        /*mSavedRecipe.add(new RecipeSaver(R.drawable.mushroom_risotto_recipe_1, getString(R.string.recipe_mushroom_risotto), getString(R.string.recipe_mushroom_risotto_material)
                , getString(R.string.recipe_mushroom_risotto_quantity), getString(R.string.recipe_mushroom_risotto_text)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.softened_sweet_onion_and_80481_16x9, getString(R.string.recipe_braised_pork_belly), getString(R.string.recipe_braised_pork_belly_material)
                , getString(R.string.recipe_braised_pork_belly_quantity), getString(R.string.recipe_braised_pork_belly_text)));*/
        mSavedRecipe.add(new RecipeSaver(R.drawable.fgoeufs_brouilles_6, getString(R.string.recipe_french_scrambled_eggs), getString(R.string.recipe_french_scrambled_eggs_material)
                , getString(R.string.recipe_french_scrambled_eggs_quantity), getString(R.string.recipe_french_scrambled_eggs_text)));
        mSavedRecipe.add(new RecipeSaver(R.drawable.recipe_image_legacy_id_423533_12, "","","",""));
        mSavedRecipe.add(new RecipeSaver(R.drawable.fennelandherbbarbecu_67598_16x9, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.dong_po_rou_1080x676, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.fish_curry_09718_16x9, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.honey_orange_roast_sea_bass_with_lentils,"","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.sauteed_fish_platter, "","","",""));
        //mSavedRecipe.add(new RecipeSaver(R.drawable.smoky_hake_beans_greens_with_quick_garlic_mayonnaise, "","","",""));
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.popular_btn:
                mSavedRecipe.clear();
                popularImgResource();
                mAdapter.notifyDataSetChanged();
                this.getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
            case R.id.recommendable_btn:
                mSavedRecipe.clear();
                initImgResource();
                mAdapter.notifyDataSetChanged();
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
            case R.id.steps_btn:
                mSavedRecipe.clear();
                stepsImgResource();
                mAdapter.notifyDataSetChanged();
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
            case R.id.imagefish:
                //allResource();
                Bundle bundle = new Bundle();
                bundle.putBoolean("food_fish", true);
                EventBus.getDefault().post(bundle);
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
