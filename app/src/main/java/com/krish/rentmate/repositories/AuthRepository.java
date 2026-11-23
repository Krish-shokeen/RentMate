package com.krish.rentmate.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.krish.rentmate.models.User;

public class AuthRepository {

    private static AuthRepository instance;

    private AuthRepository() {}

    public static synchronized AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    public boolean isUserAuthenticated() {
        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            return auth.getCurrentUser() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public FirebaseUser getCurrentUser() {
        try {
            return FirebaseAuth.getInstance().getCurrentUser();
        } catch (Exception e) {
            return null;
        }
    }

    public void register(String name, String email, String phone, String password, RepositoryCallback<User> callback) {
        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getUser() != null) {
                            FirebaseUser fbUser = task.getResult().getUser();
                            
                            // Update display name
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            fbUser.updateProfile(profileUpdates);

                            // Create Firestore user document
                            User newUser = new User(fbUser.getUid(), name, email, phone);
                            UserRepository.getInstance().createUserProfile(newUser, new RepositoryCallback<Void>() {
                                @Override
                                public void onSuccess(Void result) {
                                    callback.onSuccess(newUser);
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    // Even if Firestore creation has an issue, auth succeeded
                                    callback.onSuccess(newUser);
                                }
                            });
                        } else {
                            String errorMsg = task.getException() != null ? task.getException().getLocalizedMessage() : "Registration failed.";
                            callback.onError(errorMsg);
                        }
                    });
        } catch (Exception e) {
            callback.onError("Firebase is not configured or initialized. " + (e.getMessage() != null ? e.getMessage() : ""));
        }
    }

    public void login(String email, String password, RepositoryCallback<User> callback) {
        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getUser() != null) {
                            FirebaseUser fbUser = task.getResult().getUser();
                            UserRepository.getInstance().getUserProfile(fbUser.getUid(), new RepositoryCallback<User>() {
                                @Override
                                public void onSuccess(User result) {
                                    callback.onSuccess(result);
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    // Fallback to basic user if profile document missing
                                    User basicUser = new User(fbUser.getUid(), fbUser.getDisplayName(), fbUser.getEmail(), "");
                                    callback.onSuccess(basicUser);
                                }
                            });
                        } else {
                            String errorMsg = task.getException() != null ? task.getException().getLocalizedMessage() : "Authentication failed.";
                            callback.onError(errorMsg);
                        }
                    });
        } catch (Exception e) {
            callback.onError("Firebase is not configured or initialized. " + (e.getMessage() != null ? e.getMessage() : ""));
        }
    }

    public void resetPassword(String email, RepositoryCallback<Void> callback) {
        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onSuccess(null);
                        } else {
                            String errorMsg = task.getException() != null ? task.getException().getLocalizedMessage() : "Failed to send reset email.";
                            callback.onError(errorMsg);
                        }
                    });
        } catch (Exception e) {
            callback.onError("Firebase is not configured or initialized.");
        }
    }

    public void logout() {
        try {
            FirebaseAuth.getInstance().signOut();
        } catch (Exception ignored) {}
    }
}