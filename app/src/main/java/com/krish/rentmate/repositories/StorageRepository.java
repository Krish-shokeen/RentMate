package com.krish.rentmate.repositories;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageRepository {

    private static StorageRepository instance;

    private StorageRepository() {}

    public static synchronized StorageRepository getInstance() {
        if (instance == null) {
            instance = new StorageRepository();
        }
        return instance;
    }

    public void uploadProfileImage(String uid, Uri imageUri, RepositoryCallback<String> callback) {
        try {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("profile_images/" + uid + "/profile.jpg");

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> 
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> callback.onSuccess(uri.toString()))
                                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()))
                    )
                    .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
        } catch (Exception e) {
            callback.onError("Firebase Storage is not configured.");
        }
    }

    public void uploadPropertyImage(String uid, String propertyId, Uri imageUri, RepositoryCallback<String> callback) {
        try {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("property_images/" + uid + "/" + propertyId + "/" + System.currentTimeMillis() + ".jpg");

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> 
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> callback.onSuccess(uri.toString()))
                                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()))
                    )
                    .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
        } catch (Exception e) {
            callback.onError("Firebase Storage is not configured.");
        }
    }
}