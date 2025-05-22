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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.widget.RadioButton;

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
    private RadioButton rbDariHati;
    private RadioButton rbDariKantong;
    private View btnAddFood;
    private boolean isDariHatiSelected = false;
    private boolean isDariKantongSelected = false;


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

        ImageButton imageButton = view.findViewById(R.id.btn_add_donation);
        imageButton.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), DonationActivity.class);
            startActivity(intent);
        });

        RecyclerView recyclerView1 = view.findViewById(R.id.recycler_donasi_1);
        RecyclerView recyclerView2 = view.findViewById(R.id.recycler_donasi_2);

        // Data Dummy
        List<Donasi> dataRC1 = new ArrayList<>();
        dataRC1.add(new Donasi(
                R.drawable.img_donasi_1,
                "Palestina: Donasi Makanan untuk Saudara Kita",
                "Satria",
                "200000",
                "Di tengah konflik dan krisis kemanusiaan yang terus berlangsung, ribuan keluarga di Palestina berjuang untuk mendapatkan makanan setiap hari. Akses terhadap kebutuhan dasar semakin terbatas, dan mereka sangat membutuhkan uluran tangan kita.\n" +
                "\n" +
                "Blokade dan keterbatasan distribusi bahan pangan membuat harga makanan melambung tinggi, sementara persediaan semakin menipis. Banyak keluarga hanya mampu makan satu kali sehari atau bahkan terpaksa berpuasa karena tidak ada makanan yang tersisa. Situasi ini semakin diperparah dengan rusaknya infrastruktur dan sulitnya akses bantuan kemanusiaan."
        ));
        dataRC1.add(new Donasi(
                R.drawable.img_donasi_2,
                "Yaman: Donasi Makanan untuk Mengatasi Kelaparan",
                "Farhah",
                "500000",
                "Lorem ipsum dolor sit amet."
        ));

        List<Donasi> dataRC2 = new ArrayList<>();
        dataRC2.add(new Donasi(
                R.drawable.img_donasi_3,
                "Papua: Donasi Makanan untuk Selamatkan Nyawa",
                "Kinky",
                "10000",
                "Lorem ipsum dolor sit amet."
        ));

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            String judul = bundle.getString("judul");
            String gambar = bundle.getString("gambar");
            String nominalBaru = bundle.getString("nominal");

            if (judul!=null && nominalBaru!=null){
                boolean found = false;
                for (Donasi d : dataRC1){
                    if (d.getJudul().equals(judul)){
                        int total = Integer.parseInt(d.getNominalDonasi()) + Integer.parseInt(nominalBaru);
                        d.nominalDonasi = String.valueOf(total);
                        found = true;
                        break;
                    }
                }
                if (!found){
                    Toast.makeText(getContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
        }

        DonationAdapter donationAdapter = new DonationAdapter(getContext(), dataRC1);
        DonationAdapter donationAdapter2 = new DonationAdapter(getContext(), dataRC2);
        recyclerView1.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false
        ));
        recyclerView2.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false
        ));
        recyclerView1.setAdapter(donationAdapter);
        recyclerView2.setAdapter(donationAdapter2);
        donationAdapter.notifyDataSetChanged();
        return view;
    }

    public static class Donasi {
        int gambarId;
        String judul;
        String namaDonatur;
        String nominalDonasi;
        String deskripsiDonasi;

        public Donasi(int gambarId, String judul, String namaDonatur, String nominalDonasi, String deskripsiDonasi) {
            this.gambarId = gambarId;
            this.judul = judul;
            this.namaDonatur = namaDonatur;
            this.nominalDonasi = nominalDonasi;
            this.deskripsiDonasi = deskripsiDonasi;
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

        public String getDeskripsiDonasi() {
            return deskripsiDonasi;
        }

    }
}