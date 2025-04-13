package com.example.proyekakhirpam;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Calendar;

public class DonationActivity extends AppCompatActivity {

    private EditText etTanggalKadaluarsa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_activity);

        etTanggalKadaluarsa = findViewById(R.id.tanggal_kadaluarsa);
        etTanggalKadaluarsa.setOnClickListener(v -> showDatePicker());

        AppCompatButton btnBagikan = findViewById(R.id.btn_bagikan);
        btnBagikan.setOnClickListener(v->{
            // Masukkan data ke halaman home dlm bentuk card
            // TODO: Tambahkan data ke halaman home
            // Lalu intent sambil bawa data
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

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
                    etTanggalKadaluarsa.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}