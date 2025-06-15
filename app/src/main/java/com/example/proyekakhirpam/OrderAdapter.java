package com.example.proyekakhirpam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<OrderItem> orderList;
    private Context context;
    private OnCancelClickListener cancelClickListener;

    public interface OnCancelClickListener {
        void onCancelClick(OrderItem item, int position);
    }

    public OrderAdapter(Context context, List<OrderItem> orderList, OnCancelClickListener cancelClickListener) {
        this.context = context;
        this.orderList = orderList;
        this.cancelClickListener = cancelClickListener;
    }

    public void setOrderList(List<OrderItem> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
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
        holder.tvDonor.setText(item.getNamaRestoran());
        holder.tvTotal.setText("Total: Rp" + item.getTotalHarga());

        // Load dari image_url jika ada, jika null gunakan placeholder
        if (item.getImageURL() != null && !item.getImageURL().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImageURL())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(24)))
                    .into(holder.imgPlaceholder);
        } else {
            holder.imgPlaceholder.setImageResource(R.drawable.ic_placeholder);
        }

        holder.btnBatal.setOnClickListener(v -> {
            if (cancelClickListener != null) {
                cancelClickListener.onCancelClick(item, position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPlaceholder;
        TextView tvNama, tvDonor, tvTotal, tvJumlah;
        Button btnBatal;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPlaceholder = itemView.findViewById(R.id.iv_placehoder);
            tvNama = itemView.findViewById(R.id.tv_nama_makanan);
            tvDonor = itemView.findViewById(R.id.tv_nama_donor);
            tvTotal = itemView.findViewById(R.id.tv_total);
            btnBatal = itemView.findViewById(R.id.btn_batal);
        }
    }
}