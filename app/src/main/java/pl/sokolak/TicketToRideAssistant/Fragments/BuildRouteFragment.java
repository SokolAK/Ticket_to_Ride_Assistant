package pl.sokolak.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pl.sokolak.TicketToRideAssistant.Logic.Game;
import pl.sokolak.TicketToRideAssistant.Logic.Player;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Logic.Route;
import pl.sokolak.TicketToRideAssistant.Logic.TtRA_Application;
import pl.sokolak.TicketToRideAssistant.UI.Card;
import pl.sokolak.TicketToRideAssistant.UI.CardsCarFragment;
import pl.sokolak.TicketToRideAssistant.UI.CustomItem;
import pl.sokolak.TicketToRideAssistant.UI.SpinnerRouteFragment;
import pl.sokolak.TicketToRideAssistant.UI.SpinnerListenerInterface;

public class BuildRouteFragment extends Fragment implements View.OnClickListener, SpinnerListenerInterface {
    private Game game;
    private Player player;
    private int[] cardCounter;
    private int[] cardsNumbers;
    private int maxCards;
    private Route route;
    int cars;
    private View drawer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        game = ((TtRA_Application) requireActivity().getApplication()).game;
        player = ((TtRA_Application) requireActivity().getApplication()).player;
        cardCounter = new int[1];
        cardsNumbers = new int[game.getCards().size()];

        for (int i = 0; i < game.getCards().size(); ++i) {
            game.getCards().get(i).setClickable(1);
            game.getCards().get(i).setVisible(1);
            cardsNumbers[i] = 0;
        }


        drawer = inflater.inflate(R.layout.fragment_build_route, container, false);

        ImageView acceptIcon = drawer.findViewById(R.id.accept_button);
        acceptIcon.setOnClickListener(this);
        ImageView resetIcon = drawer.findViewById(R.id.reset_button);
        resetIcon.setOnClickListener(this);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        SpinnerRouteFragment spinnerRouteFragment = new SpinnerRouteFragment('R');
        ft.replace(R.id.spinners_container, spinnerRouteFragment);
        ft.commit();

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_buildRoute);

        return drawer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.accept_button:
                int length = route.getLength();
                int locos = route.getLocos();
                if (length <= player.getNumberOfCars()) {
                    if (cardCounter[0] >= length) {
                        int selectedLocos = 0;
                        for (int i = 0; i < game.getCards().size(); ++i) {
                            if (game.getCards().get(i).getColor() == 'L') {
                                selectedLocos = cardsNumbers[i];
                            }
                        }
                        if (selectedLocos >= locos) {
                            player.spendCars(length);
                            player.spendCards(cardsNumbers);
                            route.setBuilt(true);
                            char builtColor = determineRouteColor(cardsNumbers);
                            route.setBuiltColor(builtColor);
                            route.setBuiltCardsNumber(cardsNumbers.clone());
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

            case R.id.reset_button:
                clearCards();
                refreshCards();
                break;
        }
    }

    private void clearCards() {
        cardCounter[0] = 0;
        Game game = ((TtRA_Application) requireActivity().getApplication()).game;
        for (int i = 0; i < game.getCards().size(); ++i) {
            cardsNumbers[i] = 0;
        }
        setAvailableCards(route.getColor());
    }

    private void refreshCards() {
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
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CardsCarFragment cardsCarFragment = CardsCarFragment.builder().cardsNumbers(cardsNumbers).
                cardCounter(cardCounter).maxCards(maxCards).maxCardsNumbers(maxCardsNumbers).
                active(true).activeLong(true).oneColor(true).
                build();
        ft.replace(R.id.cards_container, cardsCarFragment);
        ft.commit();
        requireActivity().findViewById(R.id.buttons_panel).setVisibility(View.VISIBLE);
    }

    private void returnToTopPage() {
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new ShowBuiltRoutesFragment());
        ft.commit();
    }

    @Override
    public void onSpinnerItemSelected(CustomItem... items) {
        if (items.length == 1) {
            int routeId = items[0].getItemId();
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

            setAvailableCards(route.getColor());

            clearCards();
            refreshCards();
        }
    }

    private void setAvailableCards(char color) {
        for (Card card : game.getCards()) {
            card.setClickable(0);
            card.setVisible(0);
        }
        for (Card card : game.getCards()) {
            if (card.getColor() == 'L') {
                card.setClickable(1);
                card.setVisible(1);
            } else {
                if (color == '-') {
                    if (cars > 0) {
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