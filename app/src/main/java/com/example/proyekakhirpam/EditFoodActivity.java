package com.example.proyekakhirpam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditFoodActivity extends AppCompatActivity {

    private EditText etNamaMakanan, etNamaDonor, etJumlah, etTanggal, etHarga;
    private FrameLayout uploadPlaceholder;
    private ImageView ivPreview;
    private Button btnUpdate, btnDelete;
    private String foodItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        etNamaMakanan = findViewById(R.id.et_nama_makanan);
        etNamaDonor = findViewById(R.id.et_nama_restoran);
        etJumlah = findViewById(R.id.et_jumlah);
        etTanggal = findViewById(R.id.et_tanggal);
        etHarga = findViewById(R.id.et_harga);
        uploadPlaceholder = findViewById(R.id.upload_placeholder);
        ivPreview = findViewById(R.id.iv_preview);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);

        foodItemId = getIntent().getStringExtra("foodItemId");
        fetchFoodItem(foodItemId);

        btnUpdate.setOnClickListener(v -> updateFood());
        btnDelete.setOnClickListener(v -> deleteFood());
    }

    private void fetchFoodItem(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("food-pickup").document(id).get().addOnSuccessListener(document -> {
            if (document.exists()) {
                etNamaMakanan.setText(document.getString("nama_makanan"));
                etNamaDonor.setText(document.getString("donor_nama"));
                Long jumlah = document.getLong("jumlah");
                etJumlah.setText(jumlah != null ? String.valueOf(jumlah) : "");
                Long harga = document.getLong("harga");
                etHarga.setText(harga != null ? String.valueOf(harga) : "");

                Timestamp tglExpired = document.getTimestamp("tanggal_expired");
                if (tglExpired != null) {
                    String tanggalStr = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(tglExpired.toDate());
                    etTanggal.setText(tanggalStr);
                }
                etTanggal.setEnabled(false);

                String gambarUrl = document.getString("gambar_url");
                if (gambarUrl != null && !gambarUrl.isEmpty()) {
                    ivPreview.setVisibility(ImageView.VISIBLE);
                    Glide.with(this).load(gambarUrl).into(ivPreview);
                }
            }
        });
    }

    public void updateFood() {
        String namaMakanan = etNamaMakanan.getText().toString().trim();
        String namaDonor = etNamaDonor.getText().toString().trim();
        String jumlahStr = etJumlah.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("nama_makanan", namaMakanan);
        updatedData.put("donor_nama", namaDonor);
        updatedData.put("jumlah", Integer.parseInt(jumlahStr));
        updatedData.put("harga", Integer.parseInt(hargaStr));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("food-pickup").document(foodItemId)
                .update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Data makanan berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal memperbarui data makanan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void deleteFood() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("food-pickup").document(foodItemId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Data makanan berhasil dihapus", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal menghapus data makanan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}