package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView, backIcon;
    TextView namaPost, deskripsiPost;
    TextView hapusPostingan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.detailImage);
        namaPost = findViewById(R.id.detailNama);
        deskripsiPost = findViewById(R.id.detailDeskripsi);
        backIcon = findViewById(R.id.backIcon);
        hapusPostingan = findViewById(R.id.hapusPostingan);

        // Ambil data dari intent
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String deskripsi = intent.getStringExtra("deskripsi");
        String imageUrl = intent.getStringExtra("image");
        int position = intent.getIntExtra("Position", -1);

        namaPost.setText(nama);
        deskripsiPost.setText(deskripsi);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.pc_makanan) // gambar default sementara loading
                .into(imageView);

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

