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

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FormPostinganActivity extends AppCompatActivity {

    private EditText etNama, etDeskripsi;
    private ImageView imagePreview, iconUpload;
    private TextView textUpload;
    private Button btnPilihGambar, btnSubmit;
    private Uri imageUri;

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    imagePreview.setImageURI(imageUri);
                    imagePreview.setVisibility(View.VISIBLE);
                    iconUpload.setVisibility(View.GONE);
                    textUpload.setVisibility(View.GONE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_postingan);

        etNama = findViewById(R.id.etNama);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        imagePreview = findViewById(R.id.imagePreview);
        iconUpload = findViewById(R.id.iconUpload);
        textUpload = findViewById(R.id.textUpload);
        btnPilihGambar = findViewById(R.id.btnPilihGambar);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnPilihGambar.setOnClickListener(v -> pilihGambar());

        btnSubmit.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadKeCloudinaryDanSimpan();
            } else {
                Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
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
}
