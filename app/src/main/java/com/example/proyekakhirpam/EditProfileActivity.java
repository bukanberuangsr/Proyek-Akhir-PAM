package com.example.proyekakhirpam;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etNama, etTanggalLahir, etBio;
    private ImageView btnPickDate, btnBack;
    private TextView tvBatal;
    private Button btnSimpan;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Inisialisasi View
        etNama = findViewById(R.id.etNama);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        etBio = findViewById(R.id.etBio);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnBack = findViewById(R.id.btnBack);
        tvBatal = findViewById(R.id.tvBatal);
        btnSimpan = findViewById(R.id.btnSimpan);

        // Shared Preferences untuk menyimpan data
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        // Load Data yang sudah tersimpan
        loadData();

        // Event Click untuk memilih tanggal lahir
        btnPickDate.setOnClickListener(v -> showDatePickerDialog());
        etTanggalLahir.setOnClickListener(v -> showDatePickerDialog());

        // Tombol Simpan untuk menyimpan perubahan
        btnSimpan.setOnClickListener(v -> saveData());

        // Tombol Kembali untuk kembali ke halaman ProfileActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, InfoProfileActivity.class);
            startActivity(intent);
            finish();
        });

        // Tombol Batal untuk menampilkan pop-up konfirmasi
        tvBatal.setOnClickListener(v -> showCancelDialog());
    }

    // Menampilkan DatePickerDialog untuk memilih tanggal lahir
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etTanggalLahir.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Menampilkan pop-up konfirmasi ketika tombol BATAL ditekan
    private void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Apakah Anda yakin ingin membatalkan perubahan?");
        builder.setPositiveButton("Ya", (dialog, which) -> finish());
        builder.setNegativeButton("Tidak", null);
        builder.show();
    }

    // Menyimpan data ke SharedPreferences
    private void saveData() {
        String nama = etNama.getText().toString().trim();
        String tanggalLahir = etTanggalLahir.getText().toString().trim();
        String bio = etBio.getText().toString().trim();

        // Simpan ke SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nama", nama);
        editor.putString("tanggalLahir", tanggalLahir);
        editor.putString("bio", bio);
        editor.apply();

        // Pindah ke ProfileActivity setelah menyimpan
        Intent intent = new Intent(EditProfileActivity.this, InfoProfileActivity.class);
        startActivity(intent);
        finish();
    }

    // Memuat data yang telah disimpan sebelumnya
    private void loadData() {
        String nama = sharedPreferences.getString("nama", "");
        String tanggalLahir = sharedPreferences.getString("tanggalLahir", "");
        String bio = sharedPreferences.getString("bio", "");

        etNama.setText(nama);
        etTanggalLahir.setText(tanggalLahir);
        etBio.setText(bio);
    }
}