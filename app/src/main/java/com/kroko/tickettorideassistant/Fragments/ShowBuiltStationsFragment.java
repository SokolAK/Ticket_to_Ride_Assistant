package com.kroko.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Logic.Player;
import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.Logic.Route;
import com.kroko.TicketToRideAssistant.Logic.TtRA_Application;
import com.kroko.TicketToRideAssistant.UI.CustomSpinnerAdapter;
import com.kroko.TicketToRideAssistant.UI.CustomSpinnerItem;

import java.util.ArrayList;
import java.util.Collections;

public class ShowBuiltStationsFragment extends Fragment {
    private boolean unlockDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_show_built_stations, container, false);
        Player player = ((TtRA_Application) getActivity().getApplication()).player;
        Game game = ((TtRA_Application) getActivity().getApplication()).game;


        ArrayList<CustomSpinnerItem> stationList = new ArrayList<>();
        for(Route route: player.getBuiltStations()) {
            stationList.add(new CustomSpinnerItem(route.toString(), route.getImageId(game,route.getBuiltStationColor()), route.getId()));
        }
        Collections.sort(stationList, (x, y) -> x.compareTo(y));

        ListView listStations = drawer.findViewById(R.id.list_stations);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), stationList);
        listStations.setAdapter(adapter);

        listStations.setOnItemLongClickListener((arg0, arg1, position, id) -> {
            if(unlockDelete) {
                Route route = player.getBuiltStations().get(position);

                player.addCards(route.getBuiltStationCardsNumber());
                //player.addPoints(game.getStationPoints());
                player.removeRouteStation(position);
                for (Route rout : game.getRoutes(route.getCity1(), route.getCity2(),false,false)) {
                    rout.setBuiltStation(false);
                }
                //player.addStation(1);

                stationList.remove(position);
                player.checkIfTicketsRealized();

                adapter.notifyDataSetChanged();
            }
            return true;
        });

        Switch switchControl = drawer.findViewById(R.id.switch_delete);
        if (switchControl != null) {
            switchControl.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    unlockDelete = true;
                    switchControl.setText(R.string.unlocked);
                    switchControl.setTextColor(getResources().getColor(R.color.cardsUnlocked));
                    TextView deleteComment = drawer.findViewById(R.id.delete_route_comment);
                    deleteComment.setVisibility(View.VISIBLE);
                } else {
                    unlockDelete = false;
                    switchControl.setText(R.string.locked);
                    switchControl.setTextColor(getResources().getColor(R.color.cardsLocked));
                    TextView deleteComment = drawer.findViewById(R.id.delete_route_comment);
                    deleteComment.setVisibility(View.INVISIBLE);
                }
            });
        }

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_showStations);

        return drawer;
    }

}