package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private List<Postingan> postList;
    private FirebaseFirestore db;

    private final ActivityResultLauncher<Intent> launcherFormPostingan =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                    loadDataFromFirestore(); // Reload data setelah posting
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton btnTambah = view.findViewById(R.id.btnTambah);
        btnTambah.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FormPostinganActivity.class);
            launcherFormPostingan.launch(intent);
        });

        recyclerView = view.findViewById(R.id.recycle_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        postList = new ArrayList<>();
        adapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadDataFromFirestore();

        adapter.setOnItemClickListener((post, position) -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("nama", post.getNama());
            intent.putExtra("deskripsi", post.getDeskripsi());
            intent.putExtra("image", post.getImageUrl());
            intent.putExtra("postinganId", post.getPostinganId());
            startActivityForResult(intent, 1001);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == getActivity().RESULT_OK) {
            loadDataFromFirestore(); // Refresh daftar postingan
        }
    }

    private void loadDataFromFirestore() {
        db.collection("postingan")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postList.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Postingan post = doc.toObject(Postingan.class);
                            if (post != null) {
                                post.setPostinganId(doc.getId());
                                postList.add(post);
                                Log.d("PostCheck", "Nama: " + post.getNama());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("PostCheck", "Tidak ada data.");
                    }
                })
                .addOnFailureListener(e -> Log.e("PostCheck", "Gagal", e));
    }
}
