package pl.sokolak.TicketToRideAssistant.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.Domain.Route;
import pl.sokolak.TicketToRideAssistant.TtRA_Application;
import pl.sokolak.TicketToRideAssistant.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static pl.sokolak.TicketToRideAssistant.Util.DimensionUtils.getDimension;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpinnerRouteFragment extends Fragment {
    private Game game;
    private Player player;
    private char type;

    public SpinnerRouteFragment(char type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.custom_spinner, container, false);

        game = ((TtRA_Application) requireActivity().getApplication()).game;
        player = ((TtRA_Application) requireActivity().getApplication()).player;

        manageSpinner1(drawer);
        manageSpinner2(drawer);

        return drawer;
    }

    private void manageSpinner1(View drawer) {
        List<String> cities1 = new ArrayList<>();
        for (Route route : game.getRoutes()) {
            boolean isAvailable = false;
            if (type == 'R') {
                isAvailable = !route.isBuilt();
            }
            if (type == 'S') {
                isAvailable = !route.isBuilt() && !route.isBuiltStation();
            }

            if (isAvailable) {
                boolean flag = true;
                for (String city : cities1) {
                    if (city.equals(route.getCity1())) {
                        flag = false;
                    }
                }
                if (flag) {
                    cities1.add(route.getCity1());
                }
                flag = true;
                for (String city : cities1) {
                    if (city.equals(route.getCity2())) {
                        flag = false;
                    }
                }
                if (flag) {
                    cities1.add(route.getCity2());
                }
            }
        }
        Collections.sort(cities1, String::compareTo);

        List<TextImageItem> cityList1 = new ArrayList<>();
        for (String city1 : cities1) {
            int textSize = getDimension(requireContext(),R.dimen.text_size_normal);
            cityList1.add(new TextImageItem(city1, 0, 0, textSize));
        }
        TextImageItemAdapter adapter = new TextImageItemAdapter(getContext(), cityList1);
        Spinner spinner = drawer.findViewById(R.id.spinner1);
        spinner.setAdapter(adapter);
    }

    public void manageSpinner2(View drawer) {
        Spinner listCity1 = drawer.findViewById(R.id.spinner1);
        listCity1.setOnItemSelectedListener(new SpinnerRouteFragment.listenerCity1(drawer));
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
            String city1 = ((TextImageItem) listCity1.getSelectedItem()).getText();
            List<Route> routes = game.getRoutes(city1, false, true);

            List<TextImageItem> cityList2 = new ArrayList<>();
            for (Route route : routes) {
                boolean isAvailable = false;
                if (type == 'R') {
                    isAvailable = !route.isBuilt();
                }
                if (type == 'S') {
                    isAvailable = !route.isBuilt() && !route.isBuiltStation();
                }

                if (isAvailable) {
                    String city2;
                    if (city1.equals(route.getCity1())) {
                        city2 = route.getCity2();
                    } else {
                        city2 = route.getCity1();
                    }
                    if (type == 'R') {
                        int textSize = getDimension(requireContext(),R.dimen.text_size_normal);
                        cityList2.add(new TextImageItem(city2, route.getImageId(game, route.getColor()), route.getId(), textSize));
                    }
                    if (type == 'S') {
                        boolean addRoute = true;
                        for (TextImageItem item : cityList2) {
                            if (item.getText().equals(city2)) {
                                addRoute = false;
                                break;
                            }
                        }
                        if (addRoute) {
                            int textSize = getDimension(requireContext(),R.dimen.text_size_normal);
                            cityList2.add(new TextImageItem(city2, 0, route.getId(), textSize));
                        }
                    }
                }
            }

            Collections.sort(cityList2, TextImageItem::compareTo);

            Spinner spinner = drawer.findViewById(R.id.spinner2);
            TextImageItemAdapter adapter = new TextImageItemAdapter(getContext(), cityList2);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new SpinnerRouteFragment.listenerCity2(drawer));
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
            parentFragment.onSpinnerItemSelected((TextImageItem) spinner2.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}
