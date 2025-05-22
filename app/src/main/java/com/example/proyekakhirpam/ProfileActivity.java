package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private ImageView btnBack, imgProfile;
    private TextView tvEdit;
    private EditText etNama, etTanggalLahir, etBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inisialisasi View
        btnBack = findViewById(R.id.btnBack);
        imgProfile = findViewById(R.id.imgProfile);
        tvEdit = findViewById(R.id.tvEdit);
        etNama = findViewById(R.id.etNama);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        etBio = findViewById(R.id.etBio);

        // Tombol kembali
        btnBack.setOnClickListener(v -> onBackPressed());

        // Navigasi ke Edit Profile
        tvEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
            finish(); // Menutup ProfileActivity agar tidak menumpuk
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Pastikan ProfileActivity ditutup saat kembali ke AccountActivity
    }
}
