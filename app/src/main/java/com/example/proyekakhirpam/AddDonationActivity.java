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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddDonationActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private View btnBack;
    private EditText etJudulDonasi, etDeskripsiDonasi, etTanggalSelesai, etNominalDonasi;
    private Spinner spTipeDonasi;
    private FrameLayout uploadPlaceholder;
    private ImageView ivPreview;
    private Uri selectedImageUri;
    private String uploadedImageUrl;
    private Button btnBagikan;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_donation_activity);

        uploadPlaceholder = findViewById(R.id.upload_placeholder);
        ivPreview = findViewById(R.id.iv_preview);
        btnBack = findViewById(R.id.btn_back);
        spTipeDonasi = findViewById(R.id.tipe_donasi);
        etJudulDonasi = findViewById(R.id.et_judul_donasi);
        etDeskripsiDonasi = findViewById(R.id.et_deskripsi_donasi);
        etNominalDonasi = findViewById(R.id.et_nominal);
        etTanggalSelesai = findViewById(R.id.tanggal_selesai);
        btnBagikan = findViewById(R.id.btn_bagikan);

        btnBack.setOnClickListener(v -> finish());
        etTanggalSelesai.setOnClickListener(v -> showDatePicker());
        uploadPlaceholder.setOnClickListener(v-> pickImageFromGallery());

        spTipeDonasi.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedTipe = parent.getItemAtPosition(position).toString();
                if ("Internasional".equalsIgnoreCase(selectedTipe)) {
                    Toast.makeText(AddDonationActivity.this, "Internasional selected", Toast.LENGTH_SHORT).show();
                    // Handle Internasional selection
                } else if ("Nasional".equalsIgnoreCase(selectedTipe)) {
                    Toast.makeText(AddDonationActivity.this, "Nasional selected", Toast.LENGTH_SHORT).show();
                    // Handle Nasional selection
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                Toast.makeText(AddDonationActivity.this, "Pilih tipe donasi terlebih dahulu!", Toast.LENGTH_SHORT).show();
            }
        });

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

        btnBagikan.setOnClickListener(v-> submitForm());

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
    private void submitForm() {
        String judulDonasi = etJudulDonasi.getText().toString();
        String deskripsiDonasi = etDeskripsiDonasi.getText().toString();
        String tanggalStr = etTanggalSelesai.getText().toString();
        String nominalDonasi = etNominalDonasi.getText().toString();

        if (selectedImageUri == null) {
            Toast.makeText(this, "Upload foto Donasi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (judulDonasi.isEmpty() || deskripsiDonasi.isEmpty() || tanggalStr.isEmpty() || nominalDonasi.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        int nominal = Integer.parseInt(nominalDonasi);
        Date tanggalSelesai = convertDateStrToDate(tanggalStr);
        String tipeDonasi = spTipeDonasi.getSelectedItem().toString();

        try {
            File file = CloudinaryManager.uriToFile(this, selectedImageUri);
            btnBagikan.setEnabled(false);
            btnBagikan.setText("Mengupload...");
            CloudinaryManager.uploadImage(file, new CloudinaryManager.UploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    uploadedImageUrl = imageUrl;
                    saveToFirestore(judulDonasi, deskripsiDonasi, tanggalSelesai, nominal, uploadedImageUrl, tipeDonasi);
                }
                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        btnBagikan.setEnabled(true);
                        btnBagikan.setText("Bagikan");
                        Toast.makeText(AddDonationActivity.this, "Gagal upload gambar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, "Gagal membaca gambar", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveToFirestore(String judulDonasi, String deskripsiDonasi, Date tanggalSelesai, int nominalDonasi, String imageUrl, String tipeDonasi) {
        Map<String, Object> data = new HashMap<>();
        data.put("judul_donasi", judulDonasi);
        data.put("deskripsi_donasi", deskripsiDonasi);
        data.put("tanggal_selesai", tanggalSelesai);
        data.put("nominal_donasi", nominalDonasi);
        data.put("gambar_url", imageUrl);
        data.put("tanggal_dibagikan", com.google.firebase.Timestamp.now());
        data.put("isAvailable", true);
        data.put("tipe_donasi", tipeDonasi);

        // TODO: tambahkan donor_id dari user login jika ada
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String donorId = user.getUid();
            data.put("nama_donatur", user.getDisplayName());
        } else {
            Toast.makeText(this, "Harus login untuk membuat donasi!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("donation").add(data)
                .addOnSuccessListener(documentReference -> runOnUiThread(() -> {
                    btnBagikan.setEnabled(true);
                    btnBagikan.setText("Bagikan");
                    Toast.makeText(this, "Donasi berhasil disimpan!", Toast.LENGTH_LONG).show();
                    finish(); // kembali ke halaman sebelumnya
                }))
                .addOnFailureListener(e -> runOnUiThread(() -> {
                    btnBagikan.setEnabled(true);
                    btnBagikan.setText("Bagikan");
                    Toast.makeText(this, "Gagal menambahkan donasi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }));
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