package com.krish.rentmate.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.krish.rentmate.R;
import com.krish.rentmate.repositories.AuthRepository;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Apply fade-in animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        findViewById(R.id.logoContainer).startAnimation(fadeIn);

        // Check authentication state
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            if (AuthRepository.getInstance().isUserAuthenticated()) {
                intent = new Intent(SplashActivity.this, MainDashboardActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}