package com.krish.rentmate.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.krish.rentmate.R;
import com.krish.rentmate.utils.ValidationUtils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        tilEmail = findViewById(R.id.tilEmail);
        etEmail = findViewById(R.id.etEmail);
        
        MaterialButton btnResetPassword = findViewById(R.id.btnResetPassword);
        TextView tvBackToLogin = findViewById(R.id.tvBackToLogin);

        btnResetPassword.setOnClickListener(v -> validateAndSendReset());

        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void validateAndSendReset() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        tilEmail.setError(null);

        boolean isValid = true;

        if (!ValidationUtils.isNotEmpty(email)) {
            tilEmail.setError(getString(R.string.error_required));
            isValid = false;
        } else if (!ValidationUtils.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }

        if (isValid) {
            Snackbar.make(tilEmail, "Password reset functionality will be connected with Firebase in a later phase.", Snackbar.LENGTH_LONG).show();
        }
    }
}