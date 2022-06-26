package com.codelabs.selfit.models;

public class MealsModel {
    String mealName, mealCalories, mealUnit, mealCount;

    public MealsModel(String mealName, String mealCalories, String mealUnit, String mealCount) {
        this.mealName = mealName;
        this.mealCalories = mealCalories;
        this.mealUnit = mealUnit;
        this.mealCount = mealCount;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealCalories() {
        return mealCalories;
    }

    public void setMealCalories(String mealCalories) {
        this.mealCalories = mealCalories;
    }

    public String getMealUnit() {
        return mealUnit;
    }

    public void setMealUnit(String mealUnit) {
        this.mealUnit = mealUnit;
    }

    public String getMealCount() {
        return mealCount;
    }

    public void setMealCount(String mealCount) {
        this.mealCount = mealCount;
    }

    @Override
    public String toString() {
        return getMealName();
    }
}
