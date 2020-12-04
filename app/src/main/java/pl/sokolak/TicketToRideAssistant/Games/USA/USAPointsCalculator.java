package pl.sokolak.TicketToRideAssistant.Games.USA;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.Calculators.DefaultPointsCalculator;
import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Pair;
import pl.sokolak.TicketToRideAssistant.Util.Triplet;

public class USAPointsCalculator extends DefaultPointsCalculator {
    public USAPointsCalculator(Game game) {
        super(game);
    }

    @Override
    public List<Pair<String, String>> getPossibleBonusesList(Context context) {
        List<Pair<String, String>> list = new ArrayList<>();
        String labelLongestPath = context.getResources().getString(R.string.longestPathBonus) + "?";
        String valueLongestPath = "+ 10";
        list.add(Pair.create(labelLongestPath, valueLongestPath));

        String labelGlobetrotter = context.getResources().getString(R.string.globetrotterBonus) + "?";
        String valueGlobetrotter = "+ 15";
        list.add(Pair.create(labelGlobetrotter, valueGlobetrotter));

        return list;
    }
}
