package com.example.proyekakhirpam;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;


public class AddFoodActivity extends AppCompatActivity {
    private EditText etNamaMakanan, etRestoran, etJumlah, etTanggal, etHarga;
    private FrameLayout uploadPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // Inisialisasi view
        uploadPlaceholder = findViewById(R.id.upload_placeholder);
        etNamaMakanan = findViewById(R.id.et_nama_makanan);
        etRestoran = findViewById(R.id.et_nama_restoran);
        etJumlah = findViewById(R.id.et_jumlah);
        etTanggal = findViewById(R.id.et_tanggal);
        etHarga = findViewById(R.id.et_harga);
    }}