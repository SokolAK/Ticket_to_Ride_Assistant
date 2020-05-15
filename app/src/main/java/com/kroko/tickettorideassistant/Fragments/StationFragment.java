package com.kroko.TicketToRideAssistant.Fragments;

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
import com.kroko.TicketToRideAssistant.Logic.DbHelper;
import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Logic.Player;
import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.Logic.TtRA_Application;
import com.kroko.TicketToRideAssistant.UI.Card;
import com.kroko.TicketToRideAssistant.UI.CardsCarFragment;

public class StationFragment extends Fragment implements View.OnClickListener {
    private Game game;
    private Player player;
    private SQLiteDatabase database;
    private int[] cardCounter;
    private int[] cardsNumbers;
    private String city1;
    private String city2;
    private int length;
    private int locos;
    private boolean tunnel;
    private String colors;
    private int maxCards;

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


        manageSpinner1(drawer);
        Spinner listCity2 = drawer.findViewById(R.id.spinner_city2);
        listCity2.setOnItemSelectedListener(new Listener(drawer));


        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_buildRoute);

        return drawer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.accept_icon:
                if (length <= player.getCars()) {
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
                            player.spendCars(length);
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
                } else {
                    Toast.makeText(getContext(), R.string.too_little_cars, Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.reset_icon:
                clearCards();
                refreshCards();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        database.close();
    }

    private class Listener implements AdapterView.OnItemSelectedListener {
        private View drawer;

        private Listener(View drawer) {
            this.drawer = drawer;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner listCity1 = drawer.findViewById(R.id.spinner_city1);
            Cursor cursor1 = (Cursor) listCity1.getSelectedItem();
            city1 = cursor1.getString(0);
            Spinner listCity2 = drawer.findViewById(R.id.spinner_city2);
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
                if (cursorLoco == null) {
                    locos = 0;
                }
                else {
                    locos = cursorLoco.getInt(0);
                }
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
                //if (cursorTunnel.getString(0).equals("TRUE")) {
                if (cursorTunnel.getString(0) == null) {
                    tunnel = false;
                } else {
                    tunnel = true;
                }
            }

            TextView carText = drawer.findViewById(R.id.car_number);
            carText.setText(" "+String.valueOf(length - locos));
            setAvailableCards(colors);
            maxCards = length;

            if (locos > 0) {
                drawer.findViewById(R.id.loco_icon).setVisibility(View.VISIBLE);
                drawer.findViewById(R.id.loco_number).setVisibility(View.VISIBLE);
                TextView locoText = drawer.findViewById(R.id.loco_number);
                locoText.setText(" "+String.valueOf(locos));
            } else {
                drawer.findViewById(R.id.loco_icon).setVisibility(View.INVISIBLE);
                drawer.findViewById(R.id.loco_number).setVisibility(View.INVISIBLE);
            }
            if (tunnel) {
                drawer.findViewById(R.id.tunnel_icon).setVisibility(View.VISIBLE);
                maxCards += game.getMaxExtraCardsForTunnel();
            } else {
                drawer.findViewById(R.id.tunnel_icon).setVisibility(View.INVISIBLE);
            }

            TextView lengthText = drawer.findViewById(R.id.length_value);
            lengthText.setText(" "+String.valueOf(length));
            TextView pointsText = drawer.findViewById(R.id.points_value);
            pointsText.setText(" "+String.valueOf(game.getScoring().get(length)));

            clearCards();
            refreshCards();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
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
        CardsCarFragment cardsCarFragment = new CardsCarFragment(cardsNumbers, cardCounter, maxCards);
        cardsCarFragment.setActive(true);
        cardsCarFragment.setActiveLong(false);
        cardsCarFragment.setOneColor(true);

        int[] maxCardsNumbers = new int [player.getCardsNumbers().length];
        for (int i = 0; i < player.getCardsNumbers().length; ++i) {
            if( player.getCardsNumbers()[i] < length) {
                maxCardsNumbers[i] = player.getCardsNumbers()[i];
            }
            else {

                maxCardsNumbers[i] = length;
                if(tunnel) {
                    maxCardsNumbers[i] += game.getMaxExtraCardsForTunnel();
                }
                if(game.getCards().get(i).getColor() != 'L') {
                    maxCardsNumbers[i] -= locos;
                }
            }
        }
        cardsCarFragment.setMaxCardsNumbers(maxCardsNumbers);
        ft.replace(R.id.cards_container, cardsCarFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void returnToTopPage() {
        ((MainActivity) getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }


    private void manageSpinner1(View drawer) {
        Cursor cursor = database.rawQuery("SELECT cities, _id FROM (SELECT City1 as cities, _id FROM Routes " +
                "UNION " +
                "SELECT City2, _id FROM Routes) " +
                "GROUP BY cities", null);
        Spinner listCity1 = drawer.findViewById(R.id.spinner_city1);
        setSpinnerAdapter(listCity1, cursor, "cities");
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
                    Cursor cursor2 = database.rawQuery("SELECT cities, _id FROM (SELECT City1 as cities, _id FROM Routes WHERE City2 = \'" + city1 + "\' " +
                            " UNION " +
                            "SELECT City2, _id FROM Routes WHERE City1 = \'" + city1 + "\') " +
                            "GROUP BY cities", null);

                    Spinner listCity2 = drawer.findViewById(R.id.spinner_city2);
                    setSpinnerAdapter(listCity2, cursor2, "cities");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void setSpinnerAdapter(Spinner list, Cursor cursor, String columnName) {
        SimpleCursorAdapter spinnerAdapter = new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                cursor,
                new String[]{columnName},
                new int[]{android.R.id.text1},
                0);
        list.setAdapter(spinnerAdapter);
    }

    private void setAvailableCards(String colors) {
        if (colors == null) {
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