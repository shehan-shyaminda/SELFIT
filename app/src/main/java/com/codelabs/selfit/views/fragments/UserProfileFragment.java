package com.codelabs.selfit.views.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.codelabs.selfit.R;
import com.codelabs.selfit.helpers.CustomProgressDialog;
import com.codelabs.selfit.helpers.SharedPreferencesManager;
import com.codelabs.selfit.views.authentication.LoginActivity;
import com.codelabs.selfit.views.subviews.EditUserActivity;
import com.codelabs.selfit.views.subviews.PrivacyPolicyActivity;
import com.codelabs.selfit.views.subviews.UsersPayHistoryActivity;
import com.codelabs.selfit.views.subviews.UsersPayNowActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileFragment extends Fragment {

    private ConstraintLayout consSignout, consResetPW, consPrivacyPolicy, consPayHistory, consPayNow;
    private TextView txtName;
    private FirebaseFirestore db;
    private CustomProgressDialog customProgressDialog;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);

        consSignout = v.findViewById(R.id.cons_signout);
        consResetPW = v.findViewById(R.id.cons_edit_profile);
        consPrivacyPolicy = v.findViewById(R.id.cons_privacy_policy);
        consPayHistory = v.findViewById(R.id.cons_pay_history);
        consPayNow = v.findViewById(R.id.cons_payHere);
        txtName = v.findViewById(R.id.txt_name);

        db = FirebaseFirestore.getInstance();
        customProgressDialog = new CustomProgressDialog(getActivity());
        sharedPreferencesManager = new SharedPreferencesManager(getActivity());

        init();

        consSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();

                sharedPreferencesManager.clearPreferences(SharedPreferencesManager.ADMINS_ID);
                sharedPreferencesManager.clearPreferences(SharedPreferencesManager.TRAINERS_ID);
                sharedPreferencesManager.clearPreferences(SharedPreferencesManager.USER_ID);
                sharedPreferencesManager.clearPreferences(SharedPreferencesManager.USER_LOGGED_IN);

                customProgressDialog.dismissProgress();

                startActivity(new Intent(getActivity(), LoginActivity.class));
                Animatoo.animateSlideRight(getActivity());
                getActivity().finishAffinity();
            }
        });

        consResetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditUserActivity.class));
                Animatoo.animateSlideLeft(getActivity());
            }
        });

        consPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
                Animatoo.animateSlideLeft(getActivity());
            }
        });

        consPayHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UsersPayHistoryActivity.class));
                Animatoo.animateSlideLeft(getActivity());
            }
        });

        consPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UsersPayNowActivity.class));
                Animatoo.animateSlideLeft(getActivity());
            }
        });

        return v;
    }

    private void init() {
        customProgressDialog.createProgress();
        db.collection("users").document(sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID)).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            if(task.getResult().get("usersName").toString().isEmpty()){
                                customProgressDialog.dismissProgress();
                                Log.e(TAG, "onComplete: " + sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID) );
                                txtName.setText(sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID));
                            }else{
                                customProgressDialog.dismissProgress();
                                txtName.setText(task.getResult().get("usersName").toString());
                            }
                        }else{
                            customProgressDialog.dismissProgress();
                            txtName.setText(sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID));
                            Log.e(TAG, "onComplete: " + sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID) );
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customProgressDialog.dismissProgress();
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage() );
//                        new CustomAlertDialog().negativeDismissAlert(getActivity(), "Oops!", "Something went wrong\nPlease try again later!", CFAlertDialog.CFAlertStyle.ALERT);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }
}