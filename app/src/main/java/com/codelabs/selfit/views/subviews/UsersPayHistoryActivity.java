package com.codelabs.selfit.views.subviews;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.codelabs.selfit.R;
import com.codelabs.selfit.adapters.PaymentHistoryAdapter;
import com.codelabs.selfit.helpers.CustomProgressDialog;
import com.codelabs.selfit.helpers.SharedPreferencesManager;
import com.codelabs.selfit.models.PaymentHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersPayHistoryActivity extends AppCompatActivity {

    private SharedPreferencesManager sharedPreferencesManager;
    private CustomProgressDialog customProgressDialog;
    private CircleImageView btnBack;
    private FirebaseFirestore db;
    private TextView txtEmpty;
    private ListView lstHistory;
    private ArrayList<PaymentHistory> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_pay_history);

        db = FirebaseFirestore.getInstance();
        sharedPreferencesManager = new SharedPreferencesManager(UsersPayHistoryActivity.this);
        customProgressDialog = new CustomProgressDialog(UsersPayHistoryActivity.this);

        btnBack = findViewById(R.id.btn_pay_his_back);
        txtEmpty = findViewById(R.id.txtEmpty_pay_his);
        lstHistory = findViewById(R.id.lst_pay_his);

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
        lstHistory.setAdapter(null);
        list = new ArrayList<>();

        db.collection("userMakesPayment")
                .whereEqualTo("usersID", sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()){
                            Log.e(TAG, "onSuccess: Empty Collection");
                            lstHistory.setEnabled(false);
                            lstHistory.setVisibility(View.GONE);
                            txtEmpty.setEnabled(true);
                            txtEmpty.setVisibility(View.VISIBLE);
                        }
                        else{
                            DocumentSnapshot snapsList;
                            for(int i = 0; i < task.getResult().getDocuments().size(); i++){
                                snapsList = task.getResult().getDocuments().get(i);
                                list.add(new PaymentHistory(snapsList.get("adminsID").toString(), snapsList.get("usersID").toString(), snapsList.get("transAmount").toString(),
                                        snapsList.get("transDate").toString(), snapsList.get("transRef").toString()));
                            }

                            txtEmpty.setEnabled(false);
                            txtEmpty.setVisibility(View.GONE);
                            lstHistory.setEnabled(true);
                            lstHistory.setVisibility(View.VISIBLE);

                            PaymentHistoryAdapter listAdapter = new PaymentHistoryAdapter(UsersPayHistoryActivity.this, list);
                            lstHistory.setAdapter(listAdapter);
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
}