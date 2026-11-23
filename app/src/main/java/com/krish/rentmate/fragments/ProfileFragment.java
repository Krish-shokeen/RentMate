package com.krish.rentmate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.krish.rentmate.R;
import com.krish.rentmate.activities.MainActivity;
import com.krish.rentmate.models.User;
import com.krish.rentmate.repositories.AuthRepository;
import com.krish.rentmate.repositories.RepositoryCallback;
import com.krish.rentmate.repositories.UserRepository;

public class ProfileFragment extends Fragment {

    private ImageView ivProfileImage;
    private TextView tvName, tvEmail;
    private User currentUserProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        MaterialButton btnEditProfile = view.findViewById(R.id.btnEditProfile);

        View.OnClickListener placeholderListener = v -> {
            if (getView() != null) {
                Snackbar.make(getView(), "This feature will be available in a future update.", Snackbar.LENGTH_SHORT).show();
            }
        };

        btnEditProfile.setOnClickListener(v -> showEditProfileDialog());
        view.findViewById(R.id.optListings).setOnClickListener(placeholderListener);
        view.findViewById(R.id.optSaved).setOnClickListener(placeholderListener);
        view.findViewById(R.id.optRequests).setOnClickListener(placeholderListener);
        view.findViewById(R.id.optExpenseHistory).setOnClickListener(placeholderListener);
        view.findViewById(R.id.optNotifications).setOnClickListener(placeholderListener);

        view.findViewById(R.id.optLogout).setOnClickListener(v -> showLogoutDialog());

        loadUserProfile();

        return view;
    }

    private void loadUserProfile() {
        FirebaseUser fbUser = AuthRepository.getInstance().getCurrentUser();
        if (fbUser != null) {
            tvEmail.setText(fbUser.getEmail() != null ? fbUser.getEmail() : "krish@example.com");
            tvName.setText(fbUser.getDisplayName() != null && !fbUser.getDisplayName().isEmpty() ? fbUser.getDisplayName() : "Krish");

            UserRepository.getInstance().getUserProfile(fbUser.getUid(), new RepositoryCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    if (user != null) {
                        currentUserProfile = user;
                        if (user.getName() != null && !user.getName().isEmpty()) {
                            tvName.setText(user.getName());
                        }
                        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty() && getContext() != null) {
                            Glide.with(getContext()).load(user.getProfileImageUrl()).placeholder(R.drawable.ic_avatar_placeholder).into(ivProfileImage);
                        }
                    }
                }

                @Override
                public void onError(String errorMessage) {}
            });
        }
    }

    private void showEditProfileDialog() {
        if (getContext() == null) return;

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_profile, null);
        EditText etName = dialogView.findViewById(R.id.etEditName);
        EditText etPhone = dialogView.findViewById(R.id.etEditPhone);
        EditText etCollege = dialogView.findViewById(R.id.etEditCollege);
        EditText etLocation = dialogView.findViewById(R.id.etEditLocation);

        if (currentUserProfile != null) {
            etName.setText(currentUserProfile.getName());
            etPhone.setText(currentUserProfile.getPhone());
            etCollege.setText(currentUserProfile.getCollege());
            etLocation.setText(currentUserProfile.getLocation());
        } else {
            etName.setText(tvName.getText().toString());
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Edit Profile")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    FirebaseUser fbUser = AuthRepository.getInstance().getCurrentUser();
                    if (fbUser == null) {
                        if (getView() != null) Snackbar.make(getView(), "Saved locally.", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    User user = currentUserProfile != null ? currentUserProfile : new User(fbUser.getUid(), etName.getText().toString(), fbUser.getEmail(), etPhone.getText().toString());
                    user.setName(etName.getText().toString().trim());
                    user.setPhone(etPhone.getText().toString().trim());
                    user.setCollege(etCollege.getText().toString().trim());
                    user.setLocation(etLocation.getText().toString().trim());

                    tvName.setText(user.getName());

                    UserRepository.getInstance().updateUserProfile(user, new RepositoryCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            currentUserProfile = user;
                            if (getView() != null) Snackbar.make(getView(), "Profile updated successfully!", Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            if (getView() != null) Snackbar.make(getView(), "Failed to update: " + errorMessage, Snackbar.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    AuthRepository.getInstance().logout();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}