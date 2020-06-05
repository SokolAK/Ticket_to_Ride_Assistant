package com.kroko.TicketToRideAssistant.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Logic.Player;
import com.kroko.TicketToRideAssistant.Logic.Ticket;
import com.kroko.TicketToRideAssistant.Logic.TtRA_Application;
import com.kroko.TicketToRideAssistant.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpinnerTicketFragment extends Fragment {
    private Game game;
    private Player player;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer =  inflater.inflate(R.layout.fragment_spinner, container, false);

        game = ((TtRA_Application) getActivity().getApplication()).game;
        player = ((TtRA_Application) getActivity().getApplication()).player;

        manageSpinner1(drawer);
        manageSpinner2(drawer);

        return drawer;
    }

    private void manageSpinner1(View drawer) {
        ArrayList<String> cities1 = new ArrayList<>();
        for (Ticket ticket : game.getTickets()) {
            if (!ticket.isInHand()) {
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
        }
        Collections.sort(cities1, (x, y) -> x.compareTo(y));

        ArrayList<CustomItem> cityList = new ArrayList<>();
        for (String city1: cities1) {
            cityList.add(new CustomItem(city1, 0, 0));
        }
        CustomItemAdapter adapter = new CustomItemAdapter(getContext(), cityList);
        Spinner spinner = drawer.findViewById(R.id.spinner1);
        spinner.setAdapter(adapter);
    }

    public void manageSpinner2(View drawer) {
        Spinner listCity1 = drawer.findViewById(R.id.spinner1);
        listCity1.setOnItemSelectedListener(new SpinnerTicketFragment.listenerCity1(drawer));
    }

    private class listenerCity1 implements AdapterView.OnItemSelectedListener {
        private View drawer;

        private listenerCity1(View drawer) {
            this.drawer = drawer;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view,
                                   int position, long id) {

            Spinner listCity1 = drawer.findViewById(R.id.spinner1);
            String city1 = ((CustomItem) listCity1.getSelectedItem()).getText();
            ArrayList<Ticket> tickets = game.getTickets(city1);

            ArrayList<CustomItem> cityList = new ArrayList<>();
            for (Ticket ticket: tickets) {
                if (!ticket.isInHand()) {
                    String city2;
                    if (city1.equals(ticket.getCity1())) {
                        city2 = ticket.getCity2();
                    } else {
                        city2 = ticket.getCity1();
                    }
                    cityList.add(new CustomItem(city2, 0, ticket.getId()));
                }
            }

            Collections.sort(cityList, (x, y) -> x.compareTo(y));

            Spinner spinner = drawer.findViewById(R.id.spinner2);
            CustomItemAdapter adapter = new CustomItemAdapter(getContext(), cityList);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new SpinnerTicketFragment.listenerCity2(drawer));
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
            Spinner spinner2 = drawer.findViewById(R.id.spinner2);
            SpinnerListenerInterface parentFragment = (SpinnerListenerInterface) getParentFragment();
            parentFragment.onSpinnerItemSelected((CustomItem) spinner2.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}
