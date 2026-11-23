package com.krish.rentmate.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.krish.rentmate.R;
import com.krish.rentmate.activities.PropertyDetailsActivity;
import com.krish.rentmate.adapters.PropertyAdapter;
import com.krish.rentmate.models.Property;
import com.krish.rentmate.repositories.AuthRepository;
import com.krish.rentmate.repositories.PropertyRepository;
import com.krish.rentmate.repositories.RepositoryCallback;
import com.krish.rentmate.utils.LocationHelper;
import com.krish.rentmate.utils.MapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ExploreFragment extends Fragment implements PropertyAdapter.OnPropertyClickListener, OnMapReadyCallback {

    private PropertyAdapter adapter;
    private List<Property> allProperties;
    private List<Property> filteredProperties;
    private TextView tvResultCount, tvSort;
    private MaterialButton btnToggleMapList;
    private RecyclerView rvProperties;
    private FrameLayout mapContainer;
    
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;

    private boolean isMapMode = false;
    private String currentCategory = "All";
    private int maxBudget = 25000;
    private int sortMode = 0; // 0: Featured, 1: Price Low-High, 2: Price High-Low, 3: Rating, 4: Distance
    private Location userLocation = null;

    private final ActivityResultLauncher<String[]> locationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                if (fineGranted || coarseGranted) {
                    fetchUserCurrentLocation();
                } else {
                    if (getView() != null) {
                        Snackbar.make(getView(), R.string.location_permission_needed, Snackbar.LENGTH_LONG).show();
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        allProperties = new ArrayList<>();
        filteredProperties = new ArrayList<>();

        TextInputEditText etSearch = view.findViewById(R.id.etSearch);
        MaterialCardView btnFilter = view.findViewById(R.id.btnFilter);
        MaterialCardView btnMyLocation = view.findViewById(R.id.btnMyLocation);
        ChipGroup chipGroupCategories = view.findViewById(R.id.chipGroupCategories);
        tvResultCount = view.findViewById(R.id.tvResultCount);
        tvSort = view.findViewById(R.id.tvSort);
        btnToggleMapList = view.findViewById(R.id.btnToggleMapList);

        rvProperties = view.findViewById(R.id.rvProperties);
        mapContainer = view.findViewById(R.id.mapContainer);

        rvProperties.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PropertyAdapter(filteredProperties, this);
        rvProperties.setAdapter(adapter);

        // Embed SupportMapFragment
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapContainer);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.mapContainer, mapFragment)
                    .commit();
        }
        mapFragment.getMapAsync(this);

        // Fetch properties from Repository
        loadPropertiesFromRepository(etSearch);

        // Search listener
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Category chips
        chipGroupCategories.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chipApartments)) currentCategory = "Apartment";
            else if (checkedIds.contains(R.id.chipPG)) currentCategory = "PG";
            else if (checkedIds.contains(R.id.chipShared)) currentCategory = "Shared";
            else if (checkedIds.contains(R.id.chipStudio)) currentCategory = "Studio";
            else currentCategory = "All";

            applyFilters(etSearch.getText() != null ? etSearch.getText().toString() : "");
        });

        // Toggle Map / List
        btnToggleMapList.setOnClickListener(v -> toggleMapListView());

        // My Location button
        btnMyLocation.setOnClickListener(v -> requestLocationAccess());

        // Filter button dialog
        btnFilter.setOnClickListener(v -> showFilterDialog());

        // Sort options
        tvSort.setOnClickListener(v -> showSortDialog());

        return view;
    }

    private void toggleMapListView() {
        isMapMode = !isMapMode;
        if (isMapMode) {
            rvProperties.setVisibility(View.GONE);
            mapContainer.setVisibility(View.VISIBLE);
            btnToggleMapList.setText(R.string.list_view);
            btnToggleMapList.setIconResource(R.drawable.ic_home);
            updateMapMarkers();
        } else {
            mapContainer.setVisibility(View.GONE);
            rvProperties.setVisibility(View.VISIBLE);
            btnToggleMapList.setText(R.string.map_view);
            btnToggleMapList.setIconResource(R.drawable.ic_explore);
        }
    }

    private void requestLocationAccess() {
        if (getContext() == null) return;
        if (LocationHelper.hasLocationPermission(getContext())) {
            fetchUserCurrentLocation();
        } else {
            locationPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void fetchUserCurrentLocation() {
        if (getContext() == null) return;
        LocationHelper.getCurrentLocation(getContext(), new LocationHelper.LocationResultCallback() {
            @Override
            public void onSuccess(Location location) {
                userLocation = location;
                if (getView() != null) {
                    Snackbar.make(getView(), "Nearby sorting active for your location!", Snackbar.LENGTH_SHORT).show();
                }
                sortMode = 4; // Nearby sort
                applyFilters("");
            }

            @Override
            public void onError(String errorMessage) {
                if (getView() != null) {
                    Snackbar.make(getView(), errorMessage, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadPropertiesFromRepository(TextInputEditText etSearch) {
        PropertyRepository.getInstance().getProperties(new RepositoryCallback<List<Property>>() {
            @Override
            public void onSuccess(List<Property> result) {
                allProperties = result != null ? result : new ArrayList<>();
                applyFilters(etSearch.getText() != null ? etSearch.getText().toString() : "");
            }

            @Override
            public void onError(String errorMessage) {
                updateResultCount();
            }
        });
    }

    private void applyFilters(String searchQuery) {
        filteredProperties.clear();
        String query = searchQuery.toLowerCase().trim();

        for (Property p : allProperties) {
            boolean matchesCategory = currentCategory.equals("All") || 
                    (p.getType() != null && p.getType().toLowerCase().contains(currentCategory.toLowerCase()));
            boolean matchesSearch = query.isEmpty() || 
                    (p.getTitle() != null && p.getTitle().toLowerCase().contains(query)) || 
                    (p.getLocation() != null && p.getLocation().toLowerCase().contains(query));
            boolean matchesBudget = p.getRent() <= maxBudget;

            if (matchesCategory && matchesSearch && matchesBudget) {
                filteredProperties.add(p);
            }
        }

        applySorting();
        adapter.updateList(filteredProperties);
        updateResultCount();
        updateMapMarkers();
    }

    private void applySorting() {
        if (sortMode == 1) { // Price Low to High
            Collections.sort(filteredProperties, (p1, p2) -> Integer.compare(p1.getRent(), p2.getRent()));
        } else if (sortMode == 2) { // Price High to Low
            Collections.sort(filteredProperties, (p1, p2) -> Integer.compare(p2.getRent(), p1.getRent()));
        } else if (sortMode == 3) { // Rating
            Collections.sort(filteredProperties, (p1, p2) -> Float.compare(p2.getRating(), p1.getRating()));
        } else if (sortMode == 4 && userLocation != null) { // Distance
            Collections.sort(filteredProperties, (p1, p2) -> {
                float d1 = LocationHelper.calculateDistanceMeters(userLocation.getLatitude(), userLocation.getLongitude(), p1.getLatitude(), p1.getLongitude());
                float d2 = LocationHelper.calculateDistanceMeters(userLocation.getLatitude(), userLocation.getLongitude(), p2.getLatitude(), p2.getLongitude());
                return Float.compare(d1, d2);
            });
        }
    }

    private void updateMapMarkers() {
        if (googleMap != null && isMapMode) {
            MapUtils.populatePropertyMarkers(googleMap, filteredProperties, property -> {
                Intent intent = new Intent(getActivity(), PropertyDetailsActivity.class);
                intent.putExtra("property", property);
                startActivity(intent);
            });
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (getContext() != null && LocationHelper.hasLocationPermission(getContext())) {
            try {
                this.googleMap.setMyLocationEnabled(true);
            } catch (SecurityException ignored) {}
        }

        updateMapMarkers();
    }

    private void showFilterDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_filter_explore, null);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        Slider sliderBudget = dialogView.findViewById(R.id.sliderBudget);
        TextView tvBudgetValue = dialogView.findViewById(R.id.tvBudgetValue);
        View btnApply = dialogView.findViewById(R.id.btnApplyFilter);

        sliderBudget.setValue(maxBudget);
        tvBudgetValue.setText(String.format(Locale.US, "Up to ₹%,d / month", maxBudget));

        sliderBudget.addOnChangeListener((slider, value, fromUser) -> 
            tvBudgetValue.setText(String.format(Locale.US, "Up to ₹%,d / month", (int) value))
        );

        btnApply.setOnClickListener(v -> {
            maxBudget = (int) sliderBudget.getValue();
            applyFilters("");
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showSortDialog() {
        String[] options = {"Featured", "Price: Low to High", "Price: High to Low", "Highest Rating", "Nearest Location"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Sort Properties By")
                .setSingleChoiceItems(options, sortMode, (dialog, which) -> {
                    sortMode = which;
                    if (which == 4 && userLocation == null) {
                        requestLocationAccess();
                    } else {
                        tvSort.setText(String.format(Locale.US, "Sort: %s ▾", options[which]));
                        applyFilters("");
                    }
                    dialog.dismiss();
                })
                .show();
    }

    private void updateResultCount() {
        if (tvResultCount != null) {
            tvResultCount.setText(String.format(Locale.US, "%d Properties available", filteredProperties.size()));
        }
    }

    @Override
    public void onPropertyClick(Property property) {
        Intent intent = new Intent(getActivity(), PropertyDetailsActivity.class);
        intent.putExtra("property", property);
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Property property, int position) {
        if (AuthRepository.getInstance().isUserAuthenticated()) {
            String uid = AuthRepository.getInstance().getCurrentUser().getUid();
            PropertyRepository.getInstance().toggleFavorite(uid, property.getId(), property.isFavorite(), new RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void result) {}

                @Override
                public void onError(String errorMessage) {}
            });
        }
    }
}