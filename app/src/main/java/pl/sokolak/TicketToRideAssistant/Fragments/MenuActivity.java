package pl.sokolak.TicketToRideAssistant.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import pl.sokolak.TicketToRideAssistant.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ListView list = findViewById(R.id.games_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_list_text_view, getResources().getStringArray(R.array.games));
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("gameId", position);
            startActivity(intent);
            finish();
        });
    }
}
