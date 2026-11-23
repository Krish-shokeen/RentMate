package com.krish.rentmate.repositories;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.krish.rentmate.models.Expense;
import com.krish.rentmate.utils.DemoData;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {

    private static ExpenseRepository instance;

    private ExpenseRepository() {}

    public static synchronized ExpenseRepository getInstance() {
        if (instance == null) {
            instance = new ExpenseRepository();
        }
        return instance;
    }

    public void getExpenses(RepositoryCallback<List<Expense>> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("expenses")
                    .get()
                    .addOnSuccessListener(snapshots -> {
                        List<Expense> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            Expense e = doc.toObject(Expense.class);
                            e.setExpenseId(doc.getId());
                            list.add(e);
                        }
                        if (list.isEmpty()) {
                            callback.onSuccess(DemoData.getExpenses());
                        } else {
                            callback.onSuccess(list);
                        }
                    })
                    .addOnFailureListener(e -> callback.onSuccess(DemoData.getExpenses()));
        } catch (Exception e) {
            callback.onSuccess(DemoData.getExpenses());
        }
    }

    public void addExpense(Expense expense, RepositoryCallback<String> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            expense.setCreatedAt(System.currentTimeMillis());
            expense.setUpdatedAt(System.currentTimeMillis());
            db.collection("expenses")
                    .add(expense)
                    .addOnSuccessListener(docRef -> {
                        expense.setExpenseId(docRef.getId());
                        DemoData.addExpense(expense);
                        callback.onSuccess(docRef.getId());
                    })
                    .addOnFailureListener(e -> {
                        DemoData.addExpense(expense);
                        callback.onSuccess(expense.getId());
                    });
        } catch (Exception e) {
            DemoData.addExpense(expense);
            callback.onSuccess(expense.getId());
        }
    }

    public void deleteExpense(String expenseId, RepositoryCallback<Void> callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("expenses").document(expenseId)
                    .delete()
                    .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                    .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
        } catch (Exception e) {
            callback.onError("Firebase not configured.");
        }
    }
}