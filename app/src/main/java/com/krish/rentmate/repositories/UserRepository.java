package com.krish.rentmate.repositories;

import com.google.firebase.firestore.FirebaseFirestore;
import com.krish.rentmate.models.User;

public class UserRepository {

    private static UserRepository instance;

    private UserRepository() {}

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public void createUserProfile(User user, RepositoryCallback<Void> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(user.getUid())
                    .set(user)
                    .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                    .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
        } catch (Exception e) {
            callback.onError(e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "Firestore initialization failed.");
        }
    }

    public void getUserProfile(String uid, RepositoryCallback<User> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            callback.onSuccess(user);
                        } else {
                            callback.onError("User profile not found.");
                        }
                    })
                    .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
        } catch (Exception e) {
            callback.onError(e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "Firestore error.");
        }
    }

    public void updateUserProfile(User user, RepositoryCallback<Void> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            user.setUpdatedAt(System.currentTimeMillis());
            db.collection("users").document(user.getUid())
                    .set(user)
                    .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                    .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
        } catch (Exception e) {
            callback.onError(e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "Firestore error.");
        }
    }
}