package com.codelabs.selfit.views.subviews;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.codelabs.selfit.R;
import com.codelabs.selfit.helpers.CustomProgressDialog;

public class PlayExerciseActivity extends AppCompatActivity {

    private VideoView videoView;
    private Uri videoUri;
    private MediaController mediaController;
    private Intent i;
    private CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_exercise);

        i = getIntent();
        videoUri = Uri.parse(i.getStringExtra("url"));
        Log.e(TAG, "onCreate: " + i.getStringExtra("url"));

        videoView = findViewById(R.id.videoView);

        customProgressDialog = new CustomProgressDialog(PlayExerciseActivity.this);

        mediaController = new MediaController(PlayExerciseActivity.this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.setVideoURI(videoUri);
        videoView.start();
        customProgressDialog.createProgress();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                customProgressDialog.dismissProgress();
            }
        });

    }
}