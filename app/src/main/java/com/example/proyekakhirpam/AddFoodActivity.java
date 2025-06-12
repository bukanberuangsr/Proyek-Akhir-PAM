package com.example.proyekakhirpam;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class AddFoodActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private View btnBack;
    private EditText etNamaMakanan, etRestoran, etJumlah, etTanggal, etHarga;
    private FrameLayout uploadPlaceholder;
    private Button btnKonfirmasi;
    private ImageView ivPreview;
    private Uri selectedImageUri;
    private String uploadedImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // Inisialisasi komponen UI
        uploadPlaceholder = findViewById(R.id.upload_placeholder);
        ivPreview = findViewById(R.id.iv_preview);
        etNamaMakanan = findViewById(R.id.et_nama_makanan);
        etRestoran = findViewById(R.id.et_nama_restoran);
        etJumlah = findViewById(R.id.et_jumlah);
        etTanggal = findViewById(R.id.et_tanggal);
        etHarga = findViewById(R.id.et_harga);
        btnBack = findViewById(R.id.btn_back);
        btnKonfirmasi = findViewById(R.id.btn_konfirmasi);

        // Pilih gambar
        uploadPlaceholder.setOnClickListener(v -> pickImageFromGallery());

        // Pilih tanggal (pakai DatePicker)
        etTanggal.setOnClickListener(v -> showDatePicker());

        // Tombol konfirmasi
        btnKonfirmasi.setOnClickListener(v -> submitForm());

        // Tombol back
        btnBack.setOnClickListener(v -> finish());

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

        uploadPlaceholder.setOnClickListener(v -> pickImageFromGallery());

    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            // Tampilkan preview bisa ditambah (pakai ImageView)
            ivPreview.setVisibility(View.VISIBLE);
            ivPreview.setImageURI(selectedImageUri);
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String tanggal = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month+1, year);
            etTanggal.setText(tanggal);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    private void submitForm() {
        String namaMakanan = etNamaMakanan.getText().toString().trim();
        String restoran = etRestoran.getText().toString().trim();
        String jumlahStr = etJumlah.getText().toString().trim();
        String tanggalStr = etTanggal.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();

        // Validasi sederhana
        if (selectedImageUri == null) {
            Toast.makeText(this, "Upload foto makanan!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (namaMakanan.isEmpty() || restoran.isEmpty() || jumlahStr.isEmpty() || tanggalStr.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        int jumlah = Integer.parseInt(jumlahStr);
        int harga = hargaStr.isEmpty() ? 0 : Integer.parseInt(hargaStr);

        // Konversi tanggal ke Timestamp (bisa diubah sesuai kebutuhan Firestore)
        Timestamp expiredDate = convertDateStrToTimestamp(tanggalStr);

        // Upload gambar ke Cloudinary
        try {
            File file = CloudinaryManager.uriToFile(this, selectedImageUri);
            btnKonfirmasi.setEnabled(false);
            btnKonfirmasi.setText("Mengupload...");
            CloudinaryManager.uploadImage(file, new CloudinaryManager.UploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    uploadedImageUrl = imageUrl;
                    saveToFirestore(namaMakanan, restoran, jumlah, expiredDate, harga, uploadedImageUrl);
                }
                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        btnKonfirmasi.setEnabled(true);
                        btnKonfirmasi.setText("Konfirmasi");
                        Toast.makeText(AddFoodActivity.this, "Gagal upload gambar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, "Gagal membaca gambar", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToFirestore(String namaMakanan, String restoran, int jumlah, Timestamp expiredDate, int harga, String imageUrl) {
        Map<String, Object> data = new HashMap<>();
        data.put("nama_makanan", namaMakanan);
        data.put("donor_nama", restoran);
        data.put("jumlah", jumlah);
        data.put("tanggal_expired", expiredDate);
        data.put("harga", harga);
        data.put("gambar_url", imageUrl);
        data.put("tanggal_dibagikan", Timestamp.now());
        data.put("isAvailable", true);

        // TODO: tambahkan donor_id dari user login jika ada
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String donorId = user.getUid();
            data.put("donor_id", user.getUid());
        } else {
            Toast.makeText(this, "Harus login untuk menyumbang makanan!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("food-pickup").add(data)
                .addOnSuccessListener(documentReference -> runOnUiThread(() -> {
                    btnKonfirmasi.setEnabled(true);
                    btnKonfirmasi.setText("Konfirmasi");
                    Toast.makeText(this, "Donasi makanan berhasil disimpan!", Toast.LENGTH_LONG).show();
                    finish(); // kembali ke halaman sebelumnya
                }))
                .addOnFailureListener(e -> runOnUiThread(() -> {
                    btnKonfirmasi.setEnabled(true);
                    btnKonfirmasi.setText("Konfirmasi");
                    Toast.makeText(this, "Gagal menambahkan donasi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    private Timestamp convertDateStrToTimestamp(String dateStr) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdf.parse(dateStr);
            return new Timestamp(date);
        } catch (Exception e) {
            return Timestamp.now();
        }
    }
}