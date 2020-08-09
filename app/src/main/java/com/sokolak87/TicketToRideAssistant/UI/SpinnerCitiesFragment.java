package com.sokolak87.TicketToRideAssistant.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.sokolak87.TicketToRideAssistant.Logic.Game;
import com.sokolak87.TicketToRideAssistant.Logic.Player;
import com.sokolak87.TicketToRideAssistant.Logic.Route;
import com.sokolak87.TicketToRideAssistant.Logic.TtRA_Application;
import com.sokolak87.TicketToRideAssistant.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpinnerCitiesFragment extends Fragment {
    private Game game;
    private Player player;
    private List<String> citiesSorted = new ArrayList<>();
    private Spinner spinner1;
    private Spinner spinner2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer =  inflater.inflate(R.layout.custom_spinner, container, false);

        game = ((TtRA_Application) requireActivity().getApplication()).game;
        player = ((TtRA_Application) requireActivity().getApplication()).player;

        populateCities();
        manageSpinner1(drawer);
        manageSpinner2(drawer);

        return drawer;
    }

    private void populateCities() {
        Set<String> cities = new HashSet<>();
        for (Route route: game.getRoutes()) {
            cities.add(route.getCity1());
            cities.add(route.getCity2());
        }
        citiesSorted = new ArrayList<>(cities);
        Collections.sort(citiesSorted, String::compareTo);
    }

    private void manageSpinner1(View drawer) {
        spinner1 = drawer.findViewById(R.id.spinner1);
        List<CustomItem> cityList1 = new ArrayList<>();
        for (String city1: citiesSorted) {
            cityList1.add(new CustomItem(city1, 0, 0));
        }
        CustomItemAdapter adapter = new CustomItemAdapter(getContext(), cityList1);
        spinner1.setAdapter(adapter);
    }

    public void manageSpinner2(View drawer) {
        spinner2 = drawer.findViewById(R.id.spinner2);
        spinner1.setOnItemSelectedListener(new SpinnerCitiesFragment.listenerCity1(drawer));
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

            List<String> citiesSorted2 = new ArrayList<>(citiesSorted);
            citiesSorted2.remove(city1);
            List<CustomItem> cityList2 = new ArrayList<>();
            for (String city2: citiesSorted2) {
                cityList2.add(new CustomItem(city2, 0, 0));
            }

            CustomItemAdapter adapter = new CustomItemAdapter(getContext(), cityList2);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new SpinnerCitiesFragment.listenerCity2(drawer));
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
                parentFragment.onSpinnerItemSelected((CustomItem) spinner1.getSelectedItem(),(CustomItem) spinner2.getSelectedItem());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}
