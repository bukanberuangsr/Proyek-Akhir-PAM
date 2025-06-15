package com.example.proyekakhirpam;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

public class FoodPickupFragment extends Fragment {
    private RadioButton rbDaftarMakanan;
    private RadioButton rbDariSaya;
    private View btnAddFood;

    FirebaseFirestore db;
    FirebaseUser currentUser;
    RecyclerView recyclerView;
    FoodAdapter adapter;
    List<FoodItem> foodList = new ArrayList<>();

    public FoodPickupFragment() {

    }

    public static FoodPickupFragment newInstance(String param1, String param2) {
        FoodPickupFragment fragment = new FoodPickupFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_pickup, container, false);
        btnAddFood = view.findViewById(R.id.btn_add_food);
        rbDaftarMakanan = view.findViewById(R.id.rb_daftarMakanan);
        rbDariSaya = view.findViewById(R.id.rb_dariSaya);
        recyclerView = view.findViewById(R.id.rv_food);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(adapter);

        btnAddFood.setOnClickListener( v -> {
            Intent intent = new Intent(getActivity(), AddFoodActivity.class);
            startActivity(intent);
        });

        // Default: tampilkan semua makanan
        loadAllFoods();

        rbDaftarMakanan.setOnClickListener(v -> loadAllFoods());
        rbDariSaya.setOnClickListener(v -> loadMyDonations());

        adapter.setOnItemClickListener(item -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            boolean isDaftarMakananMode = rbDaftarMakanan.isChecked(); // atau simpan flag mode

            boolean isMyDonation = currentUser != null
                    && item.getDonor_id() != null
                    && item.getDonor_id().equals(currentUser.getUid());

            if (isDaftarMakananMode) {
                // Tab Daftar Makanan
                if (isMyDonation) {
                    Toast.makeText(getContext(), "Aksi tidak valid. Untuk mengedit makanan, buka tab Donasi Saya.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Bukan makanan sendiri, tampilkan order popup
                showOrderPopup(item);
            } else {
                // Tab Donasi Saya
                if (isMyDonation) {
                    // Boleh edit
                    Intent intent = new Intent(getActivity(), EditFoodActivity.class);
                    intent.putExtra("foodItemId", item.getId());
                    startActivity(intent);
                } else {
                    // Kalau bukan donasi sendiri di tab ini, bisa abaikan atau beri toast
                    Toast.makeText(getContext(), "Hanya bisa mengedit donasi milik Anda.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void loadAllFoods() {
        rbDaftarMakanan.setChecked(true);
        rbDariSaya.setChecked(false);

        db.collection("food-pickup")
                .whereEqualTo("isAvailable", true)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getContext(), "Data gagal dimuat: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    foodList.clear();
                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            Log.d("FirestoreTest", doc.getData().toString());
                            FoodItem item = doc.toObject(FoodItem.class);
                            item.setId(doc.getId());
                            foodList.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public void loadMyDonations(){
        rbDaftarMakanan.setChecked(false);
        rbDariSaya.setChecked(true);

        if(currentUser == null) {
            Toast.makeText(getContext(), "Harus login terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("food-pickup")
                .whereEqualTo("donor_id", currentUser.getUid())
                .whereEqualTo("isAvailable", true)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getContext(), "Data gagal dimuat: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    foodList.clear();
                    if(value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            FoodItem item = doc.toObject(FoodItem.class);
                            item.setId(doc.getId());
                            foodList.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void showOrderPopup(FoodItem item) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_order, null);

        TextView tvNamaMakanan = dialogView.findViewById(R.id.tv_nama_makanan);
        TextView tvNamaDonor = dialogView.findViewById(R.id.tv_nama_donor);
        EditText et_jumlah = dialogView.findViewById(R.id.et_jumlah);
        Button btn_beli = dialogView.findViewById(R.id.btn_beli);
        TextView tvErrorStok = dialogView.findViewById(R.id.tv_error_stok);

        tvNamaMakanan.setText(item.getNama_makanan());
        tvNamaDonor.setText(item.getDonor_nama());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        btn_beli.setOnClickListener(v -> {
            String jumlahStr = et_jumlah.getText().toString().trim();
            if (!jumlahStr.isEmpty()) {
                int jumlah;
                try {
                    jumlah = Integer.parseInt(jumlahStr);
                } catch (NumberFormatException e) {
                    tvErrorStok.setText("Jumlah tidak valid");
                    tvErrorStok.setVisibility(View.VISIBLE);
                    return;
                }

                if (jumlah <= 0) {
                    tvErrorStok.setText("Jumlah harus lebih dari 0");
                    tvErrorStok.setVisibility(View.VISIBLE);
                    return;
                }

                if (jumlah > item.getJumlah()) {
                    tvErrorStok.setText("Melebihi jumlah ketersediaan. Maksimal: " + item.getJumlah());
                    tvErrorStok.setVisibility(View.VISIBLE);
                    return;
                }

                tvErrorStok.setVisibility(View.GONE);
                prosesOrder(item, jumlah, dialog);
            } else {
                tvErrorStok.setText("Masukkan jumlah yang ingin dipesan");
                tvErrorStok.setVisibility(View.VISIBLE);
            }
        });

        dialog.show();
    }

    private void prosesOrder(FoodItem item, int jumlah, AlertDialog dialog) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        int totalHarga = jumlah * item.getHarga();
        Map<String, Object> dataOrder = new HashMap<>();
        dataOrder.put("user_id", user.getUid());
        dataOrder.put("gambar_url", item.getGambar_url());
        dataOrder.put("makanan_id", item.getId());
        dataOrder.put("nama_makanan", item.getNama_makanan());
        dataOrder.put("nama_donor", item.getDonor_nama());
        dataOrder.put("jumlah", jumlah);
        dataOrder.put("total_harga", totalHarga);

        FirebaseFirestore.getInstance()
                .collection("orders")
                .add(dataOrder)
                .addOnSuccessListener(docRef -> {
                    // Update stok makanan
                    int updateJumlah = item.getJumlah() - jumlah;
                    FirebaseFirestore.getInstance()
                            .collection("food-pickup")
                            .document(item.getId())
                            .update("jumlah", updateJumlah)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Order berhasil & stok terupdate!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Order berhasil, update stok gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            });
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Order gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}