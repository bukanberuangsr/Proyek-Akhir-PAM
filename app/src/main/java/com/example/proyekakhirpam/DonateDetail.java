package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DonateDetail extends AppCompatActivity {

    ImageView ivGambarDonasi;
    TextView tvJudulDonasi, tvDeskripsiDonasi;
    Button btnDonatePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_donate_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivGambarDonasi = findViewById(R.id.img_main);
        tvJudulDonasi = findViewById(R.id.tv_main);
        tvDeskripsiDonasi = findViewById(R.id.tv_description_content);

        Intent intent = getIntent();
        String judulDonasi = intent.getStringExtra("judul");
        int gambarDonasi = intent.getIntExtra("gambar", 0);
        String deskripsiDonasi = intent.getStringExtra("deskripsi");

        tvJudulDonasi.setText(judulDonasi);
        ivGambarDonasi.setImageResource(gambarDonasi);
        tvDeskripsiDonasi.setText(deskripsiDonasi);

        btnDonatePayment = findViewById(R.id.btn_donate_payment);
        btnDonatePayment.setOnClickListener(v -> {
            Intent paymentIntent = new Intent(this, DonatePayment.class);
            paymentIntent.putExtra("judul", judulDonasi);
            paymentIntent.putExtra("gambar", gambarDonasi);
            startActivity(paymentIntent);
        });
    }
}