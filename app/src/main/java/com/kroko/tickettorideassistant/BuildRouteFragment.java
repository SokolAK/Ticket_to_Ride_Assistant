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

public class BuildRouteFragment extends Fragment implements View.OnClickListener {

    private Game game;
    private Player player;

    private SQLiteDatabase database;
    private final String DB_NAME = "TtRADatabase.db";

    private int[] cardCounter;
    private int[] cardNumbers;
    private int maxCards;
    private int[] availableCards = {1,1,1,1,1,1,1,1,1};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        game = ((TtRA_Application) getActivity().getApplication()).game;
        player = ((TtRA_Application) getActivity().getApplication()).player;
        cardCounter = new int[1];
        cardNumbers = new int[game.getCards().size()];

        maxCards = game.getMaxNoOfCardsToDraw();
        View drawer = inflater.inflate(R.layout.fragment_build_route, container, false);

        ImageView acceptIcon = drawer.findViewById(R.id.accept_icon);
        acceptIcon.setOnClickListener(this);
        ImageView resetIcon = drawer.findViewById(R.id.reset_icon);
        resetIcon.setOnClickListener(this);


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

                Cursor cursorCarriage = database.rawQuery("SELECT Length FROM Routes " +
                                                                "WHERE (City1=\'" + city1 + "\' AND City2=\'" + city2 + "\') " +
                                                                "OR (City2=\'" + city1 + "\' AND City1=\'" + city2 + "\') ", null);
                if(cursorCarriage.moveToFirst()) {
                    TextView carNumber = drawer.findViewById(R.id.car_number);
                    carNumber.setText(cursorCarriage.getString(0));
                }

                Cursor cursorLoco = database.rawQuery("SELECT Locomotives FROM Routes " +
                                                            "WHERE (City1=\'" + city1 + "\' AND City2=\'" + city2 + "\') " +
                                                            "OR (City2=\'" + city1 + "\' AND City1=\'" + city2 + "\') ", null);
                if(cursorLoco.moveToFirst()) {
                    TextView locoNumber = drawer.findViewById(R.id.loco_number);
                    locoNumber.setText(cursorLoco.getString(0));
                }

                Cursor cursorColors = database.rawQuery("SELECT Colors FROM Routes " +
                                                                "WHERE (City1=\'" + city1 + "\' AND City2=\'" + city2 + "\') " +
                                                                "OR (City2=\'" + city1 + "\' AND City1=\'" + city2 + "\') ", null);
                if(cursorColors.moveToFirst()) {
                    availableCards = checkAvailableCards(cursorColors.getString(0));
                }

                Cursor cursorTunnel = database.rawQuery("SELECT Tunnel FROM Routes " +
                                                                "WHERE (City1=\'" + city1 + "\' AND City2=\'" + city2 + "\') " +
                                                                "OR (City2=\'" + city1 + "\' AND City1=\'" + city2 + "\') ", null);
                if(cursorTunnel.moveToFirst()) {
                    if(cursorTunnel.getString(0).equals("true")) {
                        drawer.findViewById(R.id.tunnel_icon).setVisibility(View.VISIBLE);
                    }
                    else {
                        drawer.findViewById(R.id.tunnel_icon).setVisibility(View.INVISIBLE);
                    }
                }
                clearDrawCards();
                refreshCards();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_icon:
                if(cardCounter[0] == 0) {
                    String text = getString(R.string.too_little);
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
                else {
                    Player player = ((TtRA_Application) getActivity().getApplication()).player;
                    for (int i = 0; i < cardNumbers.length; i++) {
                        int cardNumber = player.getCards()[i] + cardNumbers[i];
                        player.getCards()[i] = cardNumber;
                    }
                    clearDrawCards();
                    refreshCards();
                    returnToTopPage();
                }
                break;
            case R.id.reset_icon:
                clearDrawCards();
                refreshCards();
                break;
        }
    }

    private void clearDrawCards() {
        cardCounter[0] = 0;
        //viewModel.setCardCounter(0);
        cardNumbers = new int[game.getCards().size()];
    }
    private void refreshCards() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.cards_container, new CardsFragment(cardCounter, cardNumbers, maxCards, availableCards.clone(), true));
        ft.addToBackStack(null);
        ft.commit();
    }

    private void returnToTopPage() {
        ((MainActivity)getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public BuildRouteFragment() {
    }

    private void connectToDatabase() {
        DbHelper dbHelper = new DbHelper(getContext(), DB_NAME, 1);
        dbHelper.checkDatabase();
        dbHelper.openDatabase();
        database = dbHelper.getReadableDatabase();
    }

    private void manageSpinner1(View drawer) {
        Cursor cursor = database.rawQuery("SELECT cities, _id FROM (SELECT City1 as cities, _id FROM Routes " +
                                                                        "UNION " +
                "                                                        SELECT City2, _id FROM Routes) " +
                                                                        "GROUP BY cities", null);
        SimpleCursorAdapter spinnerAdapter = new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                cursor,
                new String[]{"cities"},
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
                    //Cursor cursor2 = database.rawQuery("SELECT City2, _id FROM Routes WHERE City1=\'"+city1+"\'", null);
                    Cursor cursor2 = database.rawQuery("SELECT cities, _id FROM (SELECT City1 as cities, _id FROM Routes WHERE City2 = \'"+city1+"\' " +
                                                                                     " UNION " +
                                                                                     "SELECT City2, _id FROM Routes WHERE City1 = \'"+city1+"\') " +
                                                                                     "GROUP BY cities", null);

                    SimpleCursorAdapter spinnerAdapter2 = new SimpleCursorAdapter(getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            cursor2,
                            new String[]{"cities"},
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

    private int[] checkAvailableCards(String colors) {
        int[] availableColors = {0,0,0,0,0,0,0,0,1};
        for(char ch: colors.toCharArray()) {
            switch(ch) {
                case 'V':
                    availableColors[0] = 1;
                    break;
                case 'O':
                    availableColors[1] = 1;
                    break;
                case 'B':
                    availableColors[2] = 1;
                    break;
                case 'Y':
                    availableColors[3] = 1;
                    break;
                case 'A':
                    availableColors[4] = 1;
                    break;
                case 'G':
                    availableColors[5] = 1;
                    break;
                case 'R':
                    availableColors[6] = 1;
                    break;
                case 'w':
                    availableColors[7] = 1;
                    break;
            }
        }
        return availableColors;
    }
}