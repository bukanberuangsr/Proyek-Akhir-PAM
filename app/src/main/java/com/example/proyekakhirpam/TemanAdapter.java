package com.example.proyekakhirpam;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.TemanViewHolder> {

    private Context context;
    private List<Teman> temanList;
    private FirebaseFirestore db;
    private String currentUserId;

    private boolean filterDiikuti = false;

    public TemanAdapter(Context context, List<Teman> temanList) {
        this.context = context;
        this.temanList = temanList;
        this.db = FirebaseFirestore.getInstance();
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void setFilterDiikuti(boolean filterDiikuti) {
        this.filterDiikuti = filterDiikuti;
    }

    @NonNull
    @Override
    public TemanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teman, parent, false);
        return new TemanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemanViewHolder holder, int position) {
        Teman teman = temanList.get(position);

        holder.tvNama.setText(teman.getUsername());
        holder.tvEmail.setText(teman.getEmail());
        holder.imgTeman.setImageResource(R.drawable.ic_profile);

        boolean isFollowed = teman.isFollowed();
        holder.btnFollow.setText(isFollowed ? "Batal" : "Ikuti");

        int colorResId = isFollowed ? R.color.red : R.color.orange;
        holder.btnFollow.setBackgroundTintList(ContextCompat.getColorStateList(context, colorResId));

        if (isFollowed) {
            holder.tvDeskripsi.setVisibility(View.VISIBLE);
            String tag = teman.getTag().isEmpty() ? "Teman" : teman.getTag();
            holder.tvDeskripsi.setText(tag);

            holder.tvDeskripsi.setOnClickListener(v -> showEditTagDialog(holder, teman, position));
        } else {
            holder.tvDeskripsi.setVisibility(View.GONE);
        }

        holder.btnFollow.setOnClickListener(v -> {
            boolean isNowFollowed = !teman.isFollowed();
            teman.setFollowed(isNowFollowed);

            if (isNowFollowed) {
                // Follow user
                String initialTag = "Teman";
                teman.setTag(initialTag);
                db.collection("users")
                        .document(currentUserId)
                        .collection("following")
                        .document(teman.getId())
                        .set(new Teman(
                                teman.getId(),
                                teman.getUsername(),
                                teman.getEmail(),
                                initialTag,
                                true
                        ))
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Mengikuti " + teman.getUsername(), Toast.LENGTH_SHORT).show();
                            notifyItemChanged(position);
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(context, "Gagal mengikuti: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            } else {
                // Unfollow user
                db.collection("users")
                        .document(currentUserId)
                        .collection("following")
                        .document(teman.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Berhenti mengikuti " + teman.getUsername(), Toast.LENGTH_SHORT).show();
                            if (filterDiikuti && context instanceof PertemananActivity) {
                                // Jika filter "Diikuti", hapus langsung dari list dan update adapter via activity method
                                PertemananActivity activity = (PertemananActivity) context;
                                activity.removeTeman(position);
                            } else {
                                // Update status follow saja
                                teman.setFollowed(false);
                                notifyItemChanged(position);
                            }
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(context, "Gagal batal mengikuti: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            }
        });
    }

    private void showEditTagDialog(TemanViewHolder holder, Teman teman, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Tag untuk " + teman.getUsername());

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(teman.getTag());
        input.setSelection(input.getText().length());

        builder.setView(input);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String newTag = input.getText().toString().trim();
            if (newTag.isEmpty()) {
                Toast.makeText(context, "Tag tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("users")
                    .document(currentUserId)
                    .collection("following")
                    .document(teman.getId())
                    .update("tag", newTag)
                    .addOnSuccessListener(aVoid -> {
                        teman.setTag(newTag);
                        notifyItemChanged(position);
                        Toast.makeText(context, "Tag diperbarui", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Gagal memperbarui tag: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public int getItemCount() {
        return temanList.size();
    }

    public static class TemanViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvDeskripsi, tvEmail;
        ImageView imgTeman;
        Button btnFollow;

        public TemanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            imgTeman = itemView.findViewById(R.id.imgTeman);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
}
