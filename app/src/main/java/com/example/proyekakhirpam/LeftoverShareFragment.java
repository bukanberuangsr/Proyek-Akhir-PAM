package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeftoverShareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeftoverShareFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LeftoverShareFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeftoverShareFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        ImageButton compatButton = view.findViewById(R.id.btn_add_donation);
        compatButton.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), DonationActivity.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycler_donasi_1);

        // Data Dummy
        List<Donasi> data = new ArrayList<>();
        data.add(new Donasi(R.drawable.img_donasi_1, "Palestina: Donasi Makanan untuk Saudara Kita yang Membutuhkan", "Joni", "Rp. 200.000"));
        data.add(new Donasi(R.drawable.img_donasi_1, "Yaman: Donasi Makanan untuk Mengatasi Kelaparan", "Hani", "Rp. 500.000"));

        recyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false
        ));
        recyclerView.setAdapter(new DonasiAdapter(data));
        return view;
    }

    public static class Donasi {
        int gambarId;
        String judul;
        String namaDonatur;
        String nominalDonasi;

        public Donasi(int gambarId, String judul, String namaDonatur, String nominalDonasi) {
            this.gambarId = gambarId;
            this.judul = judul;
            this.namaDonatur = namaDonatur;
            this.nominalDonasi = nominalDonasi;
        }

        public int getGambar() {
            return gambarId;
        }

        public String getJudul() {
            return judul;
        }

        public String getNamaDonatur() {
            return namaDonatur;
        }

        public String getNominalDonasi() {
            return nominalDonasi;
        }

    }
}