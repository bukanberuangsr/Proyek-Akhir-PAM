package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView namaPost, deskripsiPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.detailImage);
        namaPost = findViewById(R.id.detailNama);
        deskripsiPost = findViewById(R.id.detailDeskripsi);

        // Ambil data dari intent
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String deskripsi = intent.getStringExtra("deskripsi");
        int imageRes = intent.getIntExtra("image", R.drawable.pc_makanan); // default
        int smile = intent.getIntExtra("smile", 0);
        int haru = intent.getIntExtra("haru", 0);
        int hug = intent.getIntExtra("hug", 0);

        namaPost.setText(nama);
        deskripsiPost.setText(deskripsi);
        imageView.setImageResource(imageRes);
    }
}

