package com.codelabs.selfit.views.subviews;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.codelabs.selfit.R;
import com.codelabs.selfit.adapters.ExercisesAdapter;
import com.codelabs.selfit.helpers.CustomProgressDialog;
import com.codelabs.selfit.helpers.SharedPreferencesManager;
import com.codelabs.selfit.models.ExercisesModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewExerciseActivity extends AppCompatActivity {

    private Intent i;
    private TextView txtEmpty;
    private ListView lstExercises;
    private CircleImageView btnBack;
    private FirebaseFirestore db;
    private CustomProgressDialog customProgressDialog;
    private SharedPreferencesManager sharedPreferencesManager;
    private ArrayList<ExercisesModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        txtEmpty = findViewById(R.id.txtEmpty_view_ex);
        lstExercises = findViewById(R.id.lst_view_ex);
        btnBack = findViewById(R.id.btn_view_ex_back);

        db = FirebaseFirestore.getInstance();
        customProgressDialog = new CustomProgressDialog(ViewExerciseActivity.this);
        sharedPreferencesManager = new SharedPreferencesManager(ViewExerciseActivity.this);

        init();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lstExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ViewExerciseActivity.this, PlayExerciseActivity.class);
                intent.putExtra("url", list.get(i).getExURL());
                startActivity(intent);
            }
        });
    }

    private void init() {
        customProgressDialog.createProgress();
        lstExercises.setAdapter(null);
        list = new ArrayList<>();

        Log.e(TAG, "init: " + sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID));
        db.collection("trainerAssignWorkout").document(sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            for(int i=1;i<=3;i++){
                                String count = task.getResult().get(String.valueOf("count0"+i)).toString();
                                db.collection("admins").document(sharedPreferencesManager.getPreferences(SharedPreferencesManager.TRAINERS_ID))
                                        .collection("exercises").document(task.getResult().get(String.valueOf("ex0"+i)).toString())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                Log.e(TAG, "onComplete: " + documentSnapshot.get("exName").toString() );
                                                list.add(new ExercisesModel(documentSnapshot.get("exCalories").toString(), documentSnapshot.get("exName").toString(), documentSnapshot.get("exVideoPath").toString(), count));

                                                Log.e(TAG, "onComplete: " + list.toString() );
                                                customProgressDialog.dismissProgress();
                                                if (list.isEmpty()){
                                                    lstExercises.setEnabled(false);
                                                    lstExercises.setVisibility(View.GONE);
                                                    txtEmpty.setEnabled(true);
                                                    txtEmpty.setVisibility(View.VISIBLE);
                                                }else{
                                                    txtEmpty.setEnabled(false);
                                                    txtEmpty.setVisibility(View.GONE);
                                                    lstExercises.setEnabled(true);
                                                    lstExercises.setVisibility(View.VISIBLE);

                                                    ExercisesAdapter listAdapter = new ExercisesAdapter(ViewExerciseActivity.this, list);
                                                    lstExercises.setAdapter(listAdapter);
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