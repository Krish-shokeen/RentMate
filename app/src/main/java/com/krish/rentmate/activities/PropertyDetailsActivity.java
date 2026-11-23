package com.krish.rentmate.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.krish.rentmate.R;
import com.krish.rentmate.models.Property;

import java.util.Locale;

public class PropertyDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        Property property = (Property) getIntent().getSerializableExtra("property");

        ImageView ivDetailImage = findViewById(R.id.ivDetailImage);
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView btnDetailFavorite = findViewById(R.id.btnDetailFavorite);

        TextView tvDetailTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDetailLocation = findViewById(R.id.tvDetailLocation);
        TextView tvDetailRent = findViewById(R.id.tvDetailRent);
        TextView tvDetailRating = findViewById(R.id.tvDetailRating);
        TextView tvDetailBeds = findViewById(R.id.tvDetailBeds);
        TextView tvDetailBaths = findViewById(R.id.tvDetailBaths);
        TextView tvDetailFurnishing = findViewById(R.id.tvDetailFurnishing);
        TextView tvDetailDescription = findViewById(R.id.tvDetailDescription);
        TextView tvDetailAmenities = findViewById(R.id.tvDetailAmenities);

        MaterialButton btnContactOwner = findViewById(R.id.btnContactOwner);

        btnBack.setOnClickListener(v -> finish());

        if (property != null) {
            ivDetailImage.setImageResource(property.getImageResId());
            tvDetailTitle.setText(property.getTitle());
            tvDetailLocation.setText(property.getLocation());
            tvDetailRent.setText(String.format(Locale.US, "₹%,d / month", property.getRent()));
            tvDetailRating.setText(String.valueOf(property.getRating()));
            tvDetailBeds.setText(String.format(Locale.US, "%d Bedroom%s", property.getBedrooms(), property.getBedrooms() > 1 ? "s" : ""));
            tvDetailBaths.setText(String.format(Locale.US, "%d Bathroom%s", property.getBathrooms(), property.getBathrooms() > 1 ? "s" : ""));
            tvDetailFurnishing.setText(property.getFurnishing());
            tvDetailDescription.setText(property.getDescription());
            tvDetailAmenities.setText(property.getAmenities());

            btnDetailFavorite.setImageResource(property.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);

            btnDetailFavorite.setOnClickListener(v -> {
                property.setFavorite(!property.isFavorite());
                btnDetailFavorite.setImageResource(property.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
                Snackbar.make(v, property.isFavorite() ? "Saved to favorites!" : "Removed from favorites", Snackbar.LENGTH_SHORT).show();
            });
        }

        btnContactOwner.setOnClickListener(v -> 
            Snackbar.make(v, "Messaging functionality will be available in a future update.", Snackbar.LENGTH_LONG).show()
        );
    }
}