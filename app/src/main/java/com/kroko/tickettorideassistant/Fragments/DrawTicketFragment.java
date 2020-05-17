package com.kroko.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Logic.Player;
import com.kroko.TicketToRideAssistant.Logic.Ticket;
import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.Logic.Route;
import com.kroko.TicketToRideAssistant.Logic.TtRA_Application;
import com.kroko.TicketToRideAssistant.UI.Card;
import com.kroko.TicketToRideAssistant.UI.CardsCarFragment;
import com.kroko.TicketToRideAssistant.UI.CustomSpinnerAdapter;
import com.kroko.TicketToRideAssistant.UI.CustomSpinnerItem;

import java.util.ArrayList;
import java.util.Collections;

public class DrawTicketFragment extends Fragment implements View.OnClickListener {
    private Game game;
    private Player player;
    private Ticket ticket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        game = ((TtRA_Application) getActivity().getApplication()).game;
        player = ((TtRA_Application) getActivity().getApplication()).player;

        View drawer = inflater.inflate(R.layout.fragment_draw_ticket, container, false);

        ImageView acceptIcon = drawer.findViewById(R.id.accept_icon);
        acceptIcon.setOnClickListener(this);

        manageSpinner1(drawer);
        manageSpinner2(drawer);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_drawTicket);

        return drawer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_icon:
                player.addTicket(new Ticket(ticket));
                returnToTopPage();
                break;
        }
    }



    private void returnToTopPage() {
        ((MainActivity) getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void manageSpinner1(View drawer) {
        ArrayList<String> cities1 = new ArrayList<>();
        for (Ticket ticket : game.getTickets()) {

            boolean flag = true;
            for (String city : cities1) {
                if (city.equals(ticket.getCity1())) {
                    flag = false;
                }
            }
            if (flag) {
                cities1.add(ticket.getCity1());
            }
            flag = true;
            for (String city : cities1) {
                if (city.equals(ticket.getCity2())) {
                    flag = false;
                }
            }
            if (flag) {
                cities1.add(ticket.getCity2());
            }
        }
        Collections.sort(cities1, (x, y) -> x.compareTo(y));

        ArrayList<CustomSpinnerItem> cityList = new ArrayList<>();
        for (String city1: cities1) {
            cityList.add(new CustomSpinnerItem(city1, 0, 0));
        }
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), cityList);
        Spinner spinner = drawer.findViewById(R.id.spinner_city1);
        spinner.setAdapter(adapter);
    }

    public void manageSpinner2(View drawer) {
        Spinner listCity1 = drawer.findViewById(R.id.spinner_city1);
        listCity1.setOnItemSelectedListener(new listenerCity1(drawer));
    }

    private class listenerCity1 implements AdapterView.OnItemSelectedListener {
        private View drawer;

        private listenerCity1(View drawer) {
            this.drawer = drawer;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view,
                                   int position, long id) {

            Spinner listCity1 = drawer.findViewById(R.id.spinner_city1);
            String city1 = ((CustomSpinnerItem) listCity1.getSelectedItem()).getText();
            ArrayList<Ticket> tickets = game.getTickets(city1);

            ArrayList<CustomSpinnerItem> cityList = new ArrayList<>();
            for (Ticket ticket: tickets) {
                String city2;
                if (city1.equals(ticket.getCity1())) {
                    city2 = ticket.getCity2();
                } else {
                    city2 = ticket.getCity1();
                }
                cityList.add(new CustomSpinnerItem(city2, 0, ticket.getId()));
            }

            Collections.sort(cityList, (x, y) -> x.compareTo(y));

            Spinner spinner = drawer.findViewById(R.id.spinner_city2);
            CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), cityList);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new listenerCity2(drawer));
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private class listenerCity2 implements AdapterView.OnItemSelectedListener {
        private View drawer;

        private listenerCity2(View drawer) {
            this.drawer = drawer;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner spinner2 = drawer.findViewById(R.id.spinner_city2);
            int ticketId = ((CustomSpinnerItem) spinner2.getSelectedItem()).getItemId();
            ticket = game.getTicket(ticketId);
            TextView pointsValue = drawer.findViewById(R.id.points_value);
            pointsValue.setText(String.valueOf(ticket.getPoints()));

            ticket.checkIfRealized(player);
            TextView realizedValue = drawer.findViewById(R.id.realized_value);
            realizedValue.setText(String.valueOf(ticket.isRealized()));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}