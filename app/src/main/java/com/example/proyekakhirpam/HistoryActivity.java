package com.example.proyekakhirpam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.*;

public class HistoryActivity extends AppCompatActivity {
    private ImageView btnBack;
    RecyclerView recyclerView;
    OrderAdapter adapter;
    List<OrderItem> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.rv_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        adapter = new OrderAdapter(this, orderList, (orderItem, position) -> cancelOrder(orderItem, position));
        recyclerView.setAdapter(adapter);

        loadOrders();
    }

    public void loadOrders() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance()
                .collection("orders")
                .whereEqualTo("user_id", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<OrderItem> tempList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        OrderItem item = doc.toObject(OrderItem.class);
                        item.doc_id = doc.getId();
                        item.image_url = doc.getString("gambar_url");
                        item.makanan_id = doc.getString("makanan_id");
                        item.nama_makanan = doc.getString("nama_makanan");
                        item.nama_donor = doc.getString("nama_donor");
                        item.total_harga = doc.getLong("total_harga").intValue();
                        tempList.add(item);
                    }
                    orderList.clear();
                    orderList.addAll(tempList);
                    adapter.notifyDataSetChanged();
                });
    }

    private void cancelOrder(OrderItem orderItem, int position) {
        FirebaseFirestore.getInstance()
                .collection("orders")
                .document(orderItem.doc_id)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    FirebaseFirestore.getInstance()
                            .collection("food-pickup")
                            .document(orderItem.makanan_id)
                            .update("jumlah", FieldValue.increment(orderItem.jumlah))
                            .addOnSuccessListener(aVoid2 -> {
                                Toast.makeText(this, "Pesanan dibatalkan & stok dikembalikan", Toast.LENGTH_SHORT).show();
                                orderList.remove(position);
                                adapter.notifyItemRemoved(position);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Gagal mengembalikan stok: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal hapus pesanan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}