package com.kroko.TicketToRideAssistant;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class BuildRouteFragment extends Fragment {
    private int cardCounter;
    private int[] cardNumbers = new int[9];
    private SQLiteDatabase database;
    private final String DB_NAME = "TtRADatabase.db";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View drawer = inflater.inflate(R.layout.fragment_build_route, container, false);
        RecyclerView cardRecycler = drawer.findViewById(R.id.cards);

        Game game = ((TtRA_Application) getActivity().getApplication()).game;

        int[] cardImages = new int[9];
        for (int i = 0; i < cardImages.length; i++) {
            cardImages[i] = Card.cards[i].getImageResourceId();
        }

        CardImageAdapter adapter = new CardImageAdapter(cardImages, cardNumbers);
        cardRecycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
        cardRecycler.setLayoutManager(layoutManager);

        adapter.setListener(position -> {

            if (cardCounter < game.getMaxNoOfCardsToDraw()) {
                cardCounter++;
                cardNumbers[position]++;
                refreshPage();
            }
            else
            {
                String text = getString(R.string.too_much) + " " + String.valueOf(game.getMaxNoOfCardsToDraw());
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

        connectToDatabase();
        manageSpinner1(drawer);
        manageSpinner2(drawer);


        Spinner listCity2 = drawer.findViewById(R.id.spinner_city2);
        listCity2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Spinner listCity1 = drawer.findViewById(R.id.spinner_city1);
                Cursor cursor1 = (Cursor) listCity1.getSelectedItem();
                String city1 = cursor1.getString(0);
                Cursor cursor2 = (Cursor) listCity2.getSelectedItem();
                String city2 = cursor2.getString(0);

                Cursor cursorCarriage = database.rawQuery("SELECT Length FROM Routes WHERE City1=\'" + city1 + "\' AND City2=\'" + city2 + "\'", null);
                if(cursorCarriage.moveToFirst()) {
                    TextView carNumber = drawer.findViewById(R.id.car_number);
                    carNumber.setText(cursorCarriage.getString(0));
                }

                Cursor cursorLoco = database.rawQuery("SELECT Locomotives FROM Routes WHERE City1=\'" + city1 + "\' AND City2=\'" + city2 + "\'", null);
                if(cursorLoco.moveToFirst()) {
                    TextView locoNumber = drawer.findViewById(R.id.loco_number);
                    locoNumber.setText(cursorLoco.getString(0));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });





        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_buildRoute);

        return drawer;
    }

    private void clearDrawCards() {
        cardCounter = 0;
        cardNumbers = new int[9];
    }
    private void refreshPage() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new BuildRouteFragment(cardCounter,cardNumbers));
        ft.commit();
    }

    public BuildRouteFragment() {
    }
    public BuildRouteFragment(int cardCounter, int[] cardNumbers) {
        this.cardCounter = cardCounter;
        this.cardNumbers = cardNumbers;
    }

    private void returnToTopPage() {
        ((MainActivity)getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void connectToDatabase() {
        DbHelper dbHelper = new DbHelper(getContext(), DB_NAME, 1);
        dbHelper.checkDatabase();
        dbHelper.openDatabase();
        database = dbHelper.getReadableDatabase();
    }

    private void manageSpinner1(View drawer) {
        Cursor cursor = database.rawQuery("SELECT City1, MIN(_id) AS _id FROM Routes GROUP BY City1", null);
        SimpleCursorAdapter spinnerAdapter = new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                cursor,
                new String[]{"City1"},
                new int[]{android.R.id.text1},
                0);
        Spinner listCity1 = drawer.findViewById(R.id.spinner_city1);
        listCity1.setAdapter(spinnerAdapter);
    }

    public void manageSpinner2(View drawer) {
        Spinner listCity1 = drawer.findViewById(R.id.spinner_city1);
        listCity1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                int position, long id) {
                    Cursor cursor1 = (Cursor)listCity1.getSelectedItem();
                    String city1 = cursor1.getString(0);
                    if (cursor1.moveToFirst()) {
                        Cursor cursor2 = database.rawQuery("SELECT City2, _id FROM Routes WHERE City1=\'"+city1+"\'", null);
                        SimpleCursorAdapter spinnerAdapter2 = new SimpleCursorAdapter(getContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                cursor2,
                                new String[]{"City2"},
                                new int[]{android.R.id.text1},
                                0);
                        Spinner listCity2 = drawer.findViewById(R.id.spinner_city2);
                        listCity2.setAdapter(spinnerAdapter2);
                    }
                }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });
    }
}
