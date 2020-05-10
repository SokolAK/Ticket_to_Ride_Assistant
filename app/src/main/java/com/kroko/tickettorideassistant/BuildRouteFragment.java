package com.kroko.TicketToRideAssistant;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class BuildRouteFragment extends Fragment implements View.OnClickListener {

    private Game game;
    private Player player;

    private SQLiteDatabase database;
    private final String DB_NAME = "TtRADatabase.db";

    private int[] cardCounter;
    private int[] cardsNumbers;
    private String city1;
    private String city2;
    private int length;
    private int locos;
    private boolean tunnel;
    private String colors;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        game = ((TtRA_Application) getActivity().getApplication()).game;
        player = ((TtRA_Application) getActivity().getApplication()).player;
        cardCounter = new int[1];
        cardsNumbers = new int[game.getCards().size()];
        for (int i = 0; i < game.getCards().size(); ++i) {
            game.getCards().get(i).setClickable(1);
            game.getCards().get(i).setVisible(1);
            cardsNumbers[i] = 0;
        }

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
                city1 = cursor1.getString(0);
                Cursor cursor2 = (Cursor) listCity2.getSelectedItem();
                city2 = cursor2.getString(0);

                Cursor cursorCarriage = database.rawQuery("SELECT Length FROM Routes " +
                        "WHERE (City1=\'" + city1 + "\' AND City2=\'" + city2 + "\') " +
                        "OR (City2=\'" + city1 + "\' AND City1=\'" + city2 + "\') ", null);
                if (cursorCarriage.moveToFirst()) {
                    length = cursorCarriage.getInt(0);
                }

                Cursor cursorLoco = database.rawQuery("SELECT Locomotives FROM Routes " +
                        "WHERE (City1=\'" + city1 + "\' AND City2=\'" + city2 + "\') " +
                        "OR (City2=\'" + city1 + "\' AND City1=\'" + city2 + "\') ", null);
                if (cursorLoco.moveToFirst()) {
                    locos = cursorLoco.getInt(0);
                }

                Cursor cursorColors = database.rawQuery("SELECT Colors FROM Routes " +
                        "WHERE (City1=\'" + city1 + "\' AND City2=\'" + city2 + "\') " +
                        "OR (City2=\'" + city1 + "\' AND City1=\'" + city2 + "\') ", null);
                if (cursorColors.moveToFirst()) {
                    colors = cursorColors.getString(0);
                }

                Cursor cursorTunnel = database.rawQuery("SELECT Tunnel FROM Routes " +
                        "WHERE (City1=\'" + city1 + "\' AND City2=\'" + city2 + "\') " +
                        "OR (City2=\'" + city1 + "\' AND City1=\'" + city2 + "\') ", null);
                if (cursorTunnel.moveToFirst()) {
                    if (cursorTunnel.getString(0).equals("true")) {
                        tunnel = true;
                    } else {
                        tunnel = false;
                    }
                }


                TextView lengthText = drawer.findViewById(R.id.car_number);
                lengthText.setText(String.valueOf(length));
                TextView locoText = drawer.findViewById(R.id.loco_number);
                locoText.setText(String.valueOf(locos));

                setAvailableCards(colors);

                if (tunnel) {
                    drawer.findViewById(R.id.tunnel_icon).setVisibility(View.VISIBLE);
                } else {
                    drawer.findViewById(R.id.tunnel_icon).setVisibility(View.INVISIBLE);
                }


                clearCards();
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
                if ( length <= player.getCars()) {
                    if (cardCounter[0] >= length) {
                        int selectedLocos = 0;
                        for (int i = 0; i < game.getCards().size(); ++i) {
                            if (game.getCards().get(i).getColor() == 'L') {
                                selectedLocos = cardsNumbers[i];
                            }
                        }
                        if (selectedLocos >= locos) {
                            Player player = ((TtRA_Application) getActivity().getApplication()).player;
                            player.addPoints(game.getScoring().get(length));
                            player.spendCars(cardCounter[0]);
                            player.spendCards(cardsNumbers);
                            clearCards();
                            refreshCards();
                            returnToTopPage();
                        } else {
                            Toast.makeText(getContext(), R.string.too_little_locos, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), R.string.too_little_cards, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), R.string.too_little_cars, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.reset_icon:
                clearCards();
                refreshCards();
                break;
        }
    }

    private void clearCards() {
        cardCounter[0] = 0;
        Game game = ((TtRA_Application) getActivity().getApplication()).game;
        for (int i = 0; i < game.getCards().size(); ++i) {
            cardsNumbers[i] = 0;
        }
        setAvailableCards(colors);
    }

    private void refreshCards() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CardsFragment cardsFragment = new CardsFragment(cardsNumbers, cardCounter);
        cardsFragment.setDrawingCards(false);
        cardsFragment.setMaxCardsNumbers(player.getCardsNumbers());
        ft.replace(R.id.cards_container, cardsFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void returnToTopPage() {
        ((MainActivity) getActivity()).onNavigationItemSelected(0);
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
                Cursor cursor1 = (Cursor) listCity1.getSelectedItem();
                String city1 = cursor1.getString(0);
                if (cursor1.moveToFirst()) {
                    //Cursor cursor2 = database.rawQuery("SELECT City2, _id FROM Routes WHERE City1=\'"+city1+"\'", null);
                    Cursor cursor2 = database.rawQuery("SELECT cities, _id FROM (SELECT City1 as cities, _id FROM Routes WHERE City2 = \'" + city1 + "\' " +
                            " UNION " +
                            "SELECT City2, _id FROM Routes WHERE City1 = \'" + city1 + "\') " +
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

    private void setAvailableCards(String colors) {
        if (colors.equals("")) {
            for (Card card : game.getCards()) {
                card.setClickable(1);
                card.setVisible(1);
            }
        } else {
            for (Card card : game.getCards()) {
                card.setClickable(0);
                card.setVisible(0);
                for (char color : colors.toCharArray()) {
                    if (card.getColor() == color || card.getColor() == 'L') {
                        card.setClickable(1);
                        card.setVisible(1);
                        break;
                    }
                }
            }
        }
    }
}