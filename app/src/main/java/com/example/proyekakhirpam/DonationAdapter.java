package com.example.proyekakhirpam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder>{
    private List<LeftoverShareFragment.Donasi> dataList;
    public DonationAdapter(List<LeftoverShareFragment.Donasi> data) {
        this.dataList = data;
    }

    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donation, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
        LeftoverShareFragment.Donasi item = dataList.get(position);
        holder.imageView.setImageResource(item.getGambar());
        holder.judulText.setText(item.getJudul());
        holder.namaDonatur.setText("Nama Donatur: " + item.getNamaDonatur());
        holder.nominalDonasi.setText("Donasi Sebesar " + item.getNominalDonasi());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView judulText, namaDonatur, nominalDonasi;
        CardView cardView;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_view);
            judulText = itemView.findViewById(R.id.judul_donasi);
            namaDonatur = itemView.findViewById(R.id.nama_donatur);
            nominalDonasi = itemView.findViewById(R.id.nominal_donasi);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
