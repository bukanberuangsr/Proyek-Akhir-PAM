package com.example.proyekakhirpam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> postList;

    private OnItemClickListener listener;

    // 2. Interface untuk callback click
    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    // 3. Setter untuk listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Constructor
    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.nama.setText(post.getNama());
        holder.deskripsi.setText(post.getDeskripsi());
        holder.imageView.setImageResource(post.getImageRes());
        holder.react1Count.setText(String.valueOf(post.getReactSmile()));
        holder.react2Count.setText(String.valueOf(post.getReactHaru()));
        holder.react3Count.setText(String.valueOf(post.getReactHug()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(post); // klik listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    // ViewHolder
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView nama, deskripsi, react1Count, react2Count, react3Count;
        ImageView imageView;
        CardView card;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.judulPost);
            deskripsi = itemView.findViewById(R.id.detailPost);
            imageView = itemView.findViewById(R.id.imageView);
            react1Count = itemView.findViewById(R.id.react1Count);
            react2Count = itemView.findViewById(R.id.react2Count);
            react3Count = itemView.findViewById(R.id.react3Count);
        }
    }
}
