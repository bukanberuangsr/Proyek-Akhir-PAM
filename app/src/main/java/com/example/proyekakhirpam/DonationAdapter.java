package com.example.proyekakhirpam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder>{
    private List<DonationItem> dataList;
    private Context context;
    public DonationAdapter(Context context, List<DonationItem> data) {
        this.context = context;
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
        DonationItem item = dataList.get(position);
//        holder.imageView.setImageResource(item.getGambar_url());
        Glide.with(context).load(item.getGambar_url()).into(holder.imageView);
        holder.judulText.setText(item.getJudul());
        holder.namaDonatur.setText("Nama Donatur: " + item.getNamaDonatur());
        holder.nominalDonasi.setText("Donasi Sebesar Rp." + item.getNominalDonasi());

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DonateDetailActivity.class);
            intent.putExtra("donationId", item.getId());
            intent.putExtra("judul", item.getJudul());
            intent.putExtra("gambar", item.getGambar_url());
            intent.putExtra("deskripsi", item.getDeskripsiDonasi());
            intent.putExtra("tanggal_selesai", item.getTanggalSelesai().toString());
            intent.putExtra("nominal", item.getNominalDonasi());
            intent.putExtra("jenis_donasi", item.getTipeDonasi());
            context.startActivity(intent);
        });
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
