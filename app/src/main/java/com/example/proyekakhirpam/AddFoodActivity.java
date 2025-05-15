package com.example.proyekakhirpam;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;


public class AddFoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);


        EditText etNamaMakanan = findViewById(R.id.et_nama_makanan);
        EditText etRestoran = findViewById(R.id.et_nama_restoran);
        EditText etJumlah = findViewById(R.id.et_jumlah);
        EditText etTanggal = findViewById(R.id.et_tanggal);
        EditText etHarga = findViewById(R.id.et_harga);

    }
}