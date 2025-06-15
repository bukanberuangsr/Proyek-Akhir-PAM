package com.example.proyekakhirpam;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PertemananActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TemanAdapter adapter;
    List<Teman> temanList = new ArrayList<>();
    List<Teman> semuaTemanList = new ArrayList<>();

    ImageView btnBack;
    RadioGroup radioFilter;

    FirebaseFirestore db;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pertemanan);

        recyclerView = findViewById(R.id.recyclerViewPertemanan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TemanAdapter(this, temanList);
        recyclerView.setAdapter(adapter);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        radioFilter = findViewById(R.id.radioFilter);
        radioFilter.setOnCheckedChangeListener((group, checkedId) -> applyFilter());

        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadTemanData();
    }

    private void loadTemanData() {
        db.collection("users").get().addOnSuccessListener(querySnapshot -> {
            semuaTemanList.clear();

            int[] totalUser = {0};
            int[] loadedCount = {0};

            for (QueryDocumentSnapshot doc : querySnapshot) {
                String uid = doc.getId();
                if (!uid.equals(currentUserId)) {
                    totalUser[0]++;

                    String username = doc.getString("username");
                    String email = doc.getString("email");

                    db.collection("users")
                            .document(currentUserId)
                            .collection("following")
                            .document(uid)
                            .get()
                            .addOnSuccessListener(followDoc -> {
                                boolean isFollowed = followDoc.exists();
                                String tag = followDoc.contains("tag") ? followDoc.getString("tag") : "";

                                Teman teman = new Teman(uid, username, email, tag, isFollowed);
                                semuaTemanList.add(teman);

                                loadedCount[0]++;
                                if (loadedCount[0] == totalUser[0]) {
                                    applyFilter(); // Jalankan SEKALI saat SEMUA teman selesai dimuat
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firestore", "Gagal ambil data follow: " + e.getMessage());
                                loadedCount[0]++;
                                if (loadedCount[0] == totalUser[0]) {
                                    applyFilter(); // Tetap jalankan walau ada error
                                }
                            });
                }
            }

            if (totalUser[0] == 0) {
                applyFilter(); // Kalau tidak ada user lain selain currentUser
            }

        }).addOnFailureListener(e -> Log.e("Firestore", "Gagal ambil data teman: " + e.getMessage()));
    }

    private void applyFilter() {
        temanList.clear();

        int checkedId = radioFilter.getCheckedRadioButtonId();
        for (Teman t : semuaTemanList) {
            if (checkedId == R.id.radioSemua) {
                temanList.add(t);
            } else if (checkedId == R.id.radioFollowed && t.isFollowed()) {
                temanList.add(t);
            }
        }

        adapter.notifyDataSetChanged();
    }

    // Method untuk diakses dari adapter supaya bisa hapus teman di list dan update UI
    public void removeTeman(int position) {
        if (position >= 0 && position < temanList.size()) {
            temanList.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, temanList.size());
        }
    }
}
