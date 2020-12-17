package pl.sokolak.TicketToRideAssistant.Games.Nordic;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.Calculators.DefaultPointsCalculator;
import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Pair;

public class NordicPointsCalculator extends DefaultPointsCalculator {
    public NordicPointsCalculator(Game game) {
        super(game);
    }

    @Override
    public List<Pair<String, String>> getPossibleBonusesList(Context context) {
        List<Pair<String, String>> list = new ArrayList<>();
        String labelGlobetrotter = context.getResources().getString(R.string.globetrotterBonus) + "?";
        String valueGlobetrotter = "+ 10";
        list.add(Pair.create(labelGlobetrotter, valueGlobetrotter));
        return list;
    }
}