package com.example.proyekakhirpam;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class SearchBar extends LinearLayout {

    public SearchBar(Context context) {
        super(context);
        init(context);
    }

    public SearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.search_bar, this, true);

        ImageView notifIcon = findViewById(R.id.ic_notif);
        ImageView notifHistory = findViewById(R.id.ic_history);

        notifHistory.setOnClickListener(v -> {
            Intent intent = new Intent(context, HistoryPage.class);
            context.startActivity(intent);
        });
    }
}
