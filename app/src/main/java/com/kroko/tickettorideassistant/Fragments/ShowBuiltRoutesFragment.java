package com.kroko.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Logic.Player;
import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.Logic.Route;
import com.kroko.TicketToRideAssistant.Logic.TtRA_Application;
import com.kroko.TicketToRideAssistant.UI.CustomSpinnerAdapter;
import com.kroko.TicketToRideAssistant.UI.CustomSpinnerItem;

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
            routeList.add(new CustomSpinnerItem(route.toString(), route.getImageId(game,route.getBuiltColor()), route.get_id()));
        }
        Collections.sort(routeList, (x, y) -> x.compareTo(y));

        ListView listRoutes = drawer.findViewById(R.id.list_routes);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), routeList);
        listRoutes.setAdapter(adapter);

        listRoutes.setOnItemLongClickListener((arg0, arg1, position, id) -> {
            Route route = player.getBuiltRoutes().get(position);

            player.addCards(route.getBuiltCardsNumber());
            player.addCars(route.getLength());
            player.spendPoints(game.getScoring().get(route.getLength()));
            player.removeRoute(position);
            game.getRoute(route.get_id()).setBuilt(false);

            routeList.remove(position);
            adapter.notifyDataSetChanged();
            return true;
        });

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_top);

        return drawer;
    }
}
