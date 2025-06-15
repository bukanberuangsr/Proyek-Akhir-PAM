package com.example.proyekakhirpam;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditDonationActivity extends AppCompatActivity {

    private EditText etJudulDonasi, etDeskripsiDonasi, etTanggalSelesai, etNominalDonasi;
    private Spinner spTipeDonasi;
    private FrameLayout uploadPlaceholder;
    private View btnBack;
    private ImageView ivPreview;
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

        // Set to input fields
        etJudulDonasi.setText(judul);
        etDeskripsiDonasi.setText(deskripsi);
        etTanggalSelesai.setText(tanggalSelesai);
        etNominalDonasi.setText(nominal);
        uploadedImageUrl = gambarUrl; // for update

        // Set spinner selection if needed
        if (jenisDonasi != null) {
            for (int i = 0; i < spTipeDonasi.getCount(); i++) {
                if (spTipeDonasi.getItemAtPosition(i).toString().equals(jenisDonasi)) {
                    spTipeDonasi.setSelection(i);
                    break;
                }
            }
        }

        donationId = getIntent().getStringExtra("donationId");

        btnBack.setOnClickListener(v -> finish());
        btnUpdateDonasi.setOnClickListener(v -> updateDonation());
    }

    private void updateDonation() {
        String judul = etJudulDonasi.getText().toString();
        String deskripsi = etDeskripsiDonasi.getText().toString();
        String nominal = etNominalDonasi.getText().toString();
        String tipeDonasi = spTipeDonasi.getSelectedItem().toString();
        String tanggalStr = etTanggalSelesai.getText().toString();

        // Convert tanggalStr to Timestamp
        com.google.firebase.Timestamp tanggalSelesai;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse(tanggalStr);
            tanggalSelesai = new com.google.firebase.Timestamp(date);
        } catch (Exception e) {
            tanggalSelesai = com.google.firebase.Timestamp.now();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("donation").document(donationId)
                .update(
                        "judul_donasi", judul,
                        "deskripsi_donasi", deskripsi,
                        "nominal_donasi", nominal,
                        "jenis_donasi", tipeDonasi,
                        "tanggal_selesai", tanggalSelesai,
                        "gambar_url", uploadedImageUrl // if you handle image update
                )
                .addOnSuccessListener(aVoid -> {
                    // Success, finish or show a message
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }
}