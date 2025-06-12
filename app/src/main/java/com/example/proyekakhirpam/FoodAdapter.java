package com.example.proyekakhirpam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodList;
    private OnItemClickListener listener;

    public FoodAdapter(List<FoodItem> foodList) {
        this.foodList = foodList;
    }

    public interface OnItemClickListener {
        void onItemClick(FoodItem item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem item = foodList.get(position);
        holder.tvNamaMakanan.setText(item.getNama_makanan());
        holder.tvNamaRestoran.setText(item.getDonor_nama());
        holder.tvJumlah.setText(String.valueOf(item.getJumlah()));
        holder.tvHarga.setText("Rp " + item.getHarga());

        // Format tanggal dari Timestamp ke String
        String tanggalStr = "";
        Timestamp tglExpired = item.getTanggal_expired();
        if (tglExpired != null) {
            tanggalStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(tglExpired.toDate());
        }
        holder.tvTanggal.setText("Expired: " + tanggalStr);

        Glide.with(holder.itemView.getContext())
                .load(item.getGambar_url())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .centerCrop()
                .into(holder.ivPlaceholder);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaMakanan, tvNamaRestoran, tvJumlah, tvHarga, tvTanggal;
        ImageView ivPlaceholder;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaMakanan = itemView.findViewById(R.id.tv_nama_makanan);
            tvNamaRestoran = itemView.findViewById(R.id.tv_nama_restoran);
            tvJumlah = itemView.findViewById(R.id.tv_jumlah);
            tvHarga = itemView.findViewById(R.id.tv_harga);
            tvTanggal = itemView.findViewById(R.id.tv_expired);
            ivPlaceholder = itemView.findViewById(R.id.iv_placehoder);
        }
    }
}