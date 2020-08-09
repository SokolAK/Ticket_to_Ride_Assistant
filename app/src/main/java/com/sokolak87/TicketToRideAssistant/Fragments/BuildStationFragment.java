package com.sokolak87.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.sokolak87.TicketToRideAssistant.Logic.Game;
import com.sokolak87.TicketToRideAssistant.Logic.Player;
import com.sokolak87.TicketToRideAssistant.R;
import com.sokolak87.TicketToRideAssistant.Logic.Route;
import com.sokolak87.TicketToRideAssistant.Logic.TtRA_Application;
import com.sokolak87.TicketToRideAssistant.UI.Card;
import com.sokolak87.TicketToRideAssistant.UI.CardsCarFragment;
import com.sokolak87.TicketToRideAssistant.UI.CustomItem;
import com.sokolak87.TicketToRideAssistant.UI.SpinnerListenerInterface;
import com.sokolak87.TicketToRideAssistant.UI.SpinnerRouteFragment;

public class BuildStationFragment extends Fragment implements View.OnClickListener, SpinnerListenerInterface {
    private Game game;
    private Player player;
    private int[] cardCounter;
    private int[] cardsNumbers;
    private int maxCards;
    private Route route;
    private View drawer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        game = ((TtRA_Application) requireActivity().getApplication()).game;
        player = ((TtRA_Application) requireActivity().getApplication()).player;
        cardCounter = new int[1];
        cardsNumbers = new int[game.getCards().size()];
        int numberOfStation = game.getNumberOfStations() - player.getNumberOfStations() + 1;
        maxCards = game.getStationCost().get(numberOfStation);

        for (int i = 0; i < game.getCards().size(); ++i) {
            game.getCards().get(i).setClickable(1);
            game.getCards().get(i).setVisible(1);
            cardsNumbers[i] = 0;
        }

        drawer = inflater.inflate(R.layout.fragment_build_station, container, false);
        ImageView acceptIcon = drawer.findViewById(R.id.accept_button);
        acceptIcon.setOnClickListener(this);
        ImageView resetIcon = drawer.findViewById(R.id.reset_button);
        resetIcon.setOnClickListener(this);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        SpinnerRouteFragment spinnerRouteFragment = new SpinnerRouteFragment('S');
        ft.replace(R.id.spinners_container, spinnerRouteFragment);
        ft.commit();

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_buildStation);

        return drawer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.accept_button:

                if (cardCounter[0] >= maxCards) {
                    //player.spendPoints(game.getStationPoints());
                    player.spendCards(cardsNumbers);
                    for (Route rout : game.getRoutes(route.getCity1(), route.getCity2(),false,false)) {
                        rout.setBuiltStation(true);
                    }
                    char builtColor = determineRouteColor(cardsNumbers);
                    route.setBuiltStationColor(builtColor);
                    route.setBuiltStationCardsNumber(cardsNumbers.clone());
                    player.addRouteStation(route);
                    //player.spendStation(1);

                    clearCards();
                    refreshCards();
                    returnToTopPage();
                } else {
                    Toast.makeText(getContext(), R.string.too_little_cards, Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.reset_button:
                clearCards();
                refreshCards();
                break;
        }
    }

    @Override
    public void onSpinnerItemSelected(CustomItem... items) {
        if(items.length == 1) {
            int routeId = items[0].getItemId();
            route = game.getRoute(routeId);
            clearCards();
            refreshCards();
        }
    }

    private void clearCards() {
        cardCounter[0] = 0;
        Game game = ((TtRA_Application) requireActivity().getApplication()).game;
        for (int i = 0; i < game.getCards().size(); ++i) {
            cardsNumbers[i] = 0;
        }
        for (Card card : game.getCards()) {
            card.setClickable(1);
            card.setVisible(1);
        }
    }

    private void refreshCards() {
        int[] maxCardsNumbers = new int[player.getCardsNumbers().length];
        for (int i = 0; i < player.getCardsNumbers().length; ++i) {
            if (player.getCardsNumbers()[i] < maxCards) {
                maxCardsNumbers[i] = player.getCardsNumbers()[i];
            } else {
                maxCardsNumbers[i] = maxCards;
            }
        }
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CardsCarFragment cardsCarFragment = CardsCarFragment.builder().cardsNumbers(cardsNumbers).
                cardCounter(cardCounter).maxCards(maxCards).maxCardsNumbers(maxCardsNumbers).
                active(true).activeLong(true).oneColor(true).
                build();
        ft.replace(R.id.cards_container, cardsCarFragment);
        //ft.addToBackStack(null);
        ft.commit();
        //requireActivity().findViewById(R.id.buttons_panel).setVisibility(View.VISIBLE);
    }

    private void returnToTopPage() {
        //((MainActivity) requireActivity()).onNavigationItemSelected(0);
        //NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
        //navigationView.getMenu().getItem(0).setChecked(true);
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new ShowBuiltStationsFragment());
        ft.commit();
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