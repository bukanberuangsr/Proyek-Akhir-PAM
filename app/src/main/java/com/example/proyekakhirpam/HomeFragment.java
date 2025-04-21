package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private List<Post> postList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycle_contact);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        postList = new ArrayList<>();
        postList.add(new Post("Pandu Satria", "Alhamdulillah senang sekali dapat berbagi makanan kepada mereka yang membutuhkan", R.drawable.pc_makanan, 10, 5, 7));
        postList.add(new Post("Icha Amalia", "Terima kasih kepada para donatur yang telah ikut berbagi", R.drawable.pc_makanan, 8, 12, 3));

        adapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(post -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("nama", post.getNama());
            intent.putExtra("deskripsi", post.getDeskripsi());
            intent.putExtra("image", post.getImageRes());
            startActivity(intent);
        });

        return view;
    }
}
