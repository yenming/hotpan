package com.project.pan.ui.viewpager;

public class RecipeSaver {
    private int mImage;
    private String mRecipeText;
    private String mRecipeTitle;
    private String mRecipeMaterial;
    private String mRecipeQuantity;

    public RecipeSaver(int imgResource, String recipeTitle, String recipeMaterial, String recipeQuantity, String recipeText) {
        super();
        this.mImage = imgResource;
        this.mRecipeTitle = recipeTitle;
        this.mRecipeMaterial = recipeMaterial;
        this.mRecipeQuantity = recipeQuantity;
        this.mRecipeText = recipeText;
    }

    int getImgResource(){
        return mImage;
    }
    String getRecipeTitle(){
        return mRecipeTitle;
    }
    String getRecipeMaterial(){ return mRecipeMaterial; }
    String getRecipeQuantity(){
        return mRecipeQuantity;
    }
    String getRecipeText(){
        return mRecipeText;
    }

}
