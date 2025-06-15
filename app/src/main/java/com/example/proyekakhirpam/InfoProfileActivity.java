package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class InfoProfileActivity extends AppCompatActivity {

    private ImageView btnBack, imgProfile, icNama, icTanggal, icBio;
    private TextView tvEdit;
    private EditText etNama, etTanggalLahir, etBio;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_profile);

        btnBack = findViewById(R.id.btnBack);
        imgProfile = findViewById(R.id.imgProfile);
        tvEdit = findViewById(R.id.tvEdit);
        etNama = findViewById(R.id.etNama);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        etBio = findViewById(R.id.etBio);
        icNama = findViewById(R.id.icCentangNama);
        icTanggal = findViewById(R.id.icCentangTanggal);
        icBio = findViewById(R.id.icCentangBio);

        uid = getIntent().getStringExtra("uid");
        if (uid == null) {
            Toast.makeText(this, "UID tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchFromFirestore(uid);

        btnBack.setOnClickListener(v -> finish());

        tvEdit.setOnClickListener(v -> {
            Intent intent = new Intent(InfoProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("uid", uid);
            startActivityForResult(intent, 100);
        });
    }

    private void fetchFromFirestore(String uid) {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nama = documentSnapshot.getString("username");
                        String tanggal = documentSnapshot.getString("tanggalLahir");
                        String bio = documentSnapshot.getString("bio");
                        String photoUrl = documentSnapshot.getString("photo_url");

                        etNama.setText(nama != null ? nama : "");
                        etTanggalLahir.setText(tanggal != null ? tanggal : "");
                        etBio.setText(bio != null ? bio : "");

                        icNama.setColorFilter(nama == null || nama.isEmpty() ? getColor(R.color.grey) : getColor(R.color.green));
                        icTanggal.setColorFilter(tanggal == null || tanggal.isEmpty() ? getColor(R.color.grey) : getColor(R.color.green));
                        icBio.setColorFilter(bio == null || bio.isEmpty() ? getColor(R.color.grey) : getColor(R.color.green));

                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            Glide.with(this).load(photoUrl).into(imgProfile);
                        } else {
                            imgProfile.setImageResource(R.drawable.ic_profile);
                        }
                    } else {
                        Toast.makeText(this, "Data user tidak ditemukan", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal ambil data dari Firestore", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (uid != null) fetchFromFirestore(uid);
        }
    }
}