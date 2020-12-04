package pl.sokolak.TicketToRideAssistant.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardTile;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardsController;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardsFragment;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardsObserver;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.DrawCarCardsController;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.ShowCarCardsController;
import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.Domain.Ticket;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.TtRA_Application;

public class TopFragment extends Fragment implements CarCardsObserver {
    private Player player;
    private Game game;
    private View view;
    private SwitchCompat switchControl;
    private List<CarCardTile> carCardTiles = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top, container, false);
        player = ((TtRA_Application) requireActivity().getApplication()).player;
        game = ((TtRA_Application) requireActivity().getApplication()).game;

        switchControl = view.findViewById(R.id.switch_control);
        TextView points = view.findViewById(R.id.points_value);
        points.setText(String.valueOf(player.getPoints()));
        TextView cars = view.findViewById(R.id.cars_value);
        cars.setText(String.valueOf(player.getNumberOfCars()));
        TextView stations = view.findViewById(R.id.stations_value);
        stations.setText(String.valueOf(player.getNumberOfStations()));

        int realizedTicketsNumber = 0;
        int unrealizedTicketsNumber = 0;
        for (Ticket ticket : player.getTickets()) {
            if (ticket.isRealized()) {
                realizedTicketsNumber++;
            } else {
                unrealizedTicketsNumber++;
            }
        }

        TextView realizedTickets = view.findViewById(R.id.realized_tickets_value);
        realizedTickets.setText(String.valueOf(realizedTicketsNumber));
        TextView unrealizedTickets = view.findViewById(R.id.unrealized_tickets_value);
        unrealizedTickets.setText(String.valueOf(unrealizedTicketsNumber));

        TextView longestPath = view.findViewById(R.id.longest_path_value);
        longestPath.setText(String.valueOf(player.getLongestPathLength()));

        switchControl.setChecked(false);
        switchControl.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            if (isChecked) {
                drawCarCardsUnlocked(carCardTiles);
            } else {
                drawCarCardsLocked(carCardTiles);
            }
            ft.commit();
        });

        drawCarCardsLocked(carCardTiles);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_top);

        return view;
    }

    private void updateCarCardsFragment(List<CarCardTile> carCardTiles) {
        if (switchControl.isChecked()) {
            drawCarCardsUnlocked(carCardTiles);
        } else {
            drawCarCardsLocked(carCardTiles);
        }
    }

    private void drawCarCardsUnlocked(List<CarCardTile> carCardTiles) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CarCardsController carCardsController = new DrawCarCardsController();
        carCardsController.setGame(game);
        CarCardsFragment carCardsFragment = new CarCardsFragment(carCardTiles, carCardsController);
        carCardsFragment.getCarCardsObservers().add(player);
        carCardsFragment.getCarCardsObservers().add(this);
        FrameLayout cardsContainer = view.findViewById(R.id.cards_container);
        cardsContainer.setBackgroundResource(R.color.cardsUnlocked);
        switchControl.setText(R.string.cards_unlocked);
        switchControl.setTextColor(getResources().getColor(R.color.cardsUnlocked));
        ft.replace(R.id.cards_container, carCardsFragment);
        ft.commit();
    }

    private void drawCarCardsLocked(List<CarCardTile> carCardTiles) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CarCardsController carCardsController = new ShowCarCardsController();
        carCardsController.setPlayer(player);
        CarCardsFragment carCardsFragment = new CarCardsFragment(carCardTiles, carCardsController);
        carCardsFragment.getCarCardsObservers().add(player);
        carCardsFragment.getCarCardsObservers().add(this);
        FrameLayout cardsContainer = view.findViewById(R.id.cards_container);
        cardsContainer.setBackgroundResource(R.color.colorBackDark);
        switchControl.setText(R.string.cards_locked);
        switchControl.setTextColor(getResources().getColor(R.color.cardsLocked));
        ft.replace(R.id.cards_container, carCardsFragment);
        ft.commit();
    }

    @Override
    public void updateCarCards(List<CarCardTile> carCardTiles) {
        updateCarCardsFragment(carCardTiles);
    }
}
