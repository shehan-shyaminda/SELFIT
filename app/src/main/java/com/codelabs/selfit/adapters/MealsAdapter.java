package com.codelabs.selfit.adapters;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codelabs.selfit.R;
import com.codelabs.selfit.models.ExercisesModel;
import com.codelabs.selfit.models.MealsModel;

import java.util.ArrayList;

public class MealsAdapter extends ArrayAdapter<MealsModel> {
    ArrayList<MealsModel> MealList;
    Activity mActivity;

    public MealsAdapter(Activity activity, ArrayList<MealsModel> MealList) {
        super(activity, R.layout.row_meals, MealList);
        this.mActivity = activity;
        this.MealList = MealList;
    }

    @Override
    public int getCount() {
        return MealList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View row = inflater.inflate(R.layout.row_meals, null, true);

        TextView txtName = row.findViewById(R.id.txt_mealName);
        TextView txtCals = row.findViewById(R.id.txt_mealCals);
        TextView txtTimes = row.findViewById(R.id.txt_much);

        try {
            txtName.setText(MealList.get(position).getMealName());
            txtCals.setText("Expected Calories:  " + MealList.get(position).getMealCalories());
            txtTimes.setText("- " + MealList.get(position).getMealCount() + " " + MealList.get(position).getMealUnit());
        } catch (Exception ex) {
            Log.e(TAG, "getView Error: " + ex.getLocalizedMessage());
        }

        return row;
    }
}
