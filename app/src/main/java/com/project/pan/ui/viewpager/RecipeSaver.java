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

    public int getImgResource(){
        return mImage;
    }
    public String getRecipeTitle(){
        return mRecipeTitle;
    }
    public String getRecipeMaterial(){ return mRecipeMaterial; }
    public String getRecipeQuantity(){
        return mRecipeQuantity;
    }
    public String getRecipeText(){
        return mRecipeText;
    }

}
