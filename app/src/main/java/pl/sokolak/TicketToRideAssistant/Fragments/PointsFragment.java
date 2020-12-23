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

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.TtRA_Application;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.UI.TextTextItem;
import pl.sokolak.TicketToRideAssistant.UI.TextTextItemAdapter;
import pl.sokolak.TicketToRideAssistant.Util.Pair;

import static pl.sokolak.TicketToRideAssistant.Util.DimensionUtils.getDimension;

public class PointsFragment extends Fragment {
    private Player player;
    private Game game;

    public PointsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        player = ((TtRA_Application) requireActivity().getApplication()).player;
        game = ((TtRA_Application) requireActivity().getApplication()).game;

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
        List<Pair<String, String>> pointsList = game.getPointsCalculator().getPointsList(context);
        List<TextTextItem> textTextItems = new ArrayList<>();
        for(Pair<String, String> pointsRow : pointsList) {
            String label = pointsRow.first;
            String value = pointsRow.second;
            TextTextItem textTextItem = new TextTextItem(label, value);
            textTextItem.setTextSize(getDimension(context, R.dimen.text_size_small));
            textTextItems.add(textTextItem);
        }
        return textTextItems;
    }

    private void returnToTopPage() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}