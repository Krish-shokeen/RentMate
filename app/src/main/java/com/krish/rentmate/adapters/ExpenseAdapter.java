package com.krish.rentmate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.krish.rentmate.R;
import com.krish.rentmate.models.Expense;

import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    public void updateList(List<Expense> newList) {
        this.expenseList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.bind(expense);
    }

    @Override
    public int getItemCount() {
        return expenseList != null ? expenseList.size() : 0;
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        ImageView ivExpenseIcon;
        TextView tvExpenseTitle, tvExpenseDetails, tvExpenseAmount, tvExpenseStatus;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            ivExpenseIcon = itemView.findViewById(R.id.ivExpenseIcon);
            tvExpenseTitle = itemView.findViewById(R.id.tvExpenseTitle);
            tvExpenseDetails = itemView.findViewById(R.id.tvExpenseDetails);
            tvExpenseAmount = itemView.findViewById(R.id.tvExpenseAmount);
            tvExpenseStatus = itemView.findViewById(R.id.tvExpenseStatus);
        }

        public void bind(Expense expense) {
            ivExpenseIcon.setImageResource(expense.getIconResId());
            tvExpenseTitle.setText(expense.getTitle());
            tvExpenseDetails.setText(String.format(Locale.US, "%s • Paid by %s", expense.getDate(), expense.getPaidBy()));
            tvExpenseAmount.setText(String.format(Locale.US, "₹%,.0f", expense.getAmount()));
            tvExpenseStatus.setText(expense.getStatus());

            if ("Paid".equalsIgnoreCase(expense.getStatus())) {
                tvExpenseStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.emerald));
            } else {
                tvExpenseStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.orange));
            }
        }
    }
}