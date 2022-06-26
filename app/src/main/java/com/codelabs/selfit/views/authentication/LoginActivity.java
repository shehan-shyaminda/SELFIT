package com.codelabs.selfit.views.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.codelabs.selfit.R;
import com.codelabs.selfit.helpers.CustomAlertDialog;
import com.codelabs.selfit.helpers.CustomProgressDialog;
import com.codelabs.selfit.helpers.SharedPreferencesManager;
import com.codelabs.selfit.views.MainActivity;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tomlonghurst.expandablehinttext.ExpandableHintText;

public class LoginActivity extends AppCompatActivity {

    private Button btnLog, btnLogin;
    private ExpandableHintText txtEmail, txtPassword;
    private FirebaseFirestore db;
    private CustomProgressDialog customProgressDialog;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLog = findViewById(R.id.btn_log_login);
        btnLogin = findViewById(R.id.btn_login);
        txtEmail = findViewById(R.id.txt_log_email);
        txtPassword = findViewById(R.id.txt_log_password);

        db = FirebaseFirestore.getInstance();
        customProgressDialog = new CustomProgressDialog(LoginActivity.this);
        sharedPreferencesManager = new SharedPreferencesManager(LoginActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                if (txtEmail.getText().toString().isEmpty() ||txtPassword.getText().toString().isEmpty()){
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeDismissAlert(LoginActivity.this, "Oops!", "Email & Password are required!", CFAlertDialog.CFAlertStyle.ALERT);
                }else{
                    db.collection("users").document(txtEmail.getText().toString().trim().toLowerCase()).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    customProgressDialog.dismissProgress();
                                    if (documentSnapshot.exists() && documentSnapshot.get("userPassword").toString().equals(txtPassword.getText().toString().trim())){
                                        clearFields();
                                        savePreferences(documentSnapshot.get("userAdminID").toString(), documentSnapshot.get("userTrainerID").toString(), txtEmail.getText().toString().trim().toLowerCase(), documentSnapshot.get("userRegDate").toString());
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        Animatoo.animateSlideLeft(LoginActivity.this);
                                        finishAffinity();
                                    }else{
                                        new CustomAlertDialog().negativeDismissAlert(LoginActivity.this, "Oops!", "Email or Password incorrect!", CFAlertDialog.CFAlertStyle.ALERT);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    customProgressDialog.dismissProgress();
                                    new CustomAlertDialog().negativeDismissAlert(LoginActivity.this, "Oops!", "Email or Password incorrect!", CFAlertDialog.CFAlertStyle.ALERT);
                                }
                            });
                }
            }
        });
    }

    private void savePreferences(String adminsID, String trainerID, String userID, String RegDate) {
        sharedPreferencesManager.savePreferences(SharedPreferencesManager.USER_LOGGED_IN, true);
        sharedPreferencesManager.savePreferences(SharedPreferencesManager.ADMINS_ID, adminsID);
        sharedPreferencesManager.savePreferences(SharedPreferencesManager.TRAINERS_ID, trainerID);
        sharedPreferencesManager.savePreferences(SharedPreferencesManager.USER_ID, userID);
        sharedPreferencesManager.savePreferences(SharedPreferencesManager.USER_REG_DATE, RegDate);
    }

    private void clearFields(){
        txtEmail.setText("");
        txtPassword.setText("");
    }
}