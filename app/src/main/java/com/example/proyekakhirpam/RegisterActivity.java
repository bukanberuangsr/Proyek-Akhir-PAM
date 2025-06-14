package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btnDaftar;
    private TextView loginLink;
    FirebaseAuth mAuth; // Tambahkan FirebaseAuth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnDaftar = findViewById(R.id.button_daftar);
        loginLink = findViewById(R.id.login_link);

        mAuth = FirebaseAuth.getInstance(); // Inisialisasi FirebaseAuth

        btnDaftar.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Ambil UID user setelah berhasil daftar
                            String uid = mAuth.getCurrentUser().getUid();

                            // Buat data user
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("username", username);
                            userMap.put("email", email);
                            userMap.put("createdAt", System.currentTimeMillis());

                            // Simpan ke Firestore
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(uid)
                                    .set(userMap)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(RegisterActivity.this, "Gagal menyimpan user: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registrasi gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
        
        loginLink.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }
}