package pl.sokolak.TicketToRideAssistant.Calculators;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.Domain.Ticket;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Pair;

public class DefaultStatusCalculator implements StatusCalculator {
    protected Player player;
    protected Game game;

    public DefaultStatusCalculator(Game game) {
        this.game = game;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public List<Pair<String, String>> getStatusList(Context context) {
        List<Pair<String, String>> list = new ArrayList<>();

        String pointsLabel = context.getResources().getString(R.string.points);
        String pointsValue = String.valueOf(player.getPoints());
        list.add(Pair.create(pointsLabel, pointsValue));

        String carsLabel = context.getResources().getString(R.string.cars);
        String carsValue = String.valueOf(player.getNumberOfCars());
        list.add(Pair.create(carsLabel, carsValue));

        int realizedTicketsNumber = 0;
        int unrealizedTicketsNumber = 0;
        for (Ticket ticket : player.getTickets()) {
            if (ticket.isRealized()) {
                realizedTicketsNumber++;
            } else {
                unrealizedTicketsNumber++;
            }
        }

        String ticketsLabel = context.getResources().getString(R.string.tickets);
        String realizedValue = String.valueOf(realizedTicketsNumber);
        String unrealizedValue = String.valueOf(unrealizedTicketsNumber);
        list.add(Pair.create(ticketsLabel, "+" + realizedValue + "/-" + unrealizedValue));

        String pathLabel = context.getResources().getString(R.string.longestPath);
        String pathValue = String.valueOf(player.getLongestPathLength());
        list.add(Pair.create(pathLabel, pathValue));

        list.addAll(getAdditionalItems(context));

        return list;
    }

    public List<Pair<String, String>> getAdditionalItems(Context context) {
        return new ArrayList<>();
    }
}
