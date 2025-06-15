package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeftoverShareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeftoverShareFragment extends Fragment {

    // Tidak Dipakai
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Dipakai
    ImageButton imageButton;
    FirebaseFirestore db;
    RecyclerView rvInternasional, rvNasional;
    List<DonationItem> listInternasional;
    List<DonationItem> listNasional;
    DonationAdapter adapterInternasional;
    DonationAdapter adapterNasional;
//    List<DonationItem> donationList;

    public LeftoverShareFragment() {
        // Required empty public constructor
    }

    public static LeftoverShareFragment newInstance(String param1, String param2) {
        LeftoverShareFragment fragment = new LeftoverShareFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leftover_share, container, false);

        imageButton = view.findViewById(R.id.btn_add_donation);
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddDonationActivity.class);
            startActivity(intent);
        });

        rvInternasional = view.findViewById(R.id.recycler_donasi_1);
        rvNasional = view.findViewById(R.id.recycler_donasi_2);

        // Data dari Firestore
        db = FirebaseFirestore.getInstance();
        listInternasional = new ArrayList<>();
        listNasional = new ArrayList<>();

        adapterInternasional = new DonationAdapter(getContext(), listInternasional);
        adapterNasional = new DonationAdapter(getContext(), listNasional);
        rvInternasional.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false
        ));
        rvNasional.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false
        ));
        rvInternasional.setAdapter(adapterInternasional);
        rvNasional.setAdapter(adapterNasional);

        // Load donations from Firestore
        loadDonations();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDonations(); // Refresh data when returning to this fragment
    }

    private void loadDonations() {
        // Fetch Internasional donations
        db.collection("donation")
                .whereEqualTo("tipe_donasi", "Internasional")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listInternasional.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String id = doc.getId();
                        String gambarUrl = doc.getString("gambar_url");
                        String judul = doc.getString("judul_donasi");
                        String deskripsi = doc.getString("deskripsi_donasi");
                        String namaDonatur = doc.getString("nama_donatur");
                        String nominalDonasi = String.valueOf(doc.get("nominal_donasi"));
                        com.google.firebase.Timestamp ts = doc.getTimestamp("tanggal_selesai");
                        java.util.Date tanggalSelesai = ts != null ? ts.toDate() : new java.util.Date();

                        DonationItem item = new DonationItem(
                                id,
                                gambarUrl,
                                judul,
                                deskripsi,
                                namaDonatur,
                                nominalDonasi,
                                tanggalSelesai
                        );
                        listInternasional.add(item);
                    }
                    adapterInternasional.notifyDataSetChanged();
                });

        // Fetch Nasional donations
        db.collection("donation")
                .whereEqualTo("tipe_donasi", "Nasional")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listNasional.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String id = doc.getId();
                        String gambarUrl = doc.getString("gambar_url");
                        String judul = doc.getString("judul_donasi");
                        String deskripsi = doc.getString("deskripsi_donasi");
                        String namaDonatur = doc.getString("nama_donatur");
                        String nominalDonasi = String.valueOf(doc.get("nominal_donasi"));
                        com.google.firebase.Timestamp ts = doc.getTimestamp("tanggal_selesai");
                        java.util.Date tanggalSelesai = ts != null ? ts.toDate() : new java.util.Date();

                        DonationItem item = new DonationItem(
                                id,
                                gambarUrl,
                                judul,
                                deskripsi,
                                namaDonatur,
                                nominalDonasi,
                                tanggalSelesai
                        );
                        listNasional.add(item);
                    }
                    adapterNasional.notifyDataSetChanged();
                });
    }
}