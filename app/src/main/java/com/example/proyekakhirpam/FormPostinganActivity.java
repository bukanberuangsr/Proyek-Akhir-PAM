package com.example.proyekakhirpam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FormPostinganActivity extends AppCompatActivity {

    private EditText etNama, etDeskripsi;
    private ImageView imagePreview;
    private Button btnPilihGambar, btnSubmit;
    private Uri imageUri;

    private String documentId = null;
    private String existingImageUrl = null;
    private boolean isEditMode = false;
    ImageView backIcon;
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    imagePreview.setImageURI(imageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_postingan);

        etNama = findViewById(R.id.etNama);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        imagePreview = findViewById(R.id.imagePreview);
        btnPilihGambar = findViewById(R.id.btnPilihGambar);
        btnSubmit = findViewById(R.id.btnSubmit);
        backIcon = findViewById(R.id.backIcon);
        ImageView iconUpload = findViewById(R.id.iconUpload);
        TextView textUpload = findViewById(R.id.textUpload);

        btnPilihGambar.setOnClickListener(v -> pilihGambar());
        backIcon.setOnClickListener(v -> finish());

        // Cek apakah ini mode edit
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEdit", false);

        if (isEditMode) {
            documentId = intent.getStringExtra("postinganId"); // sesuai dengan yang dikirim
            String nama = intent.getStringExtra("nama");
            String deskripsi = intent.getStringExtra("deskripsi");
            existingImageUrl = intent.getStringExtra("image"); // harus sama dengan yang dikirim

            etNama.setText(nama);
            etDeskripsi.setText(deskripsi);

            // Tampilkan gambar dari Cloudinary jika ada
            if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
                Glide.with(this)
                        .load(existingImageUrl)
                        .placeholder(R.drawable.pc_makanan) // fallback jika loading
                        .into(imagePreview);

                imagePreview.setVisibility(View.VISIBLE);
                iconUpload.setVisibility(View.GONE);
                textUpload.setVisibility(View.GONE);
            }

            btnSubmit.setText("Update");
        }

        btnSubmit.setOnClickListener(v -> {
            if (isEditMode) {
                if (imageUri != null) {
                    uploadKeCloudinaryDanUpdate();
                } else {
                    updateFirestore(existingImageUrl);
                }
            } else {
                if (imageUri != null) {
                    uploadKeCloudinaryDanSimpan();
                } else {
                    Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void pilihGambar() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void uploadKeCloudinaryDanSimpan() {
        try {
            File imageFile = CloudinaryManager.uriToFile(this, imageUri);
            CloudinaryManager.uploadImage(imageFile, new CloudinaryManager.UploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    simpanKeFirestore(imageUrl);
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> Toast.makeText(FormPostinganActivity.this, "Upload gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal mengubah URI ke File: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void simpanKeFirestore(String imageUrl) {
        String nama = etNama.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();

        if (nama.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(this, "Nama dan Deskripsi harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> post = new HashMap<>();
        post.put("nama", nama);
        post.put("deskripsi", deskripsi);
        post.put("imageUrl", imageUrl);

        firestore.collection("postingan")
                .add(post)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Postingan berhasil disimpan!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Gagal simpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void uploadKeCloudinaryDanUpdate() {
        try {
            File imageFile = CloudinaryManager.uriToFile(this, imageUri);
            CloudinaryManager.uploadImage(imageFile, new CloudinaryManager.UploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    // Setelah upload gambar berhasil, lanjut update data ke Firestore
                    updateFirestore(imageUrl);
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> Toast.makeText(FormPostinganActivity.this, "Upload gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal mengubah URI ke File: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFirestore(String imageUrl) {
        String nama = etNama.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();

        if (nama.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(this, "Nama dan Deskripsi harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updatedPost = new HashMap<>();
        updatedPost.put("nama", nama);
        updatedPost.put("deskripsi", deskripsi);
        updatedPost.put("imageUrl", imageUrl);

        firestore.collection("postingan").document(documentId)
                .update(updatedPost)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Postingan berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Gagal update: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        Intent resultIntent = new Intent();
        resultIntent.putExtra("nama", nama);
        resultIntent.putExtra("deskripsi", deskripsi);
        resultIntent.putExtra("image", imageUrl);
        setResult(RESULT_OK, resultIntent);
        finish();

    }

}
