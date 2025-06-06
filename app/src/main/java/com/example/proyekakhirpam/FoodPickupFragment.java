package com.example.proyekakhirpam;

import android.app.AlertDialog;
import android.os.Bundle;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private boolean isDariHatiSelected = false;
    private boolean isDariKantongSelected = false;

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

        // Inisialisasi view di sini
        rbDariHati = view.findViewById(R.id.rb_dariHati);
        rbDariKantong = view.findViewById(R.id.rb_dariKantong);

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

        LinearLayout cardSteak = view.findViewById(R.id.cardSteak);
        cardSteak.setOnClickListener(v -> showOrderPopup());

        return view;
    }

    private void showOrderPopup() {
        // Inflate layout buat pop-up
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_order, null);

        // Deklarasi elemen form
        EditText et_jumlah = dialogView.findViewById(R.id.et_jumlah);
        Button btn_beli = dialogView.findViewById(R.id.btn_beli);

        // Build AlertDialog di fragment pakai getContext()
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Klik tombol Beli
        btn_beli.setOnClickListener(v -> {
            String jumlah = et_jumlah.getText().toString().trim();
            if (!jumlah.isEmpty()) {
                Toast.makeText(getContext(), "Steak sapi sejumlah " + jumlah + " berhasil dibeli!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Masukkan jumlah terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}