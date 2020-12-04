package pl.sokolak.TicketToRideAssistant.Games.Europe;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.Calculators.DefaultPointsCalculator;
import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Pair;

public class EuropePointsCalculator extends DefaultPointsCalculator {
    private final Game game;

    public EuropePointsCalculator(Game game) {
        super(game);
        this.game = game;
    }

    @Override
    public int sumPoints() {
        int stationPoints = player.getNumberOfStations() * game.getStationPoints();
        return super.sumPoints() + stationPoints;
    }

    @Override
    public List<Pair<String, String>> getBonusesList(Context context) {
        List<Pair<String, String>> list = new ArrayList<>();
        String labelStations = context.getResources().getString(R.string.stations_left);
        String valueStations = player.getNumberOfStations() + " x " +
                game.getStationPoints() + " = " +
                player.getNumberOfStations() * game.getStationPoints();
        list.add(Pair.create(labelStations + ": ", valueStations));
        return list;
    }

    @Override
    public List<Pair<String, String>> getPossibleBonusesList(Context context) {
        List<Pair<String, String>> list = new ArrayList<>();
        String labelLongestPath = context.getResources().getString(R.string.longestPathBonus) + "?";
        String valueLongestPath = "+ 10";
        list.add(Pair.create(labelLongestPath, valueLongestPath));
        return list;
    }
}
