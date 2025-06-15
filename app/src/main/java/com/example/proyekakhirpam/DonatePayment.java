package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DonatePayment extends AppCompatActivity {

    ImageView ivGambarDonasi;
    TextView tvJudulDonasi;
    Button btnBayar, btn10, btn25, btn50, btn100, btn150;
    EditText etNominal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_payment);

        ivGambarDonasi = findViewById(R.id.img_main);
        tvJudulDonasi = findViewById(R.id.tv_main);

        Intent intent = getIntent();
        String judulDonasi = intent.getStringExtra("judul");
        String gambarDonasi = intent.getStringExtra("gambar");


        tvJudulDonasi.setText(judulDonasi);
        Glide.with(this)
                .load(gambarDonasi)
                .into(ivGambarDonasi);

        etNominal = findViewById(R.id.et_nominal);
        btn10 = findViewById(R.id.btn_donate_10);
        btn10.setOnClickListener(v->etNominal.setText("10000"));
        btn25 = findViewById(R.id.btn_donate_25);
        btn25.setOnClickListener(v->etNominal.setText("25000"));
        btn50 = findViewById(R.id.btn_donate_50);
        btn50.setOnClickListener(v->etNominal.setText("50000"));
        btn100 = findViewById(R.id.btn_donate_100);
        btn100.setOnClickListener(v->etNominal.setText("100000"));
        btn150 = findViewById(R.id.btn_donate_150);
        btn150.setOnClickListener(v->etNominal.setText("150000"));

        btnBayar = findViewById(R.id.btn_confirm_payment);
        btnBayar.setOnClickListener(v -> {
            // Tambahin data dari tombol/editText ke intent
            // Data nominal ditambahkan ke card RecyclerView di LeftoverShareFragment
            String nominal = etNominal.getText().toString();

            // Get current user
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                // Handle not logged in
                Toast.makeText(this, "You must be logged in to donate!", Toast.LENGTH_SHORT).show();
                return;
            }

            String userName = user.getDisplayName();
            // String userId = user.getUid();

            // Find the donation document by judul (or better: by id if you have it)
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("donation")
                    .whereEqualTo("judul_donasi", judulDonasi)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Assume only one document per judul
                            String docId = queryDocumentSnapshots.getDocuments().get(0).getId();
                            // Update the nominal_donasi (add the new amount)
                            db.collection("donation").document(docId)
                                    .update(
                                            "nominal_donasi", com.google.firebase.firestore.FieldValue.increment(Integer.parseInt(nominal)),
                                            "nama_donatur", userName // Optionally update donor name
                                            // You can also add a donors array here if needed
                                    )
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Donation successful!", Toast.LENGTH_SHORT).show();
                                        // Go back to main or wherever
                                        Intent backIntent = new Intent(DonatePayment.this, MainActivity.class);
                                        startActivity(backIntent);
                                        finish();
                                    });
                        }
                    });

//            Intent backIntent = new Intent(DonatePayment.this, MainActivity.class);
//            backIntent.putExtra("judul", judulDonasi);
//            backIntent.putExtra("gambar", gambarDonasi);
//            backIntent.putExtra("nominal", nominal);
//            startActivity(backIntent);
//            finish();
        });
    }
}