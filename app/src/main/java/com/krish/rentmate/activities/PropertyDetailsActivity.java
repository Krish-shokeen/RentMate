package com.krish.rentmate.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.krish.rentmate.R;
import com.krish.rentmate.models.Property;

import java.util.Locale;

public class PropertyDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Property property;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        property = (Property) getIntent().getSerializableExtra("property");

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

        MaterialButton btnOpenMaps = findViewById(R.id.btnOpenMaps);
        MaterialButton btnGetDirections = findViewById(R.id.btnGetDirections);
        MaterialButton btnContactOwner = findViewById(R.id.btnContactOwner);

        btnBack.setOnClickListener(v -> finish());

        // Embed map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.detailMapContainer);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailMapContainer, mapFragment)
                    .commit();
        }
        mapFragment.getMapAsync(this);

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

        btnOpenMaps.setOnClickListener(v -> openInGoogleMaps());
        btnGetDirections.setOnClickListener(v -> openDirectionsInMaps());

        btnContactOwner.setOnClickListener(v -> 
            Snackbar.make(v, "Messaging functionality will be available in a future update.", Snackbar.LENGTH_LONG).show()
        );
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (property != null && (property.getLatitude() != 0.0 || property.getLongitude() != 0.0)) {
            LatLng pos = new LatLng(property.getLatitude(), property.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(property.getTitle())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 14f));
        }
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    private void openInGoogleMaps() {
        if (property != null && (property.getLatitude() != 0.0 || property.getLongitude() != 0.0)) {
            String uriStr = String.format(Locale.US, "geo:%f,%f?q=%f,%f(%s)", 
                    property.getLatitude(), property.getLongitude(), 
                    property.getLatitude(), property.getLongitude(), 
                    Uri.encode(property.getTitle()));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriStr));
            intent.setPackage("com.google.android.apps.maps");
            try {
                startActivity(intent);
            } catch (Exception e) {
                // Fallback to web browser or generic intent if Google Maps app is not installed
                Intent fallback = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + property.getLatitude() + "," + property.getLongitude()));
                startActivity(fallback);
            }
        } else if (property != null && property.getLocation() != null) {
            String uriStr = "geo:0,0?q=" + Uri.encode(property.getLocation());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriStr));
            startActivity(intent);
        }
    }

    private void openDirectionsInMaps() {
        if (property != null && (property.getLatitude() != 0.0 || property.getLongitude() != 0.0)) {
            String uriStr = String.format(Locale.US, "google.navigation:q=%f,%f", property.getLatitude(), property.getLongitude());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriStr));
            intent.setPackage("com.google.android.apps.maps");
            try {
                startActivity(intent);
            } catch (Exception e) {
                Intent fallback = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + property.getLatitude() + "," + property.getLongitude()));
                startActivity(fallback);
            }
        } else if (property != null && property.getLocation() != null) {
            String uriStr = "https://www.google.com/maps/dir/?api=1&destination=" + Uri.encode(property.getLocation());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriStr));
            startActivity(intent);
        }
    }
}