package com.krish.rentmate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseUser;
import com.krish.rentmate.R;
import com.krish.rentmate.activities.MainDashboardActivity;
import com.krish.rentmate.activities.PropertyDetailsActivity;
import com.krish.rentmate.adapters.PropertyAdapter;
import com.krish.rentmate.models.Property;
import com.krish.rentmate.repositories.AuthRepository;
import com.krish.rentmate.repositories.PropertyRepository;
import com.krish.rentmate.repositories.RepositoryCallback;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements PropertyAdapter.OnPropertyClickListener {

    private PropertyAdapter propertyAdapter;
    private TextView tvGreeting;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvGreeting = view.findViewById(R.id.tvGreeting);
        MaterialCardView cvSearchBar = view.findViewById(R.id.cvSearchBar);
        MaterialCardView catApartments = view.findViewById(R.id.catApartments);
        MaterialCardView catPGs = view.findViewById(R.id.catPGs);
        MaterialCardView catShared = view.findViewById(R.id.catShared);
        MaterialCardView catStudios = view.findViewById(R.id.catStudios);
        TextView tvSeeAllFeatured = view.findViewById(R.id.tvSeeAllFeatured);

        MaterialButton btnViewRoommates = view.findViewById(R.id.btnViewRoommates);
        MaterialButton btnManageExpenses = view.findViewById(R.id.btnManageExpenses);

        RecyclerView rvFeaturedProperties = view.findViewById(R.id.rvFeaturedProperties);
        rvFeaturedProperties.setLayoutManager(new LinearLayoutManager(getContext()));
        
        propertyAdapter = new PropertyAdapter(new ArrayList<>(), this);
        rvFeaturedProperties.setAdapter(propertyAdapter);

        loadGreeting();
        loadFeaturedProperties();

        // Navigation handlers
        View.OnClickListener goToExplore = v -> {
            if (getActivity() instanceof MainDashboardActivity) {
                ((MainDashboardActivity) getActivity()).navigateToTab(R.id.nav_explore);
            }
        };

        cvSearchBar.setOnClickListener(goToExplore);
        catApartments.setOnClickListener(goToExplore);
        catPGs.setOnClickListener(goToExplore);
        catShared.setOnClickListener(goToExplore);
        catStudios.setOnClickListener(goToExplore);
        tvSeeAllFeatured.setOnClickListener(goToExplore);

        btnViewRoommates.setOnClickListener(v -> {
            if (getActivity() instanceof MainDashboardActivity) {
                ((MainDashboardActivity) getActivity()).navigateToTab(R.id.nav_roommates);
            }
        });

        btnManageExpenses.setOnClickListener(v -> {
            if (getActivity() instanceof MainDashboardActivity) {
                ((MainDashboardActivity) getActivity()).navigateToTab(R.id.nav_expenses);
            }
        });

        return view;
    }

    private void loadGreeting() {
        FirebaseUser user = AuthRepository.getInstance().getCurrentUser();
        if (user != null && user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
            tvGreeting.setText("Good morning, " + user.getDisplayName());
        } else {
            tvGreeting.setText("Good morning, Krish");
        }
    }

    private void loadFeaturedProperties() {
        PropertyRepository.getInstance().getProperties(new RepositoryCallback<List<Property>>() {
            @Override
            public void onSuccess(List<Property> result) {
                if (result != null && !result.isEmpty()) {
                    List<Property> featured = result.subList(0, Math.min(result.size(), 3));
                    propertyAdapter.updateList(featured);
                }
            }

            @Override
            public void onError(String errorMessage) {}
        });
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