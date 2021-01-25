package pl.sokolak.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Domain.Route;
import pl.sokolak.TicketToRideAssistant.TtRA_Application;
import pl.sokolak.TicketToRideAssistant.UI.Card;
import pl.sokolak.TicketToRideAssistant.UI.CarCardsFragment;
import pl.sokolak.TicketToRideAssistant.UI.TextImageItem;
import pl.sokolak.TicketToRideAssistant.UI.SpinnerListenerInterface;
import pl.sokolak.TicketToRideAssistant.UI.SpinnerRouteFragment;

public class BuildStationFragment extends Fragment implements View.OnClickListener, SpinnerListenerInterface {
    private Game game;
    private Player player;
    private int[] cardCounter;
    private int[] cardsNumbers;
    private int maxCards;
    private Route route;
    private View drawer;

    public BuildStationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        game = ((TtRA_Application) requireActivity().getApplication()).game;
        player = ((TtRA_Application) requireActivity().getApplication()).player;
        cardCounter = new int[1];
        cardsNumbers = new int[game.getCards().size()];
        int numberOfStation = game.getNumberOfStations() - player.getNumberOfStations() + 1;
        maxCards = game.getStationCost().get(numberOfStation);

        for (int i = 0; i < game.getCards().size(); ++i) {
            game.getCards().get(i).setClickable(true);
            game.getCards().get(i).setVisible(true);
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
                    player.spendCards(cardsNumbers);
                    for (Route rout : game.getRoutes(route.getCity1(), route.getCity2(), false, false)) {
                        rout.setBuiltStation(true);
                    }
                    Card.CarCardColor builtColor = determineRouteColor(cardsNumbers);
                    route.setBuiltStationColor(builtColor);
                    route.setBuiltStationCardsNumber(cardsNumbers.clone());
                    player.addRouteStation(route);

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
    public void onSpinnerItemSelected(TextImageItem... items) {
        if (items.length == 1) {
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
            card.setClickable(true);
            card.setVisible(true);
        }
    }

    private void refreshCards() {
        int[] maxCardsNumbers = new int[player.getCardsNumbers().length];
        for (int i = 0; i < player.getCardsNumbers().length; ++i) {
            maxCardsNumbers[i] = Math.min(player.getCardsNumbers()[i], maxCards);
        }

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CarCardsFragment carCardsFragment = CarCardsFragment.builder().cardsNumbers(cardsNumbers).
                cardCounter(cardCounter).maxCards(maxCards).maxCardsNumbers(maxCardsNumbers).
                active(true).activeLong(true).oneColor(true).
                build();
        ft.replace(R.id.cards_container, carCardsFragment);
        ft.commit();
    }

    private void returnToTopPage() {
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new ShowBuiltStationsFragment());
        ft.commit();
    }

    private Card.CarCardColor determineRouteColor(int[] cardsNumbers) {
        int i = 0;
        for (; i < cardsNumbers.length; ++i) {
            if (cardsNumbers[i] > 0) {
                break;
            }
        }
        return game.getCards().get(i).getCarCardColor();
    }
}