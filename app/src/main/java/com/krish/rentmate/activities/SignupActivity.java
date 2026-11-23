package com.krish.rentmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.krish.rentmate.R;
import com.krish.rentmate.utils.ValidationUtils;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout tilName, tilEmail, tilPhone, tilPassword, tilConfirmPassword;
    private TextInputEditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private CheckBox cbTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhone = findViewById(R.id.tilPhone);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cbTerms = findViewById(R.id.cbTerms);

        MaterialButton btnSignup = findViewById(R.id.btnSignup);
        MaterialButton btnGoogleSignup = findViewById(R.id.btnGoogleSignup);
        TextView tvLogin = findViewById(R.id.tvLogin);

        btnSignup.setOnClickListener(v -> validateAndSignup());

        btnGoogleSignup.setOnClickListener(v ->
            Snackbar.make(v, "Google authentication will be implemented later.", Snackbar.LENGTH_LONG).show()
        );

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void validateAndSignup() {
        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        tilName.setError(null);
        tilEmail.setError(null);
        tilPhone.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);

        boolean isValid = true;

        if (!ValidationUtils.isNotEmpty(name)) {
            tilName.setError(getString(R.string.error_required));
            isValid = false;
        }

        if (!ValidationUtils.isNotEmpty(email)) {
            tilEmail.setError(getString(R.string.error_required));
            isValid = false;
        } else if (!ValidationUtils.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }

        if (!ValidationUtils.isNotEmpty(phone)) {
            tilPhone.setError(getString(R.string.error_required));
            isValid = false;
        } else if (!ValidationUtils.isValidPhone(phone)) {
            tilPhone.setError(getString(R.string.error_invalid_phone));
            isValid = false;
        }

        if (!ValidationUtils.isNotEmpty(password)) {
            tilPassword.setError(getString(R.string.error_required));
            isValid = false;
        } else if (!ValidationUtils.isValidPassword(password)) {
            tilPassword.setError(getString(R.string.error_password_length));
            isValid = false;
        }

        if (!ValidationUtils.isNotEmpty(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.error_required));
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.error_password_mismatch));
            isValid = false;
        }

        if (!cbTerms.isChecked()) {
            Snackbar.make(cbTerms, getString(R.string.error_terms), Snackbar.LENGTH_SHORT).show();
            isValid = false;
        }

        if (isValid) {
            Snackbar.make(tilEmail, "Registration validation successful, account creation next phase", Snackbar.LENGTH_LONG).show();
        }
    }
}