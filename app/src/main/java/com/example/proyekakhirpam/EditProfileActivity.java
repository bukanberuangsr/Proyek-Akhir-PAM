package com.example.proyekakhirpam;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etNama, etTanggalLahir, etBio;
    private ImageView btnPickDate, btnBack, imgProfile;
    private TextView tvBatal;
    private Button btnSimpan;
    private ImageView btnEditFoto;
    private static final int PICK_IMAGE_REQUEST = 101;
    private Uri selectedImageUri = null;
    private String currentPhotoUrl = "";
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etNama = findViewById(R.id.etNama);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        etBio = findViewById(R.id.etBio);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnBack = findViewById(R.id.btnBack);
        tvBatal = findViewById(R.id.tvBatal);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnEditFoto = findViewById(R.id.btnEditPhoto);
        imgProfile = findViewById(R.id.imgProfile);

        uid = getIntent().getStringExtra("uid");
        if (uid == null) {
            Toast.makeText(this, "UID tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchFromFirestore(uid);

        btnPickDate.setOnClickListener(v -> showDatePickerDialog());
        etTanggalLahir.setOnClickListener(v -> showDatePickerDialog());

        btnSimpan.setOnClickListener(v -> saveData());
        btnBack.setOnClickListener(v -> finish());
        btnEditFoto.setOnClickListener(v -> uploadFoto());
        tvBatal.setOnClickListener(v -> showCancelDialog());
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
                        currentPhotoUrl = photoUrl != null ? photoUrl : "";
                        if (currentPhotoUrl != null && !currentPhotoUrl.isEmpty()) {
                            Glide.with(this).load(currentPhotoUrl).into(imgProfile);
                        } else {
                            imgProfile.setImageResource(R.drawable.ic_profile);
                        }
                    } else {
                        etNama.setText("");
                        etTanggalLahir.setText("");
                        etBio.setText("");
                        imgProfile.setImageResource(R.drawable.ic_profile);
                    }
                });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etTanggalLahir.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.setOnShowListener(dialog -> {
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(android.R.color.black));
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.black));
        });

        datePickerDialog.show();
    }

    private void showCancelDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin membatalkan perubahan?")
                .setPositiveButton("Ya", (dialogInterface, which) -> finish())
                .setNegativeButton("Tidak", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.black));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.black));
        });

        dialog.show();
    }

    private void saveData() {
        String nama = etNama.getText().toString().trim();
        String tanggal = etTanggalLahir.getText().toString().trim();
        String bio = etBio.getText().toString().trim();

        if (nama.isEmpty() || tanggal.isEmpty()) {
            Toast.makeText(this, "Nama dan tanggal lahir tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", nama);
        userData.put("tanggalLahir", tanggal);
        userData.put("bio", bio);
        userData.put("photo_url", currentPhotoUrl); // selalu set foto

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .set(userData, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal menyimpan ke Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imgProfile.setImageURI(selectedImageUri);

            // Upload ke Cloudinary (atau storage lain)
            try {
                File imageFile = CloudinaryManager.uriToFile(this, selectedImageUri);
                Toast.makeText(this, "Mengunggah foto...", Toast.LENGTH_SHORT).show();
                CloudinaryManager.uploadImage(imageFile, new CloudinaryManager.UploadCallback() {
                    @Override
                    public void onSuccess(String imageUrl) {
                        saveProfileImageUrl(imageUrl);
                    }
                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Upload gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                Toast.makeText(this, "Gagal membaca file gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProfileImageUrl(String imageUrl) {
        Map<String, Object> update = new HashMap<>();
        update.put("photo_url", imageUrl);
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .set(update, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    currentPhotoUrl = imageUrl;
                    Glide.with(this).load(imageUrl).into(imgProfile);
                    Toast.makeText(this, "Foto profil berhasil diupdate!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal update foto profil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
}