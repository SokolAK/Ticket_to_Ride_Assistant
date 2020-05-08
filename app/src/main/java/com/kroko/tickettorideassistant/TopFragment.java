package com.kroko.TicketToRideAssistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class TopFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top, container, false);
    }

    @Override
    public void onViewCreated(View drawer, Bundle savedInstanceState) {
        RecyclerView cardRecycler = drawer.findViewById(R.id.cards);

        Player player = ((TtRA_Application) getActivity().getApplication()).player;

        TextView points = drawer.findViewById(R.id.points_value);
        points.setText(String.valueOf(player.getPoints()));
        TextView cars = drawer.findViewById(R.id.cars_value);
        cars.setText(String.valueOf(player.getCars()));
        TextView stations = drawer.findViewById(R.id.stations_value);
        stations.setText(String.valueOf(player.getStations()));

        int[] cardImages = new int[9];
        int[] cardNumbers = new int[9];
        for (int i = 0; i < cardImages.length; i++) {
            cardImages[i] = Card.cards[i].getImageResourceId();
            String color = Card.cards[i].getName();
            cardNumbers[i] = player.getCards().get(color);
        }

        CardImageAdapter adapter = new CardImageAdapter(cardImages, cardNumbers);
        cardRecycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
        cardRecycler.setLayoutManager(layoutManager);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_top);
    }
}
