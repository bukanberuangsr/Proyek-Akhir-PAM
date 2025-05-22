package com.example.proyekakhirpam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
        holder.tvNamaMakanan.setText(item.getNamaMakanan());
        holder.tvNamaRestoran.setText(item.getNamaRestoran());
        holder.tvJumlah.setText(String.valueOf(item.getJumlah()));
        holder.tvHarga.setText("Rp " + item.getHarga());
        holder.tvTanggal.setText("Expired: " + item.getTanggal());
        holder.ivPlaceholder.setImageResource(item.getImageId());

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
