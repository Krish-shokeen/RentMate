package com.krish.rentmate.repositories;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.krish.rentmate.models.Roommate;
import com.krish.rentmate.models.RoommateRequest;
import com.krish.rentmate.utils.DemoData;

import java.util.ArrayList;
import java.util.List;

public class RoommateRepository {

    private static RoommateRepository instance;

    private RoommateRepository() {}

    public static synchronized RoommateRepository getInstance() {
        if (instance == null) {
            instance = new RoommateRepository();
        }
        return instance;
    }

    public void getRoommates(RepositoryCallback<List<Roommate>> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("roommates")
                    .get()
                    .addOnSuccessListener(snapshots -> {
                        List<Roommate> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            Roommate r = doc.toObject(Roommate.class);
                            r.setUid(doc.getId());
                            list.add(r);
                        }
                        if (list.isEmpty()) {
                            callback.onSuccess(DemoData.getRoommates());
                        } else {
                            callback.onSuccess(list);
                        }
                    })
                    .addOnFailureListener(e -> callback.onSuccess(DemoData.getRoommates()));
        } catch (Exception e) {
            callback.onSuccess(DemoData.getRoommates());
        }
    }

    public void updateRoommateProfile(Roommate roommate, RepositoryCallback<Void> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            roommate.setUpdatedAt(System.currentTimeMillis());
            db.collection("roommates").document(roommate.getUid())
                    .set(roommate)
                    .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                    .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
        } catch (Exception e) {
            callback.onError("Firebase not configured.");
        }
    }

    public void sendConnectionRequest(String senderId, String senderName, String receiverId, RepositoryCallback<Void> callback) {
        try {
            if (senderId.equals(receiverId)) {
                callback.onError("You cannot send a roommate request to yourself.");
                return;
            }
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String reqId = senderId + "_" + receiverId;
            RoommateRequest request = new RoommateRequest(reqId, senderId, senderName, receiverId, "pending");

            db.collection("roommateRequests").document(reqId)
                    .set(request)
                    .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                    .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
        } catch (Exception e) {
            callback.onError("Firebase not configured.");
        }
    }

    public void getReceivedRequests(String uid, RepositoryCallback<List<RoommateRequest>> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("roommateRequests")
                    .whereEqualTo("receiverId", uid)
                    .get()
                    .addOnSuccessListener(snapshots -> {
                        List<RoommateRequest> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            list.add(doc.toObject(RoommateRequest.class));
                        }
                        callback.onSuccess(list);
                    })
                    .addOnFailureListener(e -> callback.onSuccess(new ArrayList<>()));
        } catch (Exception e) {
            callback.onSuccess(new ArrayList<>());
        }
    }

    public void updateRequestStatus(String requestId, String status, RepositoryCallback<Void> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("roommateRequests").document(requestId)
                    .update("status", status, "updatedAt", System.currentTimeMillis())
                    .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                    .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
        } catch (Exception e) {
            callback.onError("Firebase not configured.");
        }
    }
}