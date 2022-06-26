package com.codelabs.selfit.views.subviews;

import static com.google.firebase.database.core.RepoManager.clear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codelabs.selfit.R;
import com.codelabs.selfit.helpers.CustomAlertDialog;
import com.codelabs.selfit.helpers.CustomProgressDialog;
import com.codelabs.selfit.helpers.SharedPreferencesManager;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tomlonghurst.expandablehinttext.ExpandableHintText;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserActivity extends AppCompatActivity {

    private CircleImageView btnBack;
    private ExpandableHintText txtUserName, txtUserPW;
    private Button btnUpdate;
    private FirebaseFirestore db;
    private CustomProgressDialog customProgressDialog;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        btnBack = findViewById(R.id.btn_edit_user);
        txtUserName = findViewById(R.id.txt_user_name);
        txtUserPW = findViewById(R.id.txt_user_pw);
        btnUpdate = findViewById(R.id.btn_update_user);

        db = FirebaseFirestore.getInstance();
        customProgressDialog = new CustomProgressDialog(EditUserActivity.this);
        sharedPreferencesManager = new SharedPreferencesManager(EditUserActivity.this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                if(txtUserName.getText().isEmpty() && txtUserPW.getText().isEmpty()){
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeDismissAlert(EditUserActivity.this, "Oops!", "Please fill out all the\nrequired inputs!", CFAlertDialog.CFAlertStyle.ALERT);
                }else{
                    db.collection("users").document(sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID))
                            .update(mapData(txtUserName.getText(), txtUserPW.getText()))
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    customProgressDialog.dismissProgress();
                                    clear();
                                    new CustomAlertDialog().positiveAlert(EditUserActivity.this, "Congrats!", "Profile updated!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    customProgressDialog.dismissProgress();
                                    new CustomAlertDialog().negativeAlert(EditUserActivity.this, "Oops!", "Something went wrong.\nPlease try again later!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                }
                            });
                }
            }
        });
    }

    private Map mapData(String name, String password) {
        Map<String, Object> map = new HashMap<>();
        if (!name.isEmpty() && !password.isEmpty()){
            map.put("usersName", name);
            map.put("userPassword", password);
        }else if (name.isEmpty()){
            map.put("userPassword", password);
        }else {
            map.put("usersName", name);
        }

        return map;
    }

    private void clear(){
        txtUserName.setText("");
        txtUserPW.setText("");
    }
}