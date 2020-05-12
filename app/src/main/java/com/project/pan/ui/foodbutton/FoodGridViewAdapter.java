package com.project.pan.ui.foodbutton;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.project.pan.R;
import com.project.pan.ui.viewpager.RecipeSaver;


import java.util.ArrayList;

public class FoodGridViewAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<RecipeSaver> mSavedRecipe;
    private LayoutInflater layoutInflater;

    public FoodGridViewAdapter(Context getContext, ArrayList<RecipeSaver> savedRecipe){
        this.mContext = getContext;
        this.mSavedRecipe = savedRecipe;
    }
    @Override
    public int getCount() {
        return mSavedRecipe.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View getView = layoutInflater.inflate(R.layout.foods_inside_items, container,false);

        ImageView setImage = getView.findViewById(R.id.inside_item_img);
        setImage.setImageResource(mSavedRecipe.get(position).getImgResource());

        getView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        container.addView(getView , 0);
        return getView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

}
