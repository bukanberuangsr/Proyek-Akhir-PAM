package com.example.proyekakhirpam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class InfoProfileActivity extends AppCompatActivity {

    private ImageView btnBack, imgProfile, icNama, icTanggal, icBio;
    private TextView tvEdit;
    private EditText etNama, etTanggalLahir, etBio;
    private SharedPreferences sharedPreferences;

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

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        checkInitialData();
        fetchFromFirestore();

        btnBack.setOnClickListener(v -> finish());

        tvEdit.setOnClickListener(v -> {
            Intent intent = new Intent(InfoProfileActivity.this, EditProfileActivity.class);
            startActivityForResult(intent, 100);
        });
    }

    private void checkInitialData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String currentNama = sharedPreferences.getString("nama", "");
        if (currentNama.isEmpty()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String firebaseName = user.getDisplayName();
                if (firebaseName != null && !firebaseName.isEmpty()) {
                    editor.putString("nama", firebaseName);
                } else if (user.getEmail() != null) {
                    editor.putString("nama", user.getEmail());
                }
            }
        }

        String currentTanggal = sharedPreferences.getString("tanggalLahir", "");
        if (currentTanggal.isEmpty()) {
            editor.putString("tanggalLahir", generateRandomBirthdate());
        }

        editor.apply();
    }

    private void fetchFromFirestore() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Gunakan 'username' agar konsisten dengan ProfileFragment
                        String nama = documentSnapshot.getString("username");
                        String tanggal = documentSnapshot.getString("tanggalLahir");
                        String bio = documentSnapshot.getString("bio");

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (nama != null) editor.putString("nama", nama);
                        if (tanggal != null) editor.putString("tanggalLahir", tanggal);
                        if (bio != null) editor.putString("bio", bio);
                        editor.apply();

                        loadData();
                    } else {
                        loadData();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal ambil data dari Firestore", Toast.LENGTH_SHORT).show();
                    loadData();
                });
    }

    private void loadData() {
        String nama = sharedPreferences.getString("nama", "");
        String tanggal = sharedPreferences.getString("tanggalLahir", "");
        String bio = sharedPreferences.getString("bio", "");

        etNama.setText(nama);
        etTanggalLahir.setText(tanggal);
        etBio.setText(bio);

        icNama.setColorFilter(nama.isEmpty() ? getColor(R.color.grey) : getColor(R.color.green));
        icTanggal.setColorFilter(tanggal.isEmpty() ? getColor(R.color.grey) : getColor(R.color.green));
        icBio.setColorFilter(bio.isEmpty() ? getColor(R.color.grey) : getColor(R.color.green));
    }

    private String generateRandomBirthdate() {
        Random rand = new Random();
        int year = rand.nextInt(2005 - 1980 + 1) + 1980;
        int month = rand.nextInt(12) + 1;
        int day = rand.nextInt(28) + 1;
        return day + "/" + month + "/" + year;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            fetchFromFirestore(); // ambil ulang data yang baru disimpan
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
