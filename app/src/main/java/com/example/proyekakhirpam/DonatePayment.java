package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DonatePayment extends AppCompatActivity {

    ImageView ivGambarDonasi;
    TextView tvJudulDonasi;
    Button btnBayar, btn10, btn25, btn50, btn100, btn150;
    EditText etNominal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_donate_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivGambarDonasi = findViewById(R.id.img_main);
        tvJudulDonasi = findViewById(R.id.tv_main);

        Intent intent = getIntent();
        String judulDonasi = intent.getStringExtra("judul");
        int gambarDonasi = intent.getIntExtra("gambar", 0);

        tvJudulDonasi.setText(judulDonasi);
        ivGambarDonasi.setImageResource(gambarDonasi);

        etNominal = findViewById(R.id.et_nominal);
        btn10 = findViewById(R.id.btn_donate_10);
        btn10.setOnClickListener(v->etNominal.setText("10000"));
        btn25 = findViewById(R.id.btn_donate_25);
        btn25.setOnClickListener(v->etNominal.setText("25000"));
        btn50 = findViewById(R.id.btn_donate_50);
        btn50.setOnClickListener(v->etNominal.setText("50000"));
        btn100 = findViewById(R.id.btn_donate_100);
        btn100.setOnClickListener(v->etNominal.setText("100000"));
        btn150 = findViewById(R.id.btn_donate_150);
        btn150.setOnClickListener(v->etNominal.setText("150000"));

        btnBayar = findViewById(R.id.btn_confirm_payment);
        btnBayar.setOnClickListener(v -> {
            // Tambahin data dari tombol/editText ke intent
            // Data nominal ditambahkan ke card RecyclerView di LeftoverShareFragment
            String nominal = etNominal.getText().toString();

            Intent backIntent = new Intent(DonatePayment.this, MainActivity.class);
            backIntent.putExtra("judul", judulDonasi);
            backIntent.putExtra("gambar", gambarDonasi);
            backIntent.putExtra("nominal", nominal);
            startActivity(backIntent);
            finish();
        });
    }
}