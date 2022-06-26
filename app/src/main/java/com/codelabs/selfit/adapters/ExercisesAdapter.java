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

import java.util.ArrayList;

public class ExercisesAdapter extends ArrayAdapter<ExercisesModel> {
    ArrayList<ExercisesModel> ExercisesList;
    Activity mActivity;

    public ExercisesAdapter(Activity activity, ArrayList<ExercisesModel> ExercisesList) {
        super(activity, R.layout.row_exercises, ExercisesList);
        this.mActivity = activity;
        this.ExercisesList = ExercisesList;
    }

    @Override
    public int getCount() {
        return ExercisesList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View row = inflater.inflate(R.layout.row_exercises, null, true);

        TextView txtName = row.findViewById(R.id.txt_exName);
        TextView txtCals = row.findViewById(R.id.txt_exCals);
        TextView txtTimes = row.findViewById(R.id.txt_exTimes);

        try {
            txtName.setText(ExercisesList.get(position).getExName());
            txtCals.setText("Expected Burn:  " + ExercisesList.get(position).getExCalories());
            txtTimes.setText(ExercisesList.get(position).getExCount() + " Times");
        } catch (Exception ex) {
            Log.e(TAG, "getView Error: " + ex.getLocalizedMessage());
        }

        return row;
    }
}
