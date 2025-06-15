package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class DonateDetailActivity extends AppCompatActivity {

    ImageView ivGambarDonasi;
    TextView tvJudulDonasi, tvDeskripsiDonasi, tvTanggalSelesai, tvNominalDonasi;
    Button btnDonatePayment, btnDelete, btnUpdate;
    Spinner spTipeDonasi;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_detail);

        ivGambarDonasi = findViewById(R.id.img_main);
        tvJudulDonasi = findViewById(R.id.tv_main);
        tvDeskripsiDonasi = findViewById(R.id.tv_description_content);
        tvTanggalSelesai = findViewById(R.id.tv_tanggal_selesai);
//        tvNominalDonasi = findViewById(R.id.tv_nominal_donasi);
//        spTipeDonasi = findViewById(R.id.sp_tipe_donasi);

        Intent intent = getIntent();
        String judulDonasi = intent.getStringExtra("judul");
        String gambarDonasi = intent.getStringExtra("gambar");
        String deskripsiDonasi = intent.getStringExtra("deskripsi");
        String tanggalSelesai = intent.getStringExtra("tanggal_selesai");

        tvJudulDonasi.setText(judulDonasi);
        tvDeskripsiDonasi.setText(deskripsiDonasi);
        Glide.with(this)
                .load(gambarDonasi)
                .into(ivGambarDonasi);
        tvTanggalSelesai.setText(tanggalSelesai);

        btnDonatePayment = findViewById(R.id.btn_donate_payment);
        btnDonatePayment.setOnClickListener(v -> {
            Intent paymentIntent = new Intent(this, DonatePayment.class);
            paymentIntent.putExtra("judul", judulDonasi);
            paymentIntent.putExtra("gambar", gambarDonasi);
            startActivity(paymentIntent);
        });

        btnDelete = findViewById(R.id.btn_delete);
        btnUpdate = findViewById(R.id.btn_update);
        String docId = intent.getStringExtra("donationId"); // Get the document ID from the intent
        btnDelete.setOnClickListener(v -> deleteDonation(docId));
        btnUpdate.setOnClickListener(v -> updateDonation(docId));
    }

    private void deleteDonation(String docId) {
        db.collection("donation").document(docId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Donation deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error deleting donation: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateDonation(String docId) {
        Intent intent = new Intent(this, EditDonationActivity.class);
//        intent.putExtra("donationId", docId);
//        intent.putExtra("judul", tvJudulDonasi.getText().toString());
//        intent.putExtra("gambar", getIntent().getStringExtra("gambar"));
//        intent.putExtra("deskripsi", tvDeskripsiDonasi.getText().toString());
//        intent.putExtra("tanggal_selesai", tvTanggalSelesai.getText().toString());
//        intent.putExtra("nominal", tvNominalDonasi.getText().toString());
//        intent.putExtra("jenis_donasi", spTipeDonasi.getSelectedItem().toString());
        startActivity(intent);
    }
}