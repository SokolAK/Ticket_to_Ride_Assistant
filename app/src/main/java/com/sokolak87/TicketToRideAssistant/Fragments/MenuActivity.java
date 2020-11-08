package com.sokolak87.TicketToRideAssistant.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.sokolak87.TicketToRideAssistant.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ListView list = findViewById(R.id.games_list);
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("gameId", position);
            startActivity(intent);
            finish();
        });
    }
}
