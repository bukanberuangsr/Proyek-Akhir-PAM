package com.example.proyekakhirpam;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
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

    private RecyclerView recyclerView;
    private TemanAdapter adapter;
    private List<Teman> temanList = new ArrayList<>();
    private List<Teman> semuaTemanList = new ArrayList<>();

    private ImageView btnBack;
    private RadioGroup radioFilter;

    private FirebaseFirestore db;
    private String currentUserId;

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

            for (QueryDocumentSnapshot doc : querySnapshot) {
                String uid = doc.getId();
                if (!uid.equals(currentUserId)) {
                    String username = doc.getString("username");
                    String email = doc.getString("email");

                    // Cek apakah current user follow user ini
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

                                // Terapkan filter ulang setiap data baru masuk
                                applyFilter();
                            });
                }
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
            } else if (checkedId == R.id.radioBelumFollow && !t.isFollowed()) {
                temanList.add(t);
            }
        }

        adapter.notifyDataSetChanged();
    }
}
