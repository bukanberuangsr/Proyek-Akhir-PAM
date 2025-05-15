package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {
    private ImageView btnBack, imgProfile;
    private TextView tvDonasiSaya, tvRiwayatDonasi;
    private Button btnKeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Inisialisasi komponen UI
        btnBack = findViewById(R.id.btnBack);
        imgProfile = findViewById(R.id.imgProfile);
        tvDonasiSaya = findViewById(R.id.tvDonasiSaya);
        tvRiwayatDonasi = findViewById(R.id.tvRiwayatDonasi);
        btnKeluar = findViewById(R.id.btnKeluar);

        // Event klik tombol back
        btnBack.setOnClickListener(v -> finish());

        // Event klik foto profil untuk masuk ke ProfileActivity
        imgProfile.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

//        // Event klik teks "Donasi Saya"
//        tvDonasiSaya.setOnClickListener(v -> {
//            Intent intent = new Intent(AccountActivity.this, DonasiActivity.class);
//            startActivity(intent);
//        });

        // Event klik teks "Riwayat Donasi" menuju ke halaman daftar pembelian (HistoryActivity)
        tvRiwayatDonasi.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

//        // Event klik tombol keluar
//        btnKeluar.setOnClickListener(v -> {
//            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        });

    }
}
