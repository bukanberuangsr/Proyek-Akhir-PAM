package com.example.proyekakhirpam;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditDonationActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> pickImageLauncher;
    private EditText etJudulDonasi, etDeskripsiDonasi, etTanggalSelesai, etNominalDonasi;
    private Spinner spTipeDonasi;
    private FrameLayout uploadPlaceholder;
    private View btnBack;
    private ImageView ivPreview;
    private Uri selectedImageUri;
    private String uploadedImageUrl;
    private Button btnUpdateDonasi;
    private String donationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_donation);

        uploadPlaceholder = findViewById(R.id.upload_placeholder);
        ivPreview = findViewById(R.id.iv_preview);
        btnBack = findViewById(R.id.btn_back);
        spTipeDonasi = findViewById(R.id.tipe_donasi);
        etJudulDonasi = findViewById(R.id.et_judul_donasi);
        etDeskripsiDonasi = findViewById(R.id.et_deskripsi_donasi);
        etNominalDonasi = findViewById(R.id.et_nominal);
        etTanggalSelesai = findViewById(R.id.tanggal_selesai);
        btnUpdateDonasi = findViewById(R.id.btn_bagikan);

        // Get data from intent
        String judul = getIntent().getStringExtra("judul");
        String deskripsi = getIntent().getStringExtra("deskripsi");
        String tanggalSelesai = getIntent().getStringExtra("tanggal_selesai");
        String nominal = getIntent().getStringExtra("nominal");
        String jenisDonasi = getIntent().getStringExtra("jenis_donasi");
        String gambarUrl = getIntent().getStringExtra("gambar");
        donationId = getIntent().getStringExtra("donationId");

        // Set to input fields
        etJudulDonasi.setText(judul);
        etDeskripsiDonasi.setText(deskripsi);
        etTanggalSelesai.setText(tanggalSelesai);
        etNominalDonasi.setText(nominal);
        uploadedImageUrl = gambarUrl; // for update

        if (gambarUrl != null && !gambarUrl.isEmpty()) {
            ivPreview.setVisibility(View.VISIBLE);
            Glide.with(this).load(gambarUrl).into(ivPreview);
        }

        // Set spinner selection if needed
        if (jenisDonasi != null) {
            for (int i = 0; i < spTipeDonasi.getCount(); i++) {
                if (spTipeDonasi.getItemAtPosition(i).toString().equals(jenisDonasi)) {
                    spTipeDonasi.setSelection(i);
                    break;
                }
            }
        }

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        ivPreview.setVisibility(View.VISIBLE);
                        ivPreview.setImageURI(selectedImageUri);
                    }
                }
        );

        spTipeDonasi.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedTipe = parent.getItemAtPosition(position).toString();
                if ("Internasional".equalsIgnoreCase(selectedTipe)) {
                    Toast.makeText(EditDonationActivity.this, "Internasional selected", Toast.LENGTH_SHORT).show();
                    // Handle Internasional selection
                } else if ("Nasional".equalsIgnoreCase(selectedTipe)) {
                    Toast.makeText(EditDonationActivity.this, "Nasional selected", Toast.LENGTH_SHORT).show();
                    // Handle Nasional selection
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                Toast.makeText(EditDonationActivity.this, "Pilih tipe donasi terlebih dahulu!", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
        etTanggalSelesai.setOnClickListener(v -> showDatePicker());
        uploadPlaceholder.setOnClickListener(v -> pickImageFromGallery());
        btnUpdateDonasi.setOnClickListener(v -> updateDonation());
    }

    private void updateDonation() {
        String judulDonasi = etJudulDonasi.getText().toString();
        String deskripsiDonasi = etDeskripsiDonasi.getText().toString();
        String nominalDonasi = etNominalDonasi.getText().toString();
        String tipeDonasi = spTipeDonasi.getSelectedItem().toString();
        String tanggalStr = etTanggalSelesai.getText().toString();

        Date tanggalSelesai = convertDateStrToDate(tanggalStr);

        if (judulDonasi.isEmpty() || deskripsiDonasi.isEmpty() || tanggalStr.isEmpty() || nominalDonasi.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri != null) {
            // User picked a new image, upload to Cloudinary
            try {
                File file = CloudinaryManager.uriToFile(this, selectedImageUri);
                Toast.makeText(this, "Uploading image...", Toast.LENGTH_SHORT).show();
                CloudinaryManager.uploadImage(file, new CloudinaryManager.UploadCallback() {
                    @Override
                    public void onSuccess(String imageUrl) {
                        uploadedImageUrl = imageUrl;
                        updateDonationFirestore(judulDonasi, deskripsiDonasi, nominalDonasi, tipeDonasi, tanggalSelesai, uploadedImageUrl);
                    }
                    @Override
                    public void onError(Exception e) {
                        runOnUiThread(() -> Toast.makeText(EditDonationActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
            } catch (Exception e) {
                Toast.makeText(this, "Failed to read image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // No new image, use existing URL
            updateDonationFirestore(judulDonasi, deskripsiDonasi, nominalDonasi, tipeDonasi, tanggalSelesai, uploadedImageUrl);
        }
    }

    private void updateDonationFirestore(String judulDonasi, String deskripsiDonasi, String nominalDonasi, String tipeDonasi, Date tanggalSelesai, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("donation").document(donationId)
                .update(
                        "judul_donasi", judulDonasi,
                        "deskripsi_donasi", deskripsiDonasi,
                        "nominal_donasi", Integer.parseInt(nominalDonasi),
                        "tipe_donasi", tipeDonasi,
                        "tanggal_selesai", tanggalSelesai,
                        "gambar_url", imageUrl
                )
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Donation updated successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error updating donation: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showDatePicker(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    // Format tanggal
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    etTanggalSelesai.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    private Date convertDateStrToDate(String dateStr) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return new Date();
        }
    }
}