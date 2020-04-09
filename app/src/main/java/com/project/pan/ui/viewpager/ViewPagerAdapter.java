package com.project.pan.ui.viewpager;

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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<RecipeSaver> mSavedRecipe;
    private LayoutInflater layoutInflater;

    public ViewPagerAdapter(Context getContext, ArrayList<RecipeSaver> savedRecipe){
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
        View getView = layoutInflater.inflate(R.layout.food_item_layout, container,false);

        ImageView setImage = getView.findViewById(R.id.fragment_image);
        setImage.setImageResource(mSavedRecipe.get(position).getImgResource());

        getView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Log.d("=====Adapter=====", mSavedRecipe.get(position).getImgResource()+
                        " / "+ mSavedRecipe.get(position).getRecipeText());
                bundle.putInt("recipe_img", mSavedRecipe.get(position).getImgResource());
                bundle.putString("recipe_text", mSavedRecipe.get(position).getRecipeText());
                bundle.putBoolean("recipe", true);
                EventBus.getDefault().post(bundle);
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
