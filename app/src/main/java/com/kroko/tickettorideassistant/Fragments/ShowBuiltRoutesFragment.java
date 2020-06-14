package com.kroko.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Logic.Player;
import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.Logic.Route;
import com.kroko.TicketToRideAssistant.Logic.TtRA_Application;
import com.kroko.TicketToRideAssistant.UI.CustomItemAdapter;
import com.kroko.TicketToRideAssistant.UI.CustomItem;

import java.util.ArrayList;

public class ShowBuiltRoutesFragment extends Fragment implements View.OnClickListener {
    private boolean unlockDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_show_built_routes, container, false);
        Player player = ((TtRA_Application) requireActivity().getApplication()).player;
        Game game = ((TtRA_Application) requireActivity().getApplication()).game;


        ArrayList<CustomItem> routeList = new ArrayList<>();
        for(Route route: player.getBuiltRoutes()) {
            routeList.add(new CustomItem(route.toString(), route.getImageId(game,route.getBuiltColor()), route.getId()));
        }

        ListView listRoutes = drawer.findViewById(R.id.list_routes);
        CustomItemAdapter adapter = new CustomItemAdapter(getContext(), routeList);
        listRoutes.setAdapter(adapter);

        listRoutes.setOnItemLongClickListener((arg0, arg1, position, id) -> {
            if(unlockDelete) {
                Route route = player.getBuiltRoutes().get(position);

                player.addCards(route.getBuiltCardsNumber());
                player.addCars(route.getLength());
                //player.spendPoints(game.getScoring().get(route.getLength()));
                player.removeRoute(position);
                game.getRoute(route.getId()).setBuilt(false);

                routeList.remove(position);

                adapter.notifyDataSetChanged();
            }
            return true;
        });

        Switch switchControl = drawer.findViewById(R.id.switch_delete);
        if (switchControl != null) {
            switchControl.setChecked(false);
            switchControl.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    unlockDelete = true;
                    switchControl.setText(R.string.unlocked);
                    switchControl.setTextColor(getResources().getColor(R.color.cardsUnlocked));
                    TextView deleteComment = drawer.findViewById(R.id.delete_comment);
                    deleteComment.setVisibility(View.VISIBLE);
                } else {
                    unlockDelete = false;
                    switchControl.setText(R.string.locked);
                    switchControl.setTextColor(getResources().getColor(R.color.cardsLocked));
                    TextView deleteComment = drawer.findViewById(R.id.delete_comment);
                    deleteComment.setVisibility(View.INVISIBLE);
                }
            });
        }

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_showRoutes);

        Button buttonNewRoute = drawer.findViewById(R.id.new_route);
        buttonNewRoute.setOnClickListener(this);

        return drawer;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_route:
                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new BuildRouteFragment());
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }
}
