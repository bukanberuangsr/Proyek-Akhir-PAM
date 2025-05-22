package com.example.proyekakhirpam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyekakhirpam.OrderItem;
import com.example.proyekakhirpam.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<OrderItem> orderList;

    public OrderAdapter(List<OrderItem> orderList) {
        this.orderList = orderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderItem item = orderList.get(position);
        holder.tvNama.setText(item.getNamaMakanan());
        holder.tvRestoran.setText(item.getNamaRestoran());
        holder.tvExpired.setText("Expired: " + item.getTanggal());
//        holder.tvJumlah.setText("Jumlah: " + item.getJumlah());
        holder.tvTotal.setText("Total: Rp" + item.getTotalHarga());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvRestoran, tvExpired, tvJumlah, tvTotal;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama_makanan);
            tvRestoran = itemView.findViewById(R.id.tv_nama_restoran);
            tvExpired = itemView.findViewById(R.id.tv_expired);
//            tvJumlah = itemView.findViewById(R.id.tv_jumlah);
            tvTotal = itemView.findViewById(R.id.tv_total);
        }
    }
}
