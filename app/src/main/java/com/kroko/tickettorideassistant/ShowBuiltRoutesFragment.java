package com.kroko.TicketToRideAssistant;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class ShowBuiltRoutesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_show_built_routes, container, false);
        Player player = ((TtRA_Application) getActivity().getApplication()).player;
        Game game = ((TtRA_Application) getActivity().getApplication()).game;


        ArrayList<CustomSpinnerItem> routeList = new ArrayList<>();
        for(Route route: player.getBuiltRoutes()) {
            routeList.add(new CustomSpinnerItem(route.toString(), route.getImageId(game), route.get_id()));
        }
        Collections.sort(routeList, (x, y) -> x.compareTo(y));

        ListView listRoutes = drawer.findViewById(R.id.list_routes);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), routeList);
        listRoutes.setAdapter(adapter);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_top);

        return drawer;
    }
}
