package pl.sokolak.TicketToRideAssistant.Fragments;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardTile;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardsController;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardsFragment;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardsObserver;
import pl.sokolak.TicketToRideAssistant.Domain.CarCardColor;
import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Domain.Route;
import pl.sokolak.TicketToRideAssistant.TtRA_Application;
import pl.sokolak.TicketToRideAssistant.UI.TextImageItem;
import pl.sokolak.TicketToRideAssistant.UI.SpinnerRouteFragment;
import pl.sokolak.TicketToRideAssistant.UI.SpinnerListenerInterface;

import static pl.sokolak.TicketToRideAssistant.Util.carCardsConverter.carCardsTilesToCarCards;

public class BuildRouteFragment extends Fragment implements View.OnClickListener, SpinnerListenerInterface, CarCardsObserver {
    private Game game;
    private Player player;
    private Route route;
    int cars;
    private View view;
    private List<CarCardTile> carCardTiles;
    private CarCardsController carCardsController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        game = ((TtRA_Application) requireActivity().getApplication()).game;
        player = ((TtRA_Application) requireActivity().getApplication()).player;

        view = inflater.inflate(R.layout.fragment_build_route, container, false);

        ImageView acceptIcon = view.findViewById(R.id.accept_button);
        acceptIcon.setOnClickListener(this);
        ImageView resetIcon = view.findViewById(R.id.reset_button);
        resetIcon.setOnClickListener(this);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        SpinnerRouteFragment spinnerRouteFragment = new SpinnerRouteFragment('R', getContext(), this);
        ft.replace(R.id.spinners_container, spinnerRouteFragment);
        ft.commit();

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_buildRoute);

        carCardsController = game.getRouteCarCardController();
        carCardsController.setPlayer(player);
        carCardsController.setGame(game);
        carCardTiles = new ArrayList<>();
        for (CarCardColor card : game.getCarCardColors()) {
            carCardTiles.add(new CarCardTile(card, 0, true, true, false));
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.accept_button:
                if (carCardsController.isConditionMet(getContext(), carCardTiles)) {
                    CarCardColor builtColor = CarCardColor.LOCO;
                    for (CarCardTile carCardTile : carCardTiles) {
                        CarCardColor key = carCardTile.getCarCardColor();
                        if (carCardTile.getAmount() > 0) {
                            builtColor = key;
                            break;
                        }
                    }
                    route.setBuilt(true);
                    route.setBuiltColor(builtColor);
                    route.setBuiltCarCards(carCardsTilesToCarCards(carCardTiles));
                    player.addRoute(route);
                    player.spendCards(route.getBuiltCarCards());
                    player.spendCars(route.getLength());

                    returnToTopPage();
                }
                break;

            case R.id.reset_button:
                clearCards();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSpinnerItemSelected(TextImageItem... items) {
        if (items.length == 1) {
            int routeId = items[0].getItemId();
            route = game.getRoute(routeId);
            cars = route.getLength() - route.getLocos();

            if (cars > 0) {
                view.findViewById(R.id.car_icon).setVisibility(View.VISIBLE);
                view.findViewById(R.id.car_number).setVisibility(View.VISIBLE);
                TextView carText = view.findViewById(R.id.car_number);
                carText.setText(" " + cars);
            } else {
                view.findViewById(R.id.car_icon).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.car_number).setVisibility(View.INVISIBLE);
            }
            if (route.getLocos() > 0) {
                view.findViewById(R.id.loco_icon).setVisibility(View.VISIBLE);
                view.findViewById(R.id.loco_number).setVisibility(View.VISIBLE);
                TextView locoText = view.findViewById(R.id.loco_number);
                locoText.setText(" " + route.getLocos());
            } else {
                view.findViewById(R.id.loco_icon).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.loco_number).setVisibility(View.INVISIBLE);
            }
            if (route.isTunnel()) {
                view.findViewById(R.id.tunnel_icon).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.tunnel_icon).setVisibility(View.INVISIBLE);
            }

            TextView lengthText = view.findViewById(R.id.length_value);
            lengthText.setText(" " + route.getLength());
            TextView pointsText = view.findViewById(R.id.points_value);
            pointsText.setText(" " + game.getScoring().get(route.getLength()));

            clearCards();
        }
    }

    private void updateCarCardsFragment(List<CarCardTile> carCardTiles) {
        carCardsController.setRoute(route);
        carCardsController.setCardsVisibility(carCardTiles, route);
        CarCardsFragment carCardsFragment = new CarCardsFragment(carCardTiles, carCardsController);
        carCardsFragment.getCarCardsObservers().add(this);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.cards_container, carCardsFragment);
        ft.commit();
    }

    private void clearCards() {
        carCardTiles = new ArrayList<>();
        for (CarCardColor card : game.getCarCardColors()) {
            carCardTiles.add(new CarCardTile(card, 0, true, true, true));
        }
        updateCarCardsFragment(carCardTiles);
    }


    @Override
    public void updateCarCards(List<CarCardTile> carCardTiles) {
        updateCarCardsFragment(carCardTiles);
    }

    private void returnToTopPage() {
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new ShowBuiltRoutesFragment());
        ft.commit();
    }
}