package com.sokolak87.TicketToRideAssistant.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.sokolak87.TicketToRideAssistant.Logic.Game;
import com.sokolak87.TicketToRideAssistant.Logic.Ticket;
import com.sokolak87.TicketToRideAssistant.Logic.TtRA_Application;
import com.sokolak87.TicketToRideAssistant.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpinnerTicketFragment extends Fragment {
    private Game game;
    //private Player player;
    private Spinner spinner1;
    private Spinner spinner2;
    private String defaultItem1;
    private String defaultItem2;

    public SpinnerTicketFragment() {
    }
    public SpinnerTicketFragment(String item1, String item2) {
        defaultItem1 = item1;
        defaultItem2 = item2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer =  inflater.inflate(R.layout.custom_spinner, container, false);

        game = ((TtRA_Application) requireActivity().getApplication()).game;
        //player = ((TtRA_Application) requireActivity().getApplication()).player;

        spinner1 = drawer.findViewById(R.id.spinner1);
        spinner2 = drawer.findViewById(R.id.spinner2);
        manageSpinner1(drawer);
        manageSpinner2(drawer);

        return drawer;
    }

    private void manageSpinner1(View drawer) {
        List<String> cities1 = new ArrayList<>();
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

        List<CustomItem> cityList = new ArrayList<>();
        int pos = 0;
        int defaultPos = 0;
        for (String city1: cities1) {
            cityList.add(new CustomItem(city1, 0, 0));
            if(city1.equals(defaultItem1)) {
                defaultPos = pos;
            }
            pos++;
        }
        CustomItemAdapter adapter = new CustomItemAdapter(getContext(), cityList);
        spinner1.setAdapter(adapter);

        spinner1.setSelection(defaultPos);
    }

    public void manageSpinner2(View drawer) {
        spinner1.setOnItemSelectedListener(new SpinnerTicketFragment.listenerCity1(drawer));
    }

    private class listenerCity1 implements AdapterView.OnItemSelectedListener {
        private View drawer;

        private listenerCity1(View drawer) {
            this.drawer = drawer;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view,
                                   int position, long id) {

            String city1 = ((CustomItem) spinner1.getSelectedItem()).getText();
            List<Ticket> tickets = game.getTickets(city1);

            List<CustomItem> cityList = new ArrayList<>();
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

            Collections.sort(cityList, CustomItem::compareTo);
            int defaultPos = 0;
            for(int i = 0; i<cityList.size(); ++i) {
                if(cityList.get(i).getText().equals(defaultItem2)) {
                    defaultPos = i;
                    break;
                }
            }

            CustomItemAdapter adapter = new CustomItemAdapter(getContext(), cityList);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new SpinnerTicketFragment.listenerCity2(drawer));

            spinner2.setSelection(defaultPos);
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
            SpinnerListenerInterface parentFragment = (SpinnerListenerInterface) getParentFragment();
            if(parentFragment != null) {
                parentFragment.onSpinnerItemSelected((CustomItem) spinner2.getSelectedItem());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}
