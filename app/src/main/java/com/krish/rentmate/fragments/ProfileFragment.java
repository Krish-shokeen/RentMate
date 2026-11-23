package com.krish.rentmate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.krish.rentmate.R;
import com.krish.rentmate.activities.MainActivity;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        View.OnClickListener placeholderListener = v -> {
            if (getView() != null) {
                Snackbar.make(getView(), "This feature will be available in a future update.", Snackbar.LENGTH_SHORT).show();
            }
        };

        view.findViewById(R.id.btnEditProfile).setOnClickListener(placeholderListener);
        view.findViewById(R.id.optListings).setOnClickListener(placeholderListener);
        view.findViewById(R.id.optSaved).setOnClickListener(placeholderListener);
        view.findViewById(R.id.optRequests).setOnClickListener(placeholderListener);
        view.findViewById(R.id.optExpenseHistory).setOnClickListener(placeholderListener);
        view.findViewById(R.id.optNotifications).setOnClickListener(placeholderListener);

        view.findViewById(R.id.optLogout).setOnClickListener(v -> showLogoutDialog());

        return view;
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}