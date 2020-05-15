package com.kroko.TicketToRideAssistant.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Logic.Player;
import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.Logic.TtRA_Application;
import com.kroko.TicketToRideAssistant.UI.Card;
import com.kroko.TicketToRideAssistant.UI.CardsCarFragment;

public class TopFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top, container, false);
    }

    @Override
    public void onViewCreated(View drawer, Bundle savedInstanceState) {
        Player player = ((TtRA_Application) getActivity().getApplication()).player;
        TextView points = drawer.findViewById(R.id.points_value);
        points.setText(String.valueOf(player.getPoints()));
        TextView cars = drawer.findViewById(R.id.cars_value);
        cars.setText(String.valueOf(player.getCars()));
        TextView stations = drawer.findViewById(R.id.stations_value);
        stations.setText(String.valueOf(player.getStations()));

        Game game = ((TtRA_Application) getActivity().getApplication()).game;
        for (Card card : game.getCards()) {
            card.setClickable(1);
            card.setVisible(1);
        }

        Switch switchControl = drawer.findViewById(R.id.switch_control);
        if (switchControl != null) {
            switchControl.setOnCheckedChangeListener((buttonView, isChecked) -> {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                CardsCarFragment cardsCarFragment = new CardsCarFragment(player.getCardsNumbers());
                if(isChecked) {
                    cardsCarFragment.setActive(true);
                    cardsCarFragment.setActiveLong(true);
                    FrameLayout cardsContainer = drawer.findViewById(R.id.cards_container);
                    cardsContainer.setBackgroundResource(R.color.cardsUnlocked);
                } else {
                    cardsCarFragment.setActive(false);
                    cardsCarFragment.setActiveLong(false);
                    FrameLayout cardsContainer = drawer.findViewById(R.id.cards_container);
                    cardsContainer.setBackgroundResource(R.color.colorBackDark);
                }
                ft.replace(R.id.cards_container, cardsCarFragment);
                ft.addToBackStack(null);
                ft.commit();
            });
        }

        switchControl.setChecked(true);
        switchControl.setChecked(false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_top);
    }
}
