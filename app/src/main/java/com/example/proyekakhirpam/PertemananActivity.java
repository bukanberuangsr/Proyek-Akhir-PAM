package com.example.proyekakhirpam;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PertemananActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TemanAdapter adapter;
    private List<Teman> temanList;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pertemanan);

        recyclerView = findViewById(R.id.recyclerViewPertemanan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi tombol back
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        temanList = new ArrayList<>();
        temanList.add(new Teman("Leftover Share Official", "lewat grup diskusi makanan", R.drawable.ic_profile));
        temanList.add(new Teman("Pandu Satria", "bergabung lewat grup diskusi makanan", R.drawable.ic_profile));
        temanList.add(new Teman("Akhtar Hafiz", "melalui event donasi bersama", R.drawable.ic_profile));
        temanList.add(new Teman("Farhah Mutiara", "teman dari kuliah", R.drawable.ic_profile));

        adapter = new TemanAdapter(this, temanList);
        recyclerView.setAdapter(adapter);
    }
}
