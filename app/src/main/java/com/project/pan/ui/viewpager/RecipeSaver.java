package com.project.pan.ui.viewpager;

public class RecipeSaver {
    private int mImage;
    private String mRecipeText;

    public RecipeSaver(int imgResource, String recipeText) {
        super();
        this.mImage = imgResource;
        this.mRecipeText = recipeText;
    }

    public int getImgResource(){
        return mImage;
    }

    public String getRecipeText(){
        return mRecipeText;
    }

}
