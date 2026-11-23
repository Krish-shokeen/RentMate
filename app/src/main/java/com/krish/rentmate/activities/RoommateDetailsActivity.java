package com.krish.rentmate.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.krish.rentmate.R;
import com.krish.rentmate.models.Roommate;

import java.util.Locale;

public class RoommateDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roommate_details);

        Roommate roommate = (Roommate) getIntent().getSerializableExtra("roommate");

        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView ivDetailAvatar = findViewById(R.id.ivDetailAvatar);

        TextView tvDetailNameAge = findViewById(R.id.tvDetailNameAge);
        TextView tvDetailProfession = findViewById(R.id.tvDetailProfession);
        TextView tvDetailCompatibility = findViewById(R.id.tvDetailCompatibility);
        TextView tvDetailLocation = findViewById(R.id.tvDetailLocation);
        TextView tvDetailBudget = findViewById(R.id.tvDetailBudget);
        TextView tvDetailBio = findViewById(R.id.tvDetailBio);

        ChipGroup chipGroupDetailTags = findViewById(R.id.chipGroupDetailTags);
        MaterialButton btnDetailConnect = findViewById(R.id.btnDetailConnect);

        btnBack.setOnClickListener(v -> finish());

        if (roommate != null) {
            ivDetailAvatar.setImageResource(roommate.getAvatarResId());
            tvDetailNameAge.setText(String.format(Locale.US, "%s, %d", roommate.getName(), roommate.getAge()));
            tvDetailProfession.setText(roommate.getProfession());
            tvDetailCompatibility.setText(String.format(Locale.US, "%d%% Match", roommate.getCompatibility()));
            tvDetailLocation.setText(roommate.getLocation());
            tvDetailBudget.setText(String.format(Locale.US, "₹%dk – ₹%dk / mo", roommate.getMinBudget() / 1000, roommate.getMaxBudget() / 1000));
            tvDetailBio.setText(roommate.getBio());

            if (roommate.getTags() != null) {
                for (String tag : roommate.getTags()) {
                    Chip chip = new Chip(this);
                    chip.setText(tag);
                    chip.setChipBackgroundColorResource(R.color.light_gray);
                    chip.setTextColor(getColor(R.color.dark_navy));
                    chip.setClickable(false);
                    chipGroupDetailTags.addView(chip);
                }
            }

            if (roommate.isConnected()) {
                btnDetailConnect.setText("Request Sent");
                btnDetailConnect.setEnabled(false);
            }

            btnDetailConnect.setOnClickListener(v -> {
                roommate.setConnected(true);
                btnDetailConnect.setText("Request Sent");
                btnDetailConnect.setEnabled(false);
                Snackbar.make(v, "Connection request sent to " + roommate.getName() + "!", Snackbar.LENGTH_SHORT).show();
            });
        }
    }
}