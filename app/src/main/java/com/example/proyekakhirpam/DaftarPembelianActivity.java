package com.example.proyekakhirpam;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ImageView btnBack;
    private Button btnBatalkan;

    RecyclerView recyclerView;
    OrderAdapter adapter;
    List<OrderItem> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Inisialisasi komponen UI
        btnBack = findViewById(R.id.btnBack);

        // Event klik tombol back
        btnBack.setOnClickListener(v -> finish());

        // Event klik tombol "Batalkan"
        btnBatalkan.setOnClickListener(v -> {
            Toast.makeText(HistoryActivity.this, "Pesanan Baguette berhasil dibatalkan", Toast.LENGTH_SHORT).show();
        });

        recyclerView = findViewById(R.id.rv_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data
        orderList = new ArrayList<>();
        orderList.add(new OrderItem("Roti", "Rozy Bakery", 3, "2025-04-25", 15000, R.drawable.img_bread));
        recyclerView.setAdapter(new OrderAdapter(orderList));


        // Ambil dari Intent, later

    }
}