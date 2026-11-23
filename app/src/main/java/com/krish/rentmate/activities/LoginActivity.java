package com.krish.rentmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.krish.rentmate.R;
import com.krish.rentmate.models.User;
import com.krish.rentmate.repositories.AuthRepository;
import com.krish.rentmate.repositories.RepositoryCallback;
import com.krish.rentmate.utils.ValidationUtils;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        
        btnLogin = findViewById(R.id.btnLogin);
        MaterialButton btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        TextView tvSignup = findViewById(R.id.tvSignup);

        btnLogin.setOnClickListener(v -> validateAndLogin());

        btnGoogleLogin.setOnClickListener(v -> 
            Snackbar.make(v, "Google Sign-In requires SHA-1 configuration in Firebase Console.", Snackbar.LENGTH_LONG).show()
        );

        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        tvSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });
    }

    private void validateAndLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        tilEmail.setError(null);
        tilPassword.setError(null);

        boolean isValid = true;

        if (!ValidationUtils.isNotEmpty(email)) {
            tilEmail.setError(getString(R.string.error_required));
            isValid = false;
        } else if (!ValidationUtils.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }

        if (!ValidationUtils.isNotEmpty(password)) {
            tilPassword.setError(getString(R.string.error_required));
            isValid = false;
        }

        if (isValid) {
            setLoading(true);
            AuthRepository.getInstance().login(email, password, new RepositoryCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    setLoading(false);
                    Intent intent = new Intent(LoginActivity.this, MainDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String errorMessage) {
                    setLoading(false);
                    Snackbar.make(btnLogin, errorMessage, Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    private void setLoading(boolean isLoading) {
        btnLogin.setEnabled(!isLoading);
        btnLogin.setText(isLoading ? "Signing in..." : getString(R.string.log_in));
    }
}