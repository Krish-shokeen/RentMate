package com.krish.rentmate.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.krish.rentmate.R;
import com.krish.rentmate.models.Expense;
import com.krish.rentmate.utils.DemoData;
import com.krish.rentmate.utils.ValidationUtils;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private TextInputLayout tilExpenseTitle, tilExpenseAmount, tilExpenseCategory, tilPaidBy;
    private TextInputEditText etExpenseTitle, etExpenseAmount, etExpenseCategory, etPaidBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        tilExpenseTitle = findViewById(R.id.tilExpenseTitle);
        tilExpenseAmount = findViewById(R.id.tilExpenseAmount);
        tilExpenseCategory = findViewById(R.id.tilExpenseCategory);
        tilPaidBy = findViewById(R.id.tilPaidBy);

        etExpenseTitle = findViewById(R.id.etExpenseTitle);
        etExpenseAmount = findViewById(R.id.etExpenseAmount);
        etExpenseCategory = findViewById(R.id.etExpenseCategory);
        etPaidBy = findViewById(R.id.etPaidBy);

        MaterialButton btnSaveExpense = findViewById(R.id.btnSaveExpense);

        btnSaveExpense.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        String title = etExpenseTitle.getText() != null ? etExpenseTitle.getText().toString().trim() : "";
        String amountStr = etExpenseAmount.getText() != null ? etExpenseAmount.getText().toString().trim() : "";
        String category = etExpenseCategory.getText() != null ? etExpenseCategory.getText().toString().trim() : "General";
        String paidBy = etPaidBy.getText() != null ? etPaidBy.getText().toString().trim() : "Krish";

        tilExpenseTitle.setError(null);
        tilExpenseAmount.setError(null);

        boolean isValid = true;

        if (!ValidationUtils.isNotEmpty(title)) {
            tilExpenseTitle.setError("Title is required");
            isValid = false;
        }

        double amount = 0;
        if (!ValidationUtils.isNotEmpty(amountStr)) {
            tilExpenseAmount.setError("Amount is required");
            isValid = false;
        } else {
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    tilExpenseAmount.setError("Enter a valid amount");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                tilExpenseAmount.setError("Invalid number");
                isValid = false;
            }
        }

        if (isValid) {
            String today = new SimpleDateFormat("MMM dd, yyyy", Locale.US).format(new Date());
            int iconRes = R.drawable.ic_receipt;
            if (category.toLowerCase().contains("rent")) iconRes = R.drawable.ic_house;
            else if (category.toLowerCase().contains("electr")) iconRes = R.drawable.ic_flash;
            else if (category.toLowerCase().contains("water")) iconRes = R.drawable.ic_water;
            else if (category.toLowerCase().contains("wifi") || category.toLowerCase().contains("net")) iconRes = R.drawable.ic_wifi;
            else if (category.toLowerCase().contains("groc") || category.toLowerCase().contains("food")) iconRes = R.drawable.ic_shopping_cart;

            Expense newExpense = new Expense(
                    "e_" + System.currentTimeMillis(),
                    title,
                    amount,
                    today,
                    category,
                    "Paid",
                    paidBy.isEmpty() ? "Krish" : paidBy,
                    "Split (3 people)",
                    iconRes
            );

            DemoData.addExpense(newExpense);
            Snackbar.make(tilExpenseTitle, "Expense saved successfully!", Snackbar.LENGTH_SHORT).show();
            finish();
        }
    }
}