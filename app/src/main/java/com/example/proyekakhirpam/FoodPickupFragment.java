package com.example.proyekakhirpam;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodPickupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodPickupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RadioButton rbDariHati;
    private RadioButton rbDariKantong;
    private View btnAddFood;
    private boolean isDariHatiSelected = false;
    private boolean isDariKantongSelected = false;

    RecyclerView recyclerView;
    FoodAdapter adapter;
    List<FoodItem> dummyList;

    public FoodPickupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodPickupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodPickupFragment newInstance(String param1, String param2) {
        FoodPickupFragment fragment = new FoodPickupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void filterDariHati() {
        // TODO: logika filtering di sini (harga <= 0)
        // Butuh database dulu
    }

    private void filterDariKantong() {
        // TODO: logika filtering di sini (harga > 0)
        // Butuh database dulu
    }

    private void resetFilter() {
        // Reset filter kalau tidak ada tombol yang dipilih
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_pickup, container, false);
        View btn_add_food = view.findViewById(R.id.btn_add_food);

        // Inisialisasi view di sini
        rbDariHati = view.findViewById(R.id.rb_dariHati);
        rbDariKantong = view.findViewById(R.id.rb_dariKantong);

        //Button menambahkan makanan
        btn_add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFoodActivity.class);
                startActivity(intent);
            }
        });

        // Setup listener di sini
        rbDariHati.setOnClickListener(v -> {
            if (isDariHatiSelected) {
                rbDariHati.setChecked(false);
                isDariHatiSelected = false;
                resetFilter();
            } else {
                rbDariHati.setChecked(true);
                isDariHatiSelected = true;
                isDariKantongSelected = false;
                rbDariKantong.setChecked(false);
                filterDariHati();
            }
        });

        rbDariKantong.setOnClickListener(v -> {
            if (isDariKantongSelected) {
                rbDariKantong.setChecked(false);
                isDariKantongSelected = false;
                resetFilter();
            } else {
                rbDariKantong.setChecked(true);
                isDariKantongSelected = true;
                isDariHatiSelected = false;
                rbDariHati.setChecked(false);
                filterDariKantong();
            }
        });

        recyclerView = view.findViewById(R.id.rv_food); // pastikan id ini ada di layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext())); // tanpa 'context:'

        // Dummy data
        dummyList = new ArrayList<>();
        dummyList.add(new FoodItem("Roti", "Rozy Bakery", 3, "2025-04-25", 15000, R.drawable.img_bread));
        dummyList.add(new FoodItem("Roti", "Rozy Bakery", 3, "2025-04-25", 15000, R.drawable.img_bread));
        dummyList.add(new FoodItem("Roti", "Rozy Bakery", 3, "2025-04-25", 15000, R.drawable.img_bread));
        dummyList.add(new FoodItem("Roti", "Rozy Bakery", 3, "2025-04-25", 15000, R.drawable.img_bread));
        dummyList.add(new FoodItem("Roti", "Rozy Bakery", 3, "2025-04-25", 15000, R.drawable.img_bread));
        dummyList.add(new FoodItem("Roti", "Rozy Bakery", 3, "2025-04-25", 15000, R.drawable.img_bread));
        dummyList.add(new FoodItem("Roti", "Rozy Bakery", 3, "2025-04-25", 15000, R.drawable.img_bread));

        adapter = new FoodAdapter(dummyList);
        recyclerView.setAdapter(adapter);

        //Untuk detect row mana yang di klik
        adapter.setOnItemClickListener(item -> {
            showOrderPopup(item); // ganti Toast dengan ini
        });

        return view;
    }

    private void showOrderPopup(FoodItem item) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_order, null);

        // Ambil view dari layout
        TextView tvNamaMakanan = dialogView.findViewById(R.id.tv_nama_makanan);
        TextView tvNamaRestoran = dialogView.findViewById(R.id.tv_nama_restoran);
        EditText et_jumlah = dialogView.findViewById(R.id.et_jumlah);
        Button btn_beli = dialogView.findViewById(R.id.btn_beli);

        // Set isi text sesuai item yang dipilih
        tvNamaMakanan.setText(item.getNamaMakanan());
        tvNamaRestoran.setText(item.getNamaRestoran());

        // Buat dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        btn_beli.setOnClickListener(v -> {
            String jumlahStr = et_jumlah.getText().toString().trim();
            if (!jumlahStr.isEmpty()) {
                // Bisa lanjutkan ke Intent nanti
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Masukkan jumlah terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}