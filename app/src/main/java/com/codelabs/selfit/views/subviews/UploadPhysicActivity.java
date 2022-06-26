package com.codelabs.selfit.views.subviews;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.codelabs.selfit.R;
import com.codelabs.selfit.helpers.CustomAlertDialog;
import com.codelabs.selfit.helpers.CustomProgressDialog;
import com.codelabs.selfit.helpers.SharedPreferencesManager;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadPhysicActivity extends AppCompatActivity {

    private ImageView img_uploaded;
    private Button btnChoose, btnUpload;
    private SharedPreferencesManager sharedPreferencesManager;
    private CustomProgressDialog customProgressDialog;
    private FirebaseFirestore db;
    private Uri imageUri;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_physic);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("userPhysics");
        sharedPreferencesManager = new SharedPreferencesManager(UploadPhysicActivity.this);
        customProgressDialog = new CustomProgressDialog(UploadPhysicActivity.this);

        img_uploaded = findViewById(R.id.upload_img);
        btnChoose = findViewById(R.id.btn_choose_img);
        btnUpload = findViewById(R.id.btn_upload_img);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i,45);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                if (imageUri == null){
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeDismissAlert(UploadPhysicActivity.this, "Oops!", "Please fill out all the\nrequired inputs!", CFAlertDialog.CFAlertStyle.ALERT);
                }else{
                    final StorageReference ref = storageRef.child(String.valueOf(System.currentTimeMillis()));
                    UploadTask uploadTask = ref.putFile(imageUri);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                db.collection("users").document(sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID))
                                        .collection("userPhysics").add(mapData(downloadUri.toString()))
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                customProgressDialog.dismissProgress();
                                                Log.e(TAG, "onSuccess: " + downloadUri.toString());
                                                clear();
                                                new CustomAlertDialog().positiveAlert(UploadPhysicActivity.this, "Done!", "New physical image added!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                customProgressDialog.dismissProgress();
                                                new CustomAlertDialog().negativeAlert(UploadPhysicActivity.this, "Done!", "Something went wrong.\nPlease try again later!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                            }
                                        });

                            } else {
                                customProgressDialog.dismissProgress();
                                new CustomAlertDialog().negativeAlert(UploadPhysicActivity.this, "Oops!", "Something went wrong.\nPlease try again later!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==45 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            img_uploaded.setImageURI(imageUri);
        }else{
            Log.e(TAG, "onActivityResult: error");
        }
    }

    private Map mapData(String url) {
        Map<String, Object> map = new HashMap<>();
        map.put("imageUrl", url);
        map.put("uploadedDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        return map;
    }

    private void clear(){
        img_uploaded.setImageURI(null);
    }
}