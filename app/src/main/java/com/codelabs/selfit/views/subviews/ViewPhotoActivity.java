package com.codelabs.selfit.views.subviews;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codelabs.selfit.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPhotoActivity extends AppCompatActivity {

    private Intent i;
    private CircleImageView btnBack;
    private String imgUrl;
    private ImageView imgPhysic;
    private TextView txtdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        txtdate = findViewById(R.id.txt_uploaded_physic);
        imgPhysic = findViewById(R.id.img_physic);
        btnBack = findViewById(R.id.btn_view_physic_back);

        i = getIntent();
        imgUrl = i.getStringExtra("url");
        txtdate.setText(i.getStringExtra("date"));
        Log.e(TAG, "onCreate: " + i.getStringExtra("url"));

        init();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init(){
        Picasso.get()
                .load(imgUrl)
                .into(imgPhysic);
    }
}