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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private ImageView imgProfile;
    private TextView tvDonasiSaya, tvLengkapiProfil, tvRiwayatDonasi, tvNamaPengguna;
    private ImageView imgProfil;
    private RelativeLayout layoutDetailPembelian;
    private Button btnKeluar;
    private ProgressBar progressProfil;
    private TextView tvPersentaseProfil;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inisialisasi UI
        imgProfile = view.findViewById(R.id.imgProfile);
        tvLengkapiProfil = view.findViewById(R.id.tvLengkapiProfil);
        layoutDetailPembelian = view.findViewById(R.id.layoutDetailPembelian);
        btnKeluar = view.findViewById(R.id.btnKeluar);
        tvNamaPengguna = view.findViewById(R.id.tvNamaPengguna);
        RelativeLayout layoutPertemanan = view.findViewById(R.id.layoutPertemanan);
        progressProfil = view.findViewById(R.id.progressProfil);
        tvPersentaseProfil = view.findViewById(R.id.tvPersentaseProfil);
        imgProfil = view.findViewById(R.id.imgProfile);

        // Tampilkan data dan progress
        fetchAndDisplayDataUser();
        updateProgressBar();

        // Navigasi
        tvLengkapiProfil.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(requireContext(), InfoProfileActivity.class);
                intent.putExtra("uid", user.getUid());
                startActivity(intent);
            }
        });

        layoutPertemanan.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), PertemananActivity.class)));

        layoutDetailPembelian.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), HistoryActivity.class)));

        btnKeluar.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences prefs = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
            prefs.edit().clear().apply();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }

    private void fetchAndDisplayDataUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        SharedPreferences prefs = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();

                        if (documentSnapshot.exists()) {
                            String nama = documentSnapshot.getString("username");
                            String photoUrl = documentSnapshot.getString("photo_url");
                            String tanggalLahir = documentSnapshot.getString("tanggalLahir");
                            String bio = documentSnapshot.getString("bio");

                            tvNamaPengguna.setText(nama != null && !nama.isEmpty() ? nama : "Pengguna");

                            editor.putString("nama", nama != null ? nama : "");
                            editor.putString("photo_url", photoUrl != null ? photoUrl : "");
                            editor.putString("tanggalLahir", tanggalLahir != null ? tanggalLahir : "");
                            editor.putString("bio", bio != null ? bio : "");
                            editor.apply();

                            if (photoUrl != null && !photoUrl.isEmpty()) {
                                Glide.with(this).load(photoUrl).into(imgProfile);
                            } else {
                                imgProfile.setImageResource(R.drawable.ic_profile); // fallback drawable
                            }
                        } else {
                            tvNamaPengguna.setText(user.getEmail() != null ? user.getEmail() : "Pengguna");
                            editor.clear().apply();
                            imgProfile.setImageResource(R.drawable.ic_profile);
                        }
                        updateProgressBar();
                    })
                    .addOnFailureListener(e -> {
                        tvNamaPengguna.setText("Pengguna");
                        imgProfile.setImageResource(R.drawable.ic_profile);
                    });
        } else {
            tvNamaPengguna.setText("Pengguna");
            imgProfile.setImageResource(R.drawable.ic_profile);
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
        fetchAndDisplayDataUser();
    }
}