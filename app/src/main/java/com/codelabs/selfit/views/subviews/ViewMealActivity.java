package com.codelabs.selfit.views.subviews;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.codelabs.selfit.R;
import com.codelabs.selfit.adapters.ExercisesAdapter;
import com.codelabs.selfit.adapters.MealsAdapter;
import com.codelabs.selfit.helpers.CustomProgressDialog;
import com.codelabs.selfit.helpers.SharedPreferencesManager;
import com.codelabs.selfit.models.ExercisesModel;
import com.codelabs.selfit.models.MealsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewMealActivity extends AppCompatActivity {

    private Intent i;
    private TextView txtEmpty;
    private ListView lstMeals;
    private CircleImageView btnBack;
    private FirebaseFirestore db;
    private CustomProgressDialog customProgressDialog;
    private SharedPreferencesManager sharedPreferencesManager;
    private ArrayList<MealsModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meal);

        txtEmpty = findViewById(R.id.txtEmpty_view_meal);
        lstMeals = findViewById(R.id.lst_view_meal);
        btnBack = findViewById(R.id.btn_view_meal_back);

        db = FirebaseFirestore.getInstance();
        customProgressDialog = new CustomProgressDialog(ViewMealActivity.this);
        sharedPreferencesManager = new SharedPreferencesManager(ViewMealActivity.this);

        init();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {
        customProgressDialog.createProgress();
        lstMeals.setAdapter(null);
        list = new ArrayList<>();

        Log.e(TAG, "init: " + sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID));
        db.collection("trainerAssignMeal").document(sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            for(int i=1;i<=3;i++){
                                String count = task.getResult().get(String.valueOf("measure0"+i)).toString();
                                db.collection("admins").document(sharedPreferencesManager.getPreferences(SharedPreferencesManager.TRAINERS_ID))
                                        .collection("meals").document(task.getResult().get(String.valueOf("meal0"+i)).toString())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                Log.e(TAG, "onComplete: " + documentSnapshot.get("mealName").toString() );
                                                list.add(new MealsModel(documentSnapshot.get("mealName").toString(), documentSnapshot.get("mealCalories").toString(),
                                                        documentSnapshot.get("mealUnit").toString(), count));

                                                Log.e(TAG, "onComplete: " + list.toString() );
                                                customProgressDialog.dismissProgress();
                                                if (list.isEmpty()){
                                                    lstMeals.setEnabled(false);
                                                    lstMeals.setVisibility(View.GONE);
                                                    txtEmpty.setEnabled(true);
                                                    txtEmpty.setVisibility(View.VISIBLE);
                                                }else{
                                                    txtEmpty.setEnabled(false);
                                                    txtEmpty.setVisibility(View.GONE);
                                                    lstMeals.setEnabled(true);
                                                    lstMeals.setVisibility(View.VISIBLE);

                                                    MealsAdapter listAdapter = new MealsAdapter(ViewMealActivity.this, list);
                                                    lstMeals.setAdapter(listAdapter);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "onFailure: " + e.getLocalizedMessage() );
                                            }
                                        });
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customProgressDialog.dismissProgress();
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                    }
                });
    }
}