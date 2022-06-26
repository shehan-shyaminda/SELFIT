package com.codelabs.selfit.models;

public class ExercisesModel {
    String exCalories, exName, exURL, exCount;

    public ExercisesModel(String exCalories, String exName, String exURL, String exCount) {
        this.exCalories = exCalories;
        this.exName = exName;
        this.exURL = exURL;
        this.exCount = exCount;
    }

    public String getExCalories() {
        return exCalories;
    }

    public void setExCalories(String exCalories) {
        this.exCalories = exCalories;
    }

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public String getExURL() {
        return exURL;
    }

    public void setExURL(String exURL) {
        this.exURL = exURL;
    }

    public String getExCount() {
        return exCount;
    }

    public void setExCount(String exCount) {
        this.exCount = exCount;
    }

    @Override
    public String toString() {
        return getExName();
    }
}
