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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.TemanViewHolder> {

    private Context context;
    private List<Teman> temanList;
    private FirebaseFirestore db;
    private String currentUserId;

    public TemanAdapter(Context context, List<Teman> temanList) {
        this.context = context;
        this.temanList = temanList;
        this.db = FirebaseFirestore.getInstance();
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        holder.tvNama.setText(teman.getUsername());
        holder.tvDeskripsi.setText(teman.getTag().isEmpty() ? teman.getEmail() : teman.getTag());
        holder.imgTeman.setImageResource(R.drawable.ic_profile);

        holder.btnFollow.setText(teman.isFollowed() ? "Batal" : "Ikuti");

        holder.btnFollow.setOnClickListener(v -> {
            boolean isNowFollowed = !teman.isFollowed();
            teman.setFollowed(isNowFollowed);
            holder.btnFollow.setText(isNowFollowed ? "Batal" : "Ikuti");

            if (isNowFollowed) {
                // Tambahkan ke Firestore (subcollection following)
                db.collection("users")
                        .document(currentUserId)
                        .collection("following")
                        .document(teman.getId())
                        .set(new Teman(
                                teman.getId(),
                                teman.getUsername(),
                                teman.getEmail(),
                                "teman",  // default tag
                                true
                        ))
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Mengikuti " + teman.getUsername(), Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Gagal mengikuti: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

            } else {
                // Hapus dari Firestore
                db.collection("users")
                        .document(currentUserId)
                        .collection("following")
                        .document(teman.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Berhenti mengikuti " + teman.getUsername(), Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Gagal batal mengikuti: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
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
