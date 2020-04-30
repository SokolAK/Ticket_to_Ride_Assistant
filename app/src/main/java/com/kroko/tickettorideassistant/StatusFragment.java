package com.kroko.tickettorideassistant;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

public class StatusFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(View drawer, Bundle savedInstanceState) {
        RecyclerView cardRecycler = drawer.findViewById(R.id.cards);

        Player player = ((TtRA_Application) getActivity().getApplication()).player;

        TextView points = drawer.findViewById(R.id.points_value);
        points.setText(String.valueOf(player.getPoints()));
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


        adapter.setListener(position -> {
            String color = Card.cards[position].getName();
            int numberOfCards = player.getCards().get(color);
            player.getCards().put(color, numberOfCards+1);
            player.setStations(player.getStations()+1);

            ViewPager pager = getActivity().findViewById(R.id.pager);
            pager.getAdapter().notifyDataSetChanged();
        });
    }

    public void onResume() {
        super.onResume();

    }
}