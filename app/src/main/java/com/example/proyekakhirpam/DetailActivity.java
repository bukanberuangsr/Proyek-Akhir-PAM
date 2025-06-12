package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView, backIcon;
    TextView judulPost, deskripsiPost;
    TextView hapusPostingan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.detailImage);
        judulPost = findViewById(R.id.judulPostingan);
        deskripsiPost = findViewById(R.id.deskripsiPostingan);
        backIcon = findViewById(R.id.backIcon);
        hapusPostingan = findViewById(R.id.hapusPostingan);

        // Ambil data dari intent
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String deskripsi = intent.getStringExtra("deskripsi");
        String imageUrl = intent.getStringExtra("image");
        String postinganId = intent.getStringExtra("postinganId");
        int position = intent.getIntExtra("Position", -1);

        judulPost.setText(nama);
        deskripsiPost.setText(deskripsi);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.pc_makanan) // gambar default sementara loading
                .into(imageView);

        // tombol kembali
        backIcon.setOnClickListener(v -> finish());

        // klik hapus
        hapusPostingan.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus postingan ini?")
                    .setPositiveButton("Hapus", (dialog, which) -> {
                        // Proses penghapusan Firestore
                        if (postinganId != null && !postinganId.isEmpty()) {
                            FirebaseFirestore.getInstance().collection("postingan")
                                    .document(postinganId)
                                    .delete()
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, "Postingan berhasil dihapus", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Gagal menghapus postingan", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }
}

