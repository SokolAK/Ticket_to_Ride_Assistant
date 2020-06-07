package com.kroko.TicketToRideAssistant.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.UI.CustomItem;
import com.kroko.TicketToRideAssistant.UI.CustomItemAdapter;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //ArrayList<CustomItem> gamesList = new ArrayList<>();
        //String[] games = getResources().getStringArray(R.array.games);
        //gamesList.add(new CustomItem(games[0], R.drawable.europe, 0));
        ListView list = findViewById(R.id.games_list);
        //CustomItemAdapter adapter = new CustomItemAdapter(this, gamesList);
        //list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("gameId", position);
            startActivity(intent);
            finish();
        });
    }
}
