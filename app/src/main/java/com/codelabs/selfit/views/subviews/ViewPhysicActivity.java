package com.codelabs.selfit.views.subviews;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.codelabs.selfit.R;
import com.codelabs.selfit.adapters.PhysicAdapter;
import com.codelabs.selfit.helpers.CustomProgressDialog;
import com.codelabs.selfit.helpers.SharedPreferencesManager;
import com.codelabs.selfit.models.PhysicModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPhysicActivity extends AppCompatActivity {

    private Button addPhysic;
    private CircleImageView btnBack;
    private SharedPreferencesManager sharedPreferencesManager;
    private CustomProgressDialog customProgressDialog;
    private FirebaseFirestore db;
    private TextView txtEmpty;
    private ListView lstPhysics;
    private ArrayList<PhysicModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_physic);

        db = FirebaseFirestore.getInstance();
        sharedPreferencesManager = new SharedPreferencesManager(ViewPhysicActivity.this);
        customProgressDialog = new CustomProgressDialog(ViewPhysicActivity.this);

        addPhysic = findViewById(R.id.btn_add_physic);
        btnBack = findViewById(R.id.btn_view_physic_back);
        txtEmpty = findViewById(R.id.txtEmptyPhysic);
        lstPhysics = findViewById(R.id.lst_physics);

//        init();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lstPhysics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ViewPhysicActivity.this, ViewPhotoActivity.class);
                intent.putExtra("url", list.get(i).getImageUrl());
                intent.putExtra("date", list.get(i).getUploadedDate().replace("/","-"));
                startActivity(intent);
            }
        });

        addPhysic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewPhysicActivity.this, UploadPhysicActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        customProgressDialog.createProgress();
        lstPhysics.setAdapter(null);
        list = new ArrayList<>();

        db.collection("users").document(sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID))
                .collection("userPhysics")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()){
                            Log.e(TAG, "onSuccess: Empty Collection");
                            lstPhysics.setEnabled(false);
                            lstPhysics.setVisibility(View.GONE);
                            txtEmpty.setEnabled(true);
                            txtEmpty.setVisibility(View.VISIBLE);
                        }
                        else{
                            DocumentSnapshot snapsList;
                            for(int i = 0; i < task.getResult().getDocuments().size(); i++){
                                snapsList = task.getResult().getDocuments().get(i);
                                list.add(new PhysicModel(snapsList.get("imageUrl").toString(), snapsList.get("uploadedDate").toString()));
                            }

                            txtEmpty.setEnabled(false);
                            txtEmpty.setVisibility(View.GONE);
                            lstPhysics.setEnabled(true);
                            lstPhysics.setVisibility(View.VISIBLE);

                            PhysicAdapter listAdapter = new PhysicAdapter(ViewPhysicActivity.this, list);
                            lstPhysics.setAdapter(listAdapter);
                        }
                        customProgressDialog.dismissProgress();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customProgressDialog.dismissProgress();
                        Log.e(TAG, "onFailure: " + e);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}