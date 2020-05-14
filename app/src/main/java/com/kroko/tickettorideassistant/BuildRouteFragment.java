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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class BuildRouteFragment extends Fragment implements View.OnClickListener {
    private Game game;
    private Player player;
    private SQLiteDatabase database;
    private final String DB_NAME = "TtRADatabase.db";
    private int[] cardCounter;
    private int[] cardsNumbers;
    private String city1;
    private String city2;
    private int maxCards;
    private Route route;
    private ArrayList<String> cities1;
    private ArrayList<String> cities2;
    int cars;
    int routeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_buildRoute);

        return drawer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.accept_icon:
                int id = route.get_id();
                int length = route.getLength();
                int locos = route.getLocos();
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

                            char builtColor = determineRouteColor(cardsNumbers);
                            game.removeRoute(route.get_id());
                            player.addRoute(route);

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


    private Route getRoute(String city1, String city2) {
        for(Route route: game.getRoutes()) {
            if((route.getCity1().equals(city1) && route.getCity2().equals(city2)) ||
                    (route.getCity2().equals(city1) && route.getCity1().equals(city2))) {
                return route;
            }
        }
        return null;
    }

    private void clearCards() {
        cardCounter[0] = 0;
        Game game = ((TtRA_Application) getActivity().getApplication()).game;
        for (int i = 0; i < game.getCards().size(); ++i) {
            cardsNumbers[i] = 0;
        }
        setAvailableCards(route.get_id(), route.getColor());
    }

    private void refreshCards() {
        if(route.get_id() > 0) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            CardsCarFragment cardsCarFragment = new CardsCarFragment(cardsNumbers, cardCounter, maxCards);
            cardsCarFragment.setActive(true);
            cardsCarFragment.setOneColor(true);

            int[] maxCardsNumbers = new int[player.getCardsNumbers().length];
            for (int i = 0; i < player.getCardsNumbers().length; ++i) {
                if (player.getCardsNumbers()[i] < route.getLength()) {
                    maxCardsNumbers[i] = player.getCardsNumbers()[i];
                } else {
                    maxCardsNumbers[i] = route.getLength();
                    if (route.isTunnel()) {
                        maxCardsNumbers[i] += game.getMaxExtraCardsForTunnel();
                    }
                    if (game.getCards().get(i).getColor() != 'L') {
                        maxCardsNumbers[i] -= route.getLocos();
                    }
                }
            }
            cardsCarFragment.setMaxCardsNumbers(maxCardsNumbers);
            ft.replace(R.id.cards_container, cardsCarFragment);
            ft.addToBackStack(null);
            ft.commit();
            getActivity().findViewById(R.id.draw_buttons_panel).setVisibility(View.VISIBLE);
        } else {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.cards_container, new BlankFragment(getString(R.string.already_built)));
            ft.addToBackStack(null);
            ft.commit();
            getActivity().findViewById(R.id.draw_buttons_panel).setVisibility(View.INVISIBLE);
        }
    }

    private void returnToTopPage() {
        ((MainActivity) getActivity()).onNavigationItemSelected(0);
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
        cities1 = new ArrayList<>();
        for(Route route: game.getRoutes()) {
            boolean flag = true;
            for(String city: cities1) {
                if(city.equals(route.getCity1())) {
                    flag = false;
                }
            }
            if(flag) {
                cities1.add(route.getCity1());
            }
            flag = true;
            for(String city: cities1) {
                if(city.equals(route.getCity2())) {
                    flag = false;
                }
            }
            if(flag) {
                cities1.add(route.getCity2());
            }
        }
        Collections.sort(cities1, (x, y) -> x.compareTo(y));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                cities1);
        Spinner spinner = drawer.findViewById(R.id.spinner_city1);
        spinner.setAdapter(adapter);
    }

    public void manageSpinner2(View drawer) {
        Spinner listCity1 = drawer.findViewById(R.id.spinner_city1);
        listCity1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {


                city1 = (String) listCity1.getSelectedItem();
                cities2 = new ArrayList<>();
                for(Route route: game.getRoutes()) {
                    if(route.getCity1().equals(city1)) {
                        cities2.add(route.getCity2());
                    }
                    if(route.getCity2().equals(city1)) {
                        cities2.add(route.getCity1());
                    }
                }

                ArrayList<CustomSpinnerItem> cityList = new ArrayList<>();
                for(String city2: cities2) {
                    for(Route route: game.getRoutes()) {
                        if((route.getCity1().equals(city1) && route.getCity2().equals(city2) || (route.getCity2().equals(city1) && route.getCity1().equals(city2)))) {
                            if(route.getColor() == '-') {
                                if(route.getLength() > route.getLocos()) {
                                    cityList.add(new CustomSpinnerItem(city2, R.drawable.any, route.get_id()));
                                }
                                else {
                                    cityList.add(new CustomSpinnerItem(city2, R.drawable.loco, route.get_id()));
                                }
                            }
                            else {
                                for (Card card : game.getCards()) {
                                    if (card.getColor() == route.getColor()) {
                                        cityList.add(new CustomSpinnerItem(city2, card.getImageResourceId(), route.get_id()));
                                    }
                                }
                            }
                        }
                    }
                }
                Collections.sort(cityList, (x, y) -> x.compareTo(y));


                Spinner spinner = drawer.findViewById(R.id.spinner_city2);
                CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), cityList);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Spinner spinner3 = drawer.findViewById(R.id.spinner_city2);
                        routeId = ((CustomSpinnerItem) spinner3.getSelectedItem()).getRouteId();
                        route = game.getRoute(routeId);

                        cars = route.getLength() - route.getLocos();
                        maxCards = route.getLength();

                        if (cars > 0) {
                            drawer.findViewById(R.id.car_icon).setVisibility(View.VISIBLE);
                            drawer.findViewById(R.id.car_number).setVisibility(View.VISIBLE);
                            TextView carText = drawer.findViewById(R.id.car_number);
                            carText.setText(" " + String.valueOf(cars));
                        } else {
                            drawer.findViewById(R.id.car_icon).setVisibility(View.INVISIBLE);
                            drawer.findViewById(R.id.car_number).setVisibility(View.INVISIBLE);
                        }
                        if (route.getLocos() > 0) {
                            drawer.findViewById(R.id.loco_icon).setVisibility(View.VISIBLE);
                            drawer.findViewById(R.id.loco_number).setVisibility(View.VISIBLE);
                            TextView locoText = drawer.findViewById(R.id.loco_number);
                            locoText.setText(" " + String.valueOf(route.getLocos()));
                        } else {
                            drawer.findViewById(R.id.loco_icon).setVisibility(View.INVISIBLE);
                            drawer.findViewById(R.id.loco_number).setVisibility(View.INVISIBLE);
                        }
                        if (route.isTunnel()) {
                            drawer.findViewById(R.id.tunnel_icon).setVisibility(View.VISIBLE);
                            maxCards += game.getMaxExtraCardsForTunnel();
                        } else {
                            drawer.findViewById(R.id.tunnel_icon).setVisibility(View.INVISIBLE);
                        }

                        TextView lengthText = drawer.findViewById(R.id.length_value);
                        lengthText.setText(" " + String.valueOf(route.getLength()));
                        TextView pointsText = drawer.findViewById(R.id.points_value);
                        pointsText.setText(" " + String.valueOf(game.getScoring().get(route.getLength())));

                        setAvailableCards(route.get_id(), route.getColor());

                        clearCards();
                        refreshCards();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

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

    private void setAvailableCards(int id, char color) {
        for (Card card : game.getCards()) {
            card.setClickable(0);
            card.setVisible(0);
        }
        for (Card card : game.getCards()) {
            if(card.getColor() == 'L') {
                card.setClickable(1);
                card.setVisible(1);
            } else {
                if (color == '-') {
                    if(cars > 0) {
                        card.setClickable(1);
                        card.setVisible(1);
                    }
                } else {
                    if (card.getColor() == color) {
                        card.setClickable(1);
                        card.setVisible(1);
                    }
                }
            }
        }
    }

    private char determineRouteColor(int[] cardsNumbers) {
        int i = 0;
        for (; i < cardsNumbers.length; ++i) {
            if (cardsNumbers[i] > 0) {
                break;
            }
        }
        return game.getCards().get(i).getColor();
    }
}