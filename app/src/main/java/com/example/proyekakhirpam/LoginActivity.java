package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText etEmail, etPassword;
    private TextView registerLink;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi komponen
        btnLogin = findViewById(R.id.button_login);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        registerLink = findViewById(R.id.register_link);

        mAuth = FirebaseAuth.getInstance();

        // Event handler untuk tombol login
        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Email & Password must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            // Login firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                            // Arahkan ke MainActivity atau halaman utama
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Event handler untuk link register
        registerLink.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}