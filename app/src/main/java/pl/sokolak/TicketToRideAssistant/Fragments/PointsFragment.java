package pl.sokolak.TicketToRideAssistant.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.Logic.Game;
import pl.sokolak.TicketToRideAssistant.Logic.Player;
import pl.sokolak.TicketToRideAssistant.Logic.PointsCalculator;
import pl.sokolak.TicketToRideAssistant.Logic.TtRA_Application;
import pl.sokolak.TicketToRideAssistant.R;

public class PointsFragment extends Fragment {

    public PointsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_points, container, false);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_points);

        Activity activity = requireActivity();
        Player player = ((TtRA_Application) requireActivity().getApplication()).player;
        Game game = ((TtRA_Application) requireActivity().getApplication()).game;
        PointsCalculator pointsCalculator = new PointsCalculator(player, game);

        ListView list = drawer.findViewById(R.id.points_list);
        list.setEnabled(false);
        List<String> rows = pointsCalculator.getPointsList(activity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.points_list_text_view, rows);
        System.out.println(list);
        list.setAdapter(adapter);

        return drawer;
    }

    private void returnToTopPage() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}