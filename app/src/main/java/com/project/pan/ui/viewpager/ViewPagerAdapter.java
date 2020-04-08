package com.project.pan.ui.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.project.pan.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Integer> drawableImgs;
    private LayoutInflater layoutInflater;

    public ViewPagerAdapter(Context getContext, ArrayList<Integer> imgsArray){
        this.mContext = getContext;
        this.drawableImgs = imgsArray;
    }

    @Override
    public int getCount() {
        return drawableImgs.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View getView = layoutInflater.inflate(R.layout.food_item_layout, container,false);

        ImageView setImage = getView.findViewById(R.id.fragment_image);
        setImage.setImageResource(drawableImgs.get(position));

        container.addView(getView , 0);
        return getView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

}
