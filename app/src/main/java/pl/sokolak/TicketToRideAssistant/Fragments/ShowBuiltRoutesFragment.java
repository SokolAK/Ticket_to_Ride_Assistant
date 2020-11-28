package pl.sokolak.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import pl.sokolak.TicketToRideAssistant.Logic.Game;
import pl.sokolak.TicketToRideAssistant.Logic.Player;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Logic.Route;
import pl.sokolak.TicketToRideAssistant.Logic.TtRA_Application;
import pl.sokolak.TicketToRideAssistant.UI.TextImageItemAdapter;
import pl.sokolak.TicketToRideAssistant.UI.TextImageItem;

import java.util.ArrayList;
import java.util.List;

import static pl.sokolak.TicketToRideAssistant.Util.DimensionUtils.getDimension;

public class ShowBuiltRoutesFragment extends Fragment implements View.OnClickListener {
    private boolean unlockDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_show_built_routes, container, false);
        Player player = ((TtRA_Application) requireActivity().getApplication()).player;
        Game game = ((TtRA_Application) requireActivity().getApplication()).game;

        List<TextImageItem> routeList = new ArrayList<>();
        for (Route route : player.getBuiltRoutes()) {
            String routeString = route.toString() + " ★" + game.getScoring().get(route.getLength());
            int textSize = getDimension(requireContext(),R.dimen.text_size_small);
            routeList.add(new TextImageItem(routeString, route.getImageId(game, route.getBuiltColor()), route.getId(), textSize));
        }
        ListView listRoutes = drawer.findViewById(R.id.list_routes);
        TextImageItemAdapter adapter = new TextImageItemAdapter(getContext(), routeList);
        listRoutes.setAdapter(adapter);

        listRoutes.setOnItemLongClickListener((arg0, arg1, position, id) -> {
            if (unlockDelete) {
                Route route = player.getBuiltRoutes().get(position);

                player.addCards(route.getBuiltCardsNumber());
                player.addCars(route.getLength());
                player.removeRoute(position);
                game.getRoute(route.getId()).setBuilt(false);

                routeList.remove(position);

                adapter.notifyDataSetChanged();
            }
            return true;
        });

        SwitchCompat switchControl = drawer.findViewById(R.id.switch_delete);
        if (switchControl != null) {
            switchControl.setChecked(false);
            switchControl.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
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
