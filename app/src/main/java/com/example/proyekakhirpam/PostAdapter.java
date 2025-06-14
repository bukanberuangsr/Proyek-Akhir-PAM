package com.example.proyekakhirpam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private final Context context;
    private final List<Postingan> postinganList;
    private OnItemClickListener listener;

    public PostAdapter(Context context, List<Postingan> postinganList) {
        this.context = context;
        this.postinganList = postinganList;
    }

    // Interface listener
    public interface OnItemClickListener {
        void onItemClick(Postingan postingan, int position);
    }

    // Setter untuk listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_postingan, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Postingan postingan = postinganList.get(position);

        holder.tvJudul.setText(postingan.getNama());
        holder.tvDetail.setText(postingan.getDeskripsi());

        Glide.with(context)
                .load(postingan.getImageUrl())
                .placeholder(R.drawable.pc_makanan)
                .into(holder.imageView);

        // Tag dibutuhkan agar listener bisa dapat objek postingan
        holder.itemView.setTag(postingan);
    }

    @Override
    public int getItemCount() {
        return postinganList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvDetail;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.judulPost);
            tvDetail = itemView.findViewById(R.id.detailPost);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    Postingan post = (Postingan) v.getTag();
                    listener.onItemClick(post, getAdapterPosition());
                }
            });
        }
    }
}
