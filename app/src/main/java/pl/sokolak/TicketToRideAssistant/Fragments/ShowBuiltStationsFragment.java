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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import pl.sokolak.TicketToRideAssistant.Logic.Game;
import pl.sokolak.TicketToRideAssistant.Logic.Player;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Logic.Route;
import pl.sokolak.TicketToRideAssistant.Logic.TtRA_Application;
import pl.sokolak.TicketToRideAssistant.UI.CustomItemAdapter;
import pl.sokolak.TicketToRideAssistant.UI.CustomItem;

import java.util.ArrayList;
import java.util.List;

import static pl.sokolak.TicketToRideAssistant.Util.DimensionUtils.getDimension;

public class ShowBuiltStationsFragment extends Fragment implements View.OnClickListener {
    private boolean unlockDelete;
    private Player player;
    private Game game;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_show_built_stations, container, false);
        player = ((TtRA_Application) requireActivity().getApplication()).player;
        game = ((TtRA_Application) requireActivity().getApplication()).game;

        List<CustomItem> stationList = new ArrayList<>();
        for(Route route: player.getBuiltStations()) {
            int textSize = getDimension(requireContext(),R.dimen.text_size_small);
            stationList.add(new CustomItem(route.toString(), route.getImageId(game,route.getBuiltStationColor()), route.getId(), textSize));
        }

        ListView listStations = drawer.findViewById(R.id.list_stations);
        CustomItemAdapter adapter = new CustomItemAdapter(getContext(), stationList);
        listStations.setAdapter(adapter);

        listStations.setOnItemLongClickListener((arg0, arg1, position, id) -> {
            if(unlockDelete) {
                Route route = player.getBuiltStations().get(position);

                player.addCards(route.getBuiltStationCardsNumber());
                player.removeRouteStation(position);
                for (Route rout : game.getRoutes(route.getCity1(), route.getCity2(),false,false)) {
                    rout.setBuiltStation(false);
                }

                stationList.remove(position);

                adapter.notifyDataSetChanged();
            }
            return true;
        });

        SwitchCompat switchControl = drawer.findViewById(R.id.switch_delete);
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
        toolbar.setTitle(R.string.nav_showStations);

        Button buttonNewStation = drawer.findViewById(R.id.new_station);
        buttonNewStation.setOnClickListener(this);

        return drawer;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_station:
                if(player.getNumberOfStations() > 0) {
                    FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new BuildStationFragment());
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else {
                    Toast.makeText(getContext(), R.string.no__more_stations, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
