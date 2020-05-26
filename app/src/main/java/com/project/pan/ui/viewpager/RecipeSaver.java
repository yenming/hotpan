package com.project.pan.ui.viewpager;

public class RecipeSaver {
    private int mImage;
    private String mRecipeText;
    private String mRecipeTitle;
    private String mRecipeMaterial;
    private String mRecipeQuantity;
    private int mRecipeTime;
    private int mRecipeSteps;

    public RecipeSaver(int imgResource, String recipeTitle, String recipeMaterial, String recipeQuantity, String recipeText, int recipeTime, int recipeSteps) {
        super();
        this.mImage = imgResource;
        this.mRecipeTitle = recipeTitle;
        this.mRecipeMaterial = recipeMaterial;
        this.mRecipeQuantity = recipeQuantity;
        this.mRecipeText = recipeText;
        this.mRecipeTime = recipeTime;
        this.mRecipeSteps = recipeSteps;
    }

    public RecipeSaver(int imgResource, String recipeTitle, String recipeMaterial, String recipeQuantity, String recipeText, int recipeTime) {
        super();
        this.mImage = imgResource;
        this.mRecipeTitle = recipeTitle;
        this.mRecipeMaterial = recipeMaterial;
        this.mRecipeQuantity = recipeQuantity;
        this.mRecipeText = recipeText;
        this.mRecipeTime = recipeTime;
        this.mRecipeSteps = 10;
    }

    public RecipeSaver(int imgResource, String recipeTitle, String recipeMaterial, String recipeQuantity, String recipeText) {
        super();
        this.mImage = imgResource;
        this.mRecipeTitle = recipeTitle;
        this.mRecipeMaterial = recipeMaterial;
        this.mRecipeQuantity = recipeQuantity;
        this.mRecipeText = recipeText;
        this.mRecipeTime = 20;
        this.mRecipeSteps = 10;
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
    public int getRecipeTime(){
        return mRecipeTime;
    }
    public int getRecipeSteps(){
        return mRecipeSteps;
    }

}
