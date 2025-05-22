package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView, backIcon;
    TextView namaPost, deskripsiPost;
    TextView react1Count, react2Count, react3Count, hapusPostingan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.detailImage);
        namaPost = findViewById(R.id.detailNama);
        deskripsiPost = findViewById(R.id.detailDeskripsi);
        react1Count = findViewById(R.id.react1Count);
        react2Count = findViewById(R.id.react2Count);
        react3Count = findViewById(R.id.react3Count);
        backIcon = findViewById(R.id.backIcon);
        hapusPostingan = findViewById(R.id.hapusPostingan);

        // Ambil data dari intent
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String deskripsi = intent.getStringExtra("deskripsi");
        int imageRes = intent.getIntExtra("image", R.drawable.pc_makanan); // default
        int smile = intent.getIntExtra("smile", 0);
        int haru = intent.getIntExtra("haru", 0);
        int hug = intent.getIntExtra("hug", 0);
        int position = intent.getIntExtra("Position", -1);

        namaPost.setText(nama);
        deskripsiPost.setText(deskripsi);
        imageView.setImageResource(imageRes);
        react1Count.setText(String.valueOf(smile));  // Tampilkan jumlah react
        react2Count.setText(String.valueOf(haru));   // Tampilkan jumlah react
        react3Count.setText(String.valueOf(hug));    // Tampilkan jumlah react

        // tombol kembali
        backIcon.setOnClickListener(v -> finish());

        // klik hapus
        hapusPostingan.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("Position", position);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}

