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

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.Domain.Ticket;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.TtRA_Application;
import pl.sokolak.TicketToRideAssistant.UI.Card;
import pl.sokolak.TicketToRideAssistant.UI.CardsCarFragment;

public class TopFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_top, container, false);
        Player player = ((TtRA_Application) requireActivity().getApplication()).player;
        player.updatePoints();
        TextView points = drawer.findViewById(R.id.points_value);
        points.setText(String.valueOf(player.getPoints()));
        TextView cars = drawer.findViewById(R.id.cars_value);
        cars.setText(String.valueOf(player.getNumberOfCars()));
        TextView stations = drawer.findViewById(R.id.stations_value);
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

        TextView realizedTickets = drawer.findViewById(R.id.realized_tickets_value);
        realizedTickets.setText(String.valueOf(realizedTicketsNumber));
        TextView unrealizedTickets = drawer.findViewById(R.id.unrealized_tickets_value);
        unrealizedTickets.setText(String.valueOf(unrealizedTicketsNumber));

        TextView longestPath = drawer.findViewById(R.id.longest_path_value);
        longestPath.setText(String.valueOf(player.getLongestPathLength()));

        Game game = ((TtRA_Application) requireActivity().getApplication()).game;
        for (Card card : game.getCards()) {
            card.setClickable(1);
            card.setVisible(1);
        }

        SwitchCompat switchControl = drawer.findViewById(R.id.switch_control);
        if (switchControl != null) {
            switchControl.setChecked(false);
            switchControl.setOnCheckedChangeListener((buttonView, isChecked) -> {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                if (isChecked) {
                    CardsCarFragment cardsCarFragment = CardsCarFragment.builder().cardsNumbers(player.getCardsNumbers()).
                            active(true).activeLong(true).
                            build();

                    FrameLayout cardsContainer = drawer.findViewById(R.id.cards_container);
                    cardsContainer.setBackgroundResource(R.color.cardsUnlocked);
                    switchControl.setText(R.string.cards_unlocked);
                    switchControl.setTextColor(getResources().getColor(R.color.cardsUnlocked));
                    ft.replace(R.id.cards_container, cardsCarFragment);
                } else {
                    CardsCarFragment cardsCarFragment = CardsCarFragment.builder().cardsNumbers(player.getCardsNumbers()).build();

                    FrameLayout cardsContainer = drawer.findViewById(R.id.cards_container);
                    cardsContainer.setBackgroundResource(R.color.colorBackDark);
                    switchControl.setText(R.string.cards_locked);
                    switchControl.setTextColor(getResources().getColor(R.color.cardsLocked));
                    ft.replace(R.id.cards_container, cardsCarFragment);
                }
                ft.commit();
            });
        }

        switchControl.setChecked(true);
        switchControl.setChecked(false);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_top);

        return drawer;
    }
}
