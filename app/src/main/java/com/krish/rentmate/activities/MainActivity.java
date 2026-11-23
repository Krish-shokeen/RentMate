package com.krish.rentmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.krish.rentmate.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialButton btnGetStarted = findViewById(R.id.btnGetStarted);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        TextView btnGuest = findViewById(R.id.btnGuest);

        btnGetStarted.setOnClickListener(v -> 
            startActivity(new Intent(MainActivity.this, SignupActivity.class))
        );

        btnLogin.setOnClickListener(v -> 
            startActivity(new Intent(MainActivity.this, LoginActivity.class))
        );

        btnGuest.setOnClickListener(v -> 
            startActivity(new Intent(MainActivity.this, MainDashboardActivity.class))
        );
    }
}