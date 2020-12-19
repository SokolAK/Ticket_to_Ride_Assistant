package pl.sokolak.TicketToRideAssistant.Games.Europe;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.Calculators.DefaultPointsCalculator;
import pl.sokolak.TicketToRideAssistant.Calculators.DefaultStatusCalculator;
import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Pair;

public class EuropeStatusCalculator extends DefaultStatusCalculator {
    public EuropeStatusCalculator(Game game) {
        super(game);
    }

    @Override
    public List<Pair<String, String>> getAdditionalItems(Context context) {
        List<Pair<String, String>> list = new ArrayList<>();
        String carsLabel = context.getResources().getString(R.string.stations);
        String carsValue = String.valueOf(player.getNumberOfStations());
        list.add(Pair.create(carsLabel, carsValue));
        return list;
    }
}
