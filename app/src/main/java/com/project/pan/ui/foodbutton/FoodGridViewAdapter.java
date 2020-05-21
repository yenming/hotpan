package com.project.pan.ui.foodbutton;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.project.pan.R;
import com.project.pan.ui.viewpager.RecipeSaver;


import java.util.ArrayList;

public class FoodGridViewAdapter extends RecyclerView.Adapter<FoodGridViewAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<RecipeSaver> mSavedRecipe;
    private LayoutInflater layoutInflater;

    public FoodGridViewAdapter (Context context, ArrayList<RecipeSaver> getRecipe){
        this.mContext = context;
        this.mSavedRecipe = getRecipe;
    }
    @NonNull
    @Override
    public FoodGridViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.food_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodGridViewAdapter.ViewHolder holder, int position) {
        //Log.d("===fish===", "Img: "+mSavedRecipe.get(position).getImgResource()+"/ Title: "+mSavedRecipe.get(position).getRecipeTitle());
        holder.foodGridImg.setImageResource(mSavedRecipe.get(position).getImgResource());
        holder.recipeTitle.setText(mSavedRecipe.get(position).getRecipeTitle());
    }

    @Override
    public int getItemCount() {
        return mSavedRecipe.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodGridImg;
        TextView recipeTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            foodGridImg = (ImageView) itemView.findViewById(R.id.food_grid_img);
            recipeTitle = (TextView) itemView.findViewById(R.id.food_grid_recipetitle);
        }
    }
}
