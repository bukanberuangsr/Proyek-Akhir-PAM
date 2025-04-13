package com.example.proyekakhirpam;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {
    private ImageView btnBack;
    private Button btnBatalkanSteak, btnBatalkanBaguette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Inisialisasi komponen UI
        btnBack = findViewById(R.id.btnBack);
        btnBatalkanSteak = findViewById(R.id.btnBatalkanSteak);

        // Event klik tombol back
        btnBack.setOnClickListener(v -> finish());

        // Event klik tombol "Batalkan" untuk Steak
        btnBatalkanSteak.setOnClickListener(v -> {
            Toast.makeText(HistoryActivity.this, "Pesanan Steak berhasil dibatalkan", Toast.LENGTH_SHORT).show();
        });

        // Event klik tombol "Batalkan" untuk Baguette
    }
}