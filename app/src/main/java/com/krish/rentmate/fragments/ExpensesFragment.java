package com.krish.rentmate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.krish.rentmate.R;
import com.krish.rentmate.activities.AddExpenseActivity;
import com.krish.rentmate.adapters.ExpenseAdapter;
import com.krish.rentmate.models.Expense;
import com.krish.rentmate.utils.DemoData;

import java.util.List;

public class ExpensesFragment extends Fragment {

    private ExpenseAdapter adapter;
    private List<Expense> expenseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        expenseList = DemoData.getExpenses();

        MaterialButton btnAddExpense = view.findViewById(R.id.btnAddExpense);
        RecyclerView rvExpenses = view.findViewById(R.id.rvExpenses);

        rvExpenses.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExpenseAdapter(expenseList);
        rvExpenses.setAdapter(adapter);

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.updateList(DemoData.getExpenses());
        }
    }
}