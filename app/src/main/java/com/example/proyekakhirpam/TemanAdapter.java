package com.example.proyekakhirpam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.TemanViewHolder> {

    private Context context;
    private List<Teman> temanList;

    public TemanAdapter(Context context, List<Teman> temanList) {
        this.context = context;
        this.temanList = temanList;
    }

    @NonNull
    @Override
    public TemanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_teman, parent, false);
        return new TemanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemanViewHolder holder, int position) {
        Teman teman = temanList.get(position);
        holder.tvNama.setText(teman.getNama());
        holder.tvDeskripsi.setText(teman.getDeskripsi());
        holder.imgTeman.setImageResource(teman.getGambar());

        holder.btnFollow.setOnClickListener(v -> {
            Toast.makeText(context, "Mengikuti " + teman.getNama(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return temanList.size();
    }

    public static class TemanViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvDeskripsi;
        ImageView imgTeman;
        Button btnFollow;

        public TemanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            imgTeman = itemView.findViewById(R.id.imgTeman);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
}
