package com.example.proyekakhirpam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProfileFragment extends Fragment {

    private ImageView imgProfile;
    private TextView tvDonasiSaya, tvLengkapiProfil, tvRiwayatDonasi, tvNamaPengguna;
    private RelativeLayout layoutDetailPembelian;
    private Button btnKeluar;
    private ProgressBar progressProfil;
    private TextView tvPersentaseProfil;

    public ProfileFragment() {}

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inisialisasi UI
        imgProfile = view.findViewById(R.id.imgProfile);
        tvDonasiSaya = view.findViewById(R.id.tvDonasiSaya);
        tvLengkapiProfil = view.findViewById(R.id.tvLengkapiProfil);
        layoutDetailPembelian = view.findViewById(R.id.layoutDetailPembelian);
        btnKeluar = view.findViewById(R.id.btnKeluar);
        tvNamaPengguna = view.findViewById(R.id.tvNamaPengguna);
        RelativeLayout layoutPertemanan = view.findViewById(R.id.layoutPertemanan);
        progressProfil = view.findViewById(R.id.progressProfil);
        tvPersentaseProfil = view.findViewById(R.id.tvPersentaseProfil);

        // Tampilkan data dan progress
        tampilkanDataUser();
        updateProgressBar();

        // Navigasi
        tvLengkapiProfil.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), InfoProfileActivity.class)));

        layoutPertemanan.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), PertemananActivity.class)));

        layoutDetailPembelian.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), HistoryActivity.class)));

        btnKeluar.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    private void tampilkanDataUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nama = documentSnapshot.getString("username");
                            tvNamaPengguna.setText(nama != null && !nama.isEmpty() ? nama : "Pengguna");

                            SharedPreferences prefs = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("nama", nama != null ? nama : "");
                            editor.apply();
                        } else {
                            // fallback
                            tvNamaPengguna.setText(user.getEmail() != null ? user.getEmail() : "Pengguna");
                        }
                    })
                    .addOnFailureListener(e -> {
                        tvNamaPengguna.setText("Pengguna");
                    });
        } else {
            tvNamaPengguna.setText("Pengguna");
        }
    }


    private void updateProgressBar() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

        int count = 0;
        if (!prefs.getString("nama", "").isEmpty()) count++;
        if (!prefs.getString("tanggalLahir", "").isEmpty()) count++;
        if (!prefs.getString("bio", "").isEmpty()) count++;

        int progress = (int) ((count / 3.0) * 100);
        progressProfil.setProgress(progress);
        tvPersentaseProfil.setText(progress + "%");
    }

    @Override
    public void onResume() {
        super.onResume();
        tampilkanDataUser();
        updateProgressBar();
    }
}
