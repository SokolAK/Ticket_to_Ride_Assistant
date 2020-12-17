package pl.sokolak.TicketToRideAssistant.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.TtRA_Application;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.UI.TextTextItem;
import pl.sokolak.TicketToRideAssistant.UI.TextTextItemAdapter;

import static pl.sokolak.TicketToRideAssistant.Util.DimensionUtils.getDimension;

public class PointsFragment extends Fragment {
    //private Game.PointsCalculator pointsCalculator;
    private Player player;
    private Game game;

    public PointsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        player = ((TtRA_Application) requireActivity().getApplication()).player;
        game = ((TtRA_Application) requireActivity().getApplication()).game;
        game.getPointsCalculator().setPlayer(player);

        View drawer = inflater.inflate(R.layout.fragment_points, container, false);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_points);

        Activity activity = requireActivity();
        ListView pointsList = drawer.findViewById(R.id.points_list);
        pointsList.setEnabled(false);
        List<TextTextItem> rows = getPointsList(activity);
        TextTextItemAdapter adapter = new TextTextItemAdapter(getContext(), rows);
        pointsList.setAdapter(adapter);

        return drawer;
    }

    public List<TextTextItem> getPointsList(Context context) {
        List<TextTextItem> points = new ArrayList<>();
        int textSize = getDimension(context, R.dimen.text_size_small);

        TextTextItem ticketsItem = getPointsTicketsItem(context);
        ticketsItem.setTextSize(textSize);
        points.add(ticketsItem);

        TextTextItem routesItem = getPointsRoutesItem(context);
        routesItem.setTextSize(textSize);
        points.add(routesItem);

        if (game.isStationsAvailable()) {
            TextTextItem stationItem = getPointsStationsItem(context);
            stationItem.setTextSize(textSize);
            points.add(stationItem);
        }

        String totalPointsLabel = context.getResources().getString(R.string.total) + ": ";
        String totalPointsValue = String.valueOf(game.getPointsCalculator().sumPoints());
        TextTextItem totalItem = new TextTextItem(totalPointsLabel, totalPointsValue);
        totalItem.setTextSize(textSize);
        points.add(totalItem);

        return points;
    }

    private TextTextItem getPointsTicketsItem(Context context) {
        Map<String, Integer> pointsTickets = game.getPointsCalculator().calculatePointsTickets(player.getTickets());
        StringBuilder builder = new StringBuilder();
        builder.append(Objects.requireNonNull(pointsTickets.get("realized")).toString());
        builder.append(" - ");
        builder.append(Objects.requireNonNull(pointsTickets.get("unrealized")).toString());
        builder.append(" = ");
        builder.append(Objects.requireNonNull(pointsTickets.get("total")).toString());

        String label = context.getResources().getString(R.string.tickets) + ": ";
        String value = builder.toString();
        return new TextTextItem(label, value);
    }

    private TextTextItem getPointsRoutesItem(Context context) {
        String label = context.getResources().getString(R.string.claimed_routes) + ": ";
        String value = String.valueOf(game.getPointsCalculator().calculatePointsRoutes(player.getBuiltRoutes()));
        return new TextTextItem(label, value);
    }

    private TextTextItem getPointsStationsItem(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append(player.getNumberOfStations());
        builder.append(" x ");
        builder.append(game.getStationPoints());
        builder.append(" = ");
        builder.append(player.getNumberOfStations() * game.getStationPoints());

        String label = context.getResources().getString(R.string.stations_left) + ": ";
        String value = builder.toString();
        return new TextTextItem(label, value);
    }

    private void returnToTopPage() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}