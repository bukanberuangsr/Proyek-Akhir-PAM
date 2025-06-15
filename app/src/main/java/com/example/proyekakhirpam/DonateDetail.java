package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DonateDetail extends AppCompatActivity {

    ImageView ivGambarDonasi;
    TextView tvJudulDonasi, tvDeskripsiDonasi;
    Button btnDonatePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_detail);

        ivGambarDonasi = findViewById(R.id.img_main);
        tvJudulDonasi = findViewById(R.id.tv_main);
        tvDeskripsiDonasi = findViewById(R.id.tv_description_content);

        Intent intent = getIntent();
        String judulDonasi = intent.getStringExtra("judul");
        String gambarDonasi = intent.getStringExtra("gambar");
        String deskripsiDonasi = intent.getStringExtra("deskripsi");

        tvJudulDonasi.setText(judulDonasi);
        tvDeskripsiDonasi.setText(deskripsiDonasi);
        Glide.with(this)
                .load(gambarDonasi)
                .into(ivGambarDonasi);

        btnDonatePayment = findViewById(R.id.btn_donate_payment);
        btnDonatePayment.setOnClickListener(v -> {
            Intent paymentIntent = new Intent(this, DonatePayment.class);
            paymentIntent.putExtra("judul", judulDonasi);
            paymentIntent.putExtra("gambar", gambarDonasi);
            startActivity(paymentIntent);
        });
    }
}