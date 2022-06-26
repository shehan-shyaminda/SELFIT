package com.codelabs.selfit.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.codelabs.selfit.R;
import com.codelabs.selfit.helpers.CustomProgressDialog;
import com.codelabs.selfit.helpers.SharedPreferencesManager;
import com.codelabs.selfit.views.subviews.ViewExerciseActivity;
import com.codelabs.selfit.views.subviews.ViewMealActivity;
import com.codelabs.selfit.views.subviews.ViewPhysicActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class UserHomeFragment extends Fragment {

    private ConstraintLayout cons_view_exercise, cons_view_meal, cons_upload_photo;
    private FirebaseFirestore db;
    private CustomProgressDialog customProgressDialog;
    private SharedPreferencesManager sharedPreferencesManager;
    private TextView txtBPM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_home, container, false);

        cons_view_exercise = v.findViewById(R.id.cons_view_ex);
        cons_view_meal = v.findViewById(R.id.cons_view_meal);
        cons_upload_photo = v.findViewById(R.id.cons_upload_physic);
        txtBPM = v.findViewById(R.id.txtHeartRate);

        init();
        
        cons_view_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewExerciseActivity.class));
                Animatoo.animateSlideLeft(getActivity());
            }
        });

        cons_view_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewMealActivity.class));
                Animatoo.animateSlideLeft(getActivity());
            }
        });

        cons_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewPhysicActivity.class));
                Animatoo.animateSlideLeft(getActivity());
            }
        });

        return v;
    }

    private void init() {
    }
}