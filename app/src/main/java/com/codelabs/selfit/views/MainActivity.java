package com.codelabs.selfit.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codelabs.selfit.R;
import com.codelabs.selfit.views.fragments.UserHomeFragment;
import com.codelabs.selfit.views.fragments.UserProfileFragment;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends BaseActivity {

    private AnimatedBottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar = findViewById(R.id.adminsBottomBar);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_admins, new UserHomeFragment()).commit();
        bottomBar.selectTabById(R.id.home,true);

        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                Fragment fragment = null;
                switch (tab1.getId()) {
                    case R.id.home:
                        fragment = new UserHomeFragment();
                        break;
                    case R.id.profile:
                        fragment = new UserProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_admins, fragment).commit();
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
    }
}