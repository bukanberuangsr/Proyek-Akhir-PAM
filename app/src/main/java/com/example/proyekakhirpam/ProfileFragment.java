package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private ImageView imgProfile;
    private TextView tvDonasiSaya, tvLengkapiProfil, tvRiwayatDonasi;
    private RelativeLayout layoutDetailPembelian;
    private Button btnKeluar;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inisialisasi komponen UI
        imgProfile = view.findViewById(R.id.imgProfile);
        tvDonasiSaya = view.findViewById(R.id.tvDonasiSaya);
        tvLengkapiProfil = view.findViewById(R.id.tvLengkapiProfil);
        layoutDetailPembelian = view.findViewById(R.id.layoutDetailPembelian);
        btnKeluar = view.findViewById(R.id.btnKeluar);

        // INI yang perlu ditambah agar layoutPertemanan bisa digunakan
        RelativeLayout layoutPertemanan = view.findViewById(R.id.layoutPertemanan);

        // Klik teks "Lengkapi profil"
        tvLengkapiProfil.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ProfileActivity.class);
            startActivity(intent);
        });

        // Klik baris Pertemanan
        layoutPertemanan.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PertemananActivity.class);
            startActivity(intent);
        });

        // Klik baris Detail Pembelian
        layoutDetailPembelian.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), HistoryActivity.class);
            startActivity(intent);
        });

        btnKeluar.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }
}