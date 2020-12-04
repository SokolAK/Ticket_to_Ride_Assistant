package pl.sokolak.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import pl.sokolak.TicketToRideAssistant.UI.SpinnerListenerInterface;
import pl.sokolak.TicketToRideAssistant.UI.SpinnerRouteFragment;

import static pl.sokolak.TicketToRideAssistant.Util.carCardsConverter.carCardsTilesToCarCards;

public class BuildStationFragment extends Fragment implements View.OnClickListener, SpinnerListenerInterface, CarCardsObserver {
    private Game game;
    private Player player;
    private Route route;
    private View view;
    private List<CarCardTile> carCardTiles = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        game = ((TtRA_Application) requireActivity().getApplication()).game;
        player = ((TtRA_Application) requireActivity().getApplication()).player;

        view = inflater.inflate(R.layout.fragment_build_station, container, false);
        ImageView acceptIcon = view.findViewById(R.id.accept_button);
        acceptIcon.setOnClickListener(this);
        ImageView resetIcon = view.findViewById(R.id.reset_button);
        resetIcon.setOnClickListener(this);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        SpinnerRouteFragment spinnerRouteFragment = new SpinnerRouteFragment('S', getContext(), this);
        ft.replace(R.id.spinners_container, spinnerRouteFragment);
        ft.commit();

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_buildStation);

        clearCards();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.accept_button:
                if (game.getStationCarCardController().isConditionMet(getContext(), carCardTiles)) {
                    CarCardColor builtColor = CarCardColor.LOCO;
                    for (CarCardTile carCardTile : carCardTiles) {
                        CarCardColor key = carCardTile.getCarCardColor();
                        if (carCardTile.getAmount() > 0) {
                            builtColor = key;
                            break;
                        }
                    }
                    route.setBuiltStationColor(builtColor);
                    route.setBuiltStationCarCards(carCardsTilesToCarCards(carCardTiles));
                    player.addRouteStation(route);
                    player.spendCards(route.getBuiltStationCarCards());

                    for (Route rout : game.getRoutes(route.getCity1(), route.getCity2(), false, false)) {
                        rout.setBuiltStation(true);
                    }
                    returnToTopPage();
                }
                break;

            case R.id.reset_button:
                clearCards();
                break;
        }
    }

    private void clearCards() {
        carCardTiles = new ArrayList<>();
        for (CarCardColor card : game.getCarCardColors()) {
            carCardTiles.add(new CarCardTile(card, 0, true, true, true));
        }
        updateCarCardsFragment(carCardTiles);
    }

    @Override
    public void onSpinnerItemSelected(TextImageItem... items) {
        if (items.length == 1) {
            int routeId = items[0].getItemId();
            route = game.getRoute(routeId);
        }
    }

    private void updateCarCardsFragment(List<CarCardTile> carCardTiles) {
        CarCardsController carCardsController = game.getStationCarCardController();
        carCardsController.setPlayer(player);
        carCardsController.setGame(game);
        CarCardsFragment carCardsFragment = new CarCardsFragment(carCardTiles, carCardsController);
        carCardsFragment.getCarCardsObservers().add(this);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.cards_container, carCardsFragment);
        ft.commit();
    }

    @Override
    public void updateCarCards(List<CarCardTile> carCardTiles) {
        updateCarCardsFragment(carCardTiles);
    }

    private void returnToTopPage() {
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new ShowBuiltStationsFragment());
        ft.commit();
    }
}