package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SearchBarActivity extends AppCompatActivity {
    ImageView icHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar); // Pastikan sesuai dengan nama file XML kamu

        // Inisialisasi ImageView
        icHistory = findViewById(R.id.ic_history);

        // Event handler klik ic_history
//        icHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SearchBarActivity.this, HistoryActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}
