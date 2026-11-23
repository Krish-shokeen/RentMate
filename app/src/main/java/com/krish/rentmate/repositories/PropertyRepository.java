package com.krish.rentmate.repositories;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.krish.rentmate.models.Property;
import com.krish.rentmate.utils.DemoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyRepository {

    private static PropertyRepository instance;

    private PropertyRepository() {}

    public static synchronized PropertyRepository getInstance() {
        if (instance == null) {
            instance = new PropertyRepository();
        }
        return instance;
    }

    public void getProperties(RepositoryCallback<List<Property>> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("properties")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Property> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Property p = doc.toObject(Property.class);
                            p.setId(doc.getId());
                            list.add(p);
                        }
                        if (list.isEmpty()) {
                            // Fallback to local demo properties if collection is empty
                            callback.onSuccess(DemoData.getProperties());
                        } else {
                            callback.onSuccess(list);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Fallback on error
                        callback.onSuccess(DemoData.getProperties());
                    });
        } catch (Exception e) {
            callback.onSuccess(DemoData.getProperties());
        }
    }

    public void addProperty(Property property, RepositoryCallback<String> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            property.setCreatedAt(System.currentTimeMillis());
            property.setUpdatedAt(System.currentTimeMillis());
            db.collection("properties")
                    .add(property)
                    .addOnSuccessListener(documentReference -> {
                        property.setId(documentReference.getId());
                        callback.onSuccess(documentReference.getId());
                    })
                    .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
        } catch (Exception e) {
            callback.onError("Firebase not configured.");
        }
    }

    public void updateProperty(Property property, RepositoryCallback<Void> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            property.setUpdatedAt(System.currentTimeMillis());
            db.collection("properties").document(property.getId())
                    .set(property)
                    .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                    .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
        } catch (Exception e) {
            callback.onError("Firebase not configured.");
        }
    }

    public void deleteProperty(String propertyId, RepositoryCallback<Void> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("properties").document(propertyId)
                    .delete()
                    .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                    .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
        } catch (Exception e) {
            callback.onError("Firebase not configured.");
        }
    }

    public void getFavoritePropertyIds(String uid, RepositoryCallback<List<String>> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(uid).collection("favorites")
                    .get()
                    .addOnSuccessListener(snapshots -> {
                        List<String> ids = new ArrayList<>();
                        for (DocumentSnapshot doc : snapshots) {
                            ids.add(doc.getId());
                        }
                        callback.onSuccess(ids);
                    })
                    .addOnFailureListener(e -> callback.onSuccess(new ArrayList<>()));
        } catch (Exception e) {
            callback.onSuccess(new ArrayList<>());
        }
    }

    public void toggleFavorite(String uid, String propertyId, boolean isFavorite, RepositoryCallback<Void> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (isFavorite) {
                Map<String, Object> fav = new HashMap<>();
                fav.put("savedAt", System.currentTimeMillis());
                db.collection("users").document(uid).collection("favorites").document(propertyId)
                        .set(fav)
                        .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                        .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
            } else {
                db.collection("users").document(uid).collection("favorites").document(propertyId)
                        .delete()
                        .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                        .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
            }
        } catch (Exception e) {
            callback.onError("Firebase not configured.");
        }
    }
}