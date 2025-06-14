package com.example.proyekakhirpam;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

        etNama = findViewById(R.id.etNama);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        etBio = findViewById(R.id.etBio);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnBack = findViewById(R.id.btnBack);
        tvBatal = findViewById(R.id.tvBatal);
        btnSimpan = findViewById(R.id.btnSimpan);

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        loadData();

        btnPickDate.setOnClickListener(v -> showDatePickerDialog());
        etTanggalLahir.setOnClickListener(v -> showDatePickerDialog());

        btnSimpan.setOnClickListener(v -> saveData());
        btnBack.setOnClickListener(v -> finish());
        tvBatal.setOnClickListener(v -> showCancelDialog());
    }

    private void loadData() {
        String nama = sharedPreferences.getString("nama", "");
        String tanggal = sharedPreferences.getString("tanggalLahir", "");
        String bio = sharedPreferences.getString("bio", "");

        if (nama.isEmpty()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            etNama.setText(user != null && user.getDisplayName() != null ? user.getDisplayName() : "");
        } else {
            etNama.setText(nama);
        }

        etTanggalLahir.setText(tanggal.isEmpty() ? generateRandomDate() : tanggal);
        etBio.setText(bio);
    }

    private String generateRandomDate() {
        int year = 1990 + new Random().nextInt(15);
        int month = 1 + new Random().nextInt(12);
        int day = 1 + new Random().nextInt(28);
        return day + "/" + month + "/" + year;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etTanggalLahir.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showCancelDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin membatalkan perubahan?")
                .setPositiveButton("Ya", (dialog, which) -> finish())
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void saveData() {
        String nama = etNama.getText().toString().trim();
        String tanggal = etTanggalLahir.getText().toString().trim();
        String bio = etBio.getText().toString().trim();

        // Simpan ke SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nama", nama);
        editor.putString("tanggalLahir", tanggal);
        editor.putString("bio", bio);
        editor.apply();

        // Simpan ke Firestore
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            Map<String, Object> userData = new HashMap<>();
            userData.put("nama", nama);
            userData.put("tanggalLahir", tanggal);
            userData.put("bio", bio);

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .set(userData)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish(); // kembali ke InfoProfileActivity → ProfileFragment
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Gagal menyimpan ke Firebase", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show();
        }
    }
}
