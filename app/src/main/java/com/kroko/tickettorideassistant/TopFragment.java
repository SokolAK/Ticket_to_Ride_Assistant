package com.kroko.TicketToRideAssistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
        Player player = ((TtRA_Application) getActivity().getApplication()).player;

        TextView points = drawer.findViewById(R.id.points_value);
        points.setText(String.valueOf(player.getPoints()));
        TextView cars = drawer.findViewById(R.id.cars_value);
        cars.setText(String.valueOf(player.getCars()));
        TextView stations = drawer.findViewById(R.id.stations_value);
        stations.setText(String.valueOf(player.getStations()));

        //maxCards = ((TtRA_Application) getActivity().getApplication()).game.getMaxNoOfCardsToDraw();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.cards_container, new CardsFragment(player.getCards()));
        ft.addToBackStack(null);
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_top);
    }
}
