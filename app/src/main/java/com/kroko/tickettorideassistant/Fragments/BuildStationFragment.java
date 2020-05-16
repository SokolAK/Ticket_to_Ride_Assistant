package com.kroko.TicketToRideAssistant.Fragments;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Logic.Player;
import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.Logic.Route;
import com.kroko.TicketToRideAssistant.Logic.TtRA_Application;
import com.kroko.TicketToRideAssistant.UI.Card;
import com.kroko.TicketToRideAssistant.UI.CardsCarFragment;
import com.kroko.TicketToRideAssistant.UI.CustomSpinnerAdapter;
import com.kroko.TicketToRideAssistant.UI.CustomSpinnerItem;

import java.util.ArrayList;
import java.util.Collections;

public class BuildStationFragment extends Fragment implements View.OnClickListener {
    private Game game;
    private Player player;
    private int[] cardCounter;
    private int[] cardsNumbers;
    private int maxCards;
    private Route route;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        game = ((TtRA_Application) getActivity().getApplication()).game;
        player = ((TtRA_Application) getActivity().getApplication()).player;
        cardCounter = new int[1];
        cardsNumbers = new int[game.getCards().size()];
        int numberOfStation = game.getNumberOfStations() - player.getNumberOfStations() + 1;
        maxCards = game.getStationCost().get(numberOfStation);

        for (int i = 0; i < game.getCards().size(); ++i) {
            game.getCards().get(i).setClickable(1);
            game.getCards().get(i).setVisible(1);
            cardsNumbers[i] = 0;
        }

        View drawer = inflater.inflate(R.layout.fragment_build_station, container, false);

        ImageView acceptIcon = drawer.findViewById(R.id.accept_icon);
        acceptIcon.setOnClickListener(this);
        ImageView resetIcon = drawer.findViewById(R.id.reset_icon);
        resetIcon.setOnClickListener(this);

        manageSpinner1(drawer);
        manageSpinner2(drawer);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_buildStation);

        return drawer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.accept_icon:

                if (cardCounter[0] >= maxCards) {
                    player.spendPoints(game.getStationPoints());
                    player.spendCards(cardsNumbers);
                    for (Route rout : game.getRoutes(route.getCity1(), route.getCity2(),false,false)) {
                        rout.setBuiltStation(true);
                    }
                    char builtColor = determineRouteColor(cardsNumbers);
                    route.setBuiltStationColor(builtColor);
                    route.setBuiltStationCardsNumber(cardsNumbers.clone());
                    player.addRouteStation(route);
                    player.spendStation(1);

                    clearCards();
                    refreshCards();
                    returnToTopPage();
                } else {
                    Toast.makeText(getContext(), R.string.too_little_cards, Toast.LENGTH_SHORT).show();
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
        for (Card card : game.getCards()) {
            card.setClickable(1);
            card.setVisible(1);
        }
    }

    private void refreshCards() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CardsCarFragment cardsCarFragment = new CardsCarFragment(cardsNumbers, cardCounter, maxCards);
        cardsCarFragment.setActive(true);
        cardsCarFragment.setActiveLong(true);
        cardsCarFragment.setOneColor(true);

        int[] maxCardsNumbers = new int[player.getCardsNumbers().length];
        for (int i = 0; i < player.getCardsNumbers().length; ++i) {
            if (player.getCardsNumbers()[i] < maxCards) {
                maxCardsNumbers[i] = player.getCardsNumbers()[i];
            } else {
                maxCardsNumbers[i] = maxCards;
            }
        }
        cardsCarFragment.setMaxCardsNumbers(maxCardsNumbers);
        ft.replace(R.id.cards_container, cardsCarFragment);
        ft.addToBackStack(null);
        ft.commit();
        //getActivity().findViewById(R.id.buttons_panel).setVisibility(View.VISIBLE);
    }

    private void returnToTopPage() {
        ((MainActivity) getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void manageSpinner1(View drawer) {
        ArrayList<String> cities1 = new ArrayList<>();
        for (Route route : game.getRoutes()) {
            if (!route.isBuiltStation()) {
                boolean flag = true;
                for (String city : cities1) {
                    if (city.equals(route.getCity1())) {
                        flag = false;
                    }
                }
                if (flag) {
                    cities1.add(route.getCity1());
                }
                flag = true;
                for (String city : cities1) {
                    if (city.equals(route.getCity2())) {
                        flag = false;
                    }
                }
                if (flag) {
                    cities1.add(route.getCity2());
                }
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
        listCity1.setOnItemSelectedListener(new listenerCity1(drawer));
    }

    private class listenerCity1 implements AdapterView.OnItemSelectedListener {
        private View drawer;

        private listenerCity1(View drawer) {
            this.drawer = drawer;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view,
                                   int position, long id) {

            Spinner listCity1 = drawer.findViewById(R.id.spinner_city1);
            String city1 = (String) listCity1.getSelectedItem();
            ArrayList<Route> routes = game.getRoutes(city1, false, true);

            ArrayList<CustomSpinnerItem> cityList = new ArrayList<>();
            for (Route route : routes) {
                String city2;
                if (city1.equals(route.getCity1())) {
                    city2 = route.getCity2();
                } else {
                    city2 = route.getCity1();
                }
                cityList.add(new CustomSpinnerItem(city2, route.getImageId(game, route.getColor()), route.getId()));
            }

            Collections.sort(cityList, (x, y) -> x.compareTo(y));

            Spinner spinner = drawer.findViewById(R.id.spinner_city2);
            CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), cityList);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new listenerCity2(drawer));
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private class listenerCity2 implements AdapterView.OnItemSelectedListener {
        private View drawer;

        private listenerCity2(View drawer) {
            this.drawer = drawer;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner spinner2 = drawer.findViewById(R.id.spinner_city2);
            int routeId = ((CustomSpinnerItem) spinner2.getSelectedItem()).getRouteId();
            route = game.getRoute(routeId);
            clearCards();
            refreshCards();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
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