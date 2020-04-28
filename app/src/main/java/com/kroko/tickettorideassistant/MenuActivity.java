package com.kroko.tickettorideassistant;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        AdapterView.OnItemClickListener itemClickListener = (listView, v, position, gameId) -> {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);

            Game game = ((TtRA_Application) getApplication()).game;
            game.prepare((int) gameId);
            Player player = ((TtRA_Application) getApplication()).player;
            player.prepare(game);

            startActivity(intent);
        };

        ListView listView = findViewById(R.id.list_games);
        listView.setOnItemClickListener(itemClickListener);
    }
}
