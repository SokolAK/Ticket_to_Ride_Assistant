package pl.sokolak.TicketToRideAssistant.Calculators;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.Domain.Route;
import pl.sokolak.TicketToRideAssistant.Domain.Ticket;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Pair;

public class DefaultPointsCalculator implements PointsCalculator {
    protected Player player;
    protected Game game;

    public DefaultPointsCalculator(Game game) {
        this.game = game;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public List<Pair<String, String>> getPointsList(Context context) {
        List<Pair<String, String>> list = new ArrayList<>();

        String labelTotal = context.getResources().getString(R.string.total);
        String valueTotal = String.valueOf(game.getPointsCalculator().sumPoints());
        list.add(Pair.create(labelTotal + ": ", valueTotal));

        String labelTickets = context.getResources().getString(R.string.tickets);
        Map<String, Integer> pointsTickets = game.getPointsCalculator().calculatePointsTickets(player.getTickets());
        String valueTickets = getPointsTicketsString(Objects.requireNonNull(pointsTickets.get("realized")),
                Objects.requireNonNull(pointsTickets.get("unrealized")),
                Objects.requireNonNull(pointsTickets.get("total")));
        list.add(Pair.create(labelTickets + ": ", valueTickets));

        String labelRoutes = context.getResources().getString(R.string.claimed_routes);
        String valueRoutes = String.valueOf(game.getPointsCalculator().calculatePointsRoutes(player.getBuiltRoutes()));
        list.add(Pair.create(labelRoutes + ": ", valueRoutes));

        list.addAll(getBonusesList(context));

        list.add(Pair.create("", ""));
        list.add(Pair.create(context.getResources().getString(R.string.possibleBonuses), ""));

        list.addAll(getPossibleBonusesList(context));

        return list;
    }

    @Override
    public List<Pair<String, String>> getBonusesList(Context context) {
        return new ArrayList<>();
    }

    @Override
    public List<Pair<String, String>> getPossibleBonusesList(Context context) {
        return new ArrayList<>();
    }

    @Override
    public String getPointsTicketsString(int noRealized, int noUnrealized, int noTotal) {
        return noRealized + " - " + noUnrealized + " = " + noTotal;
    }

    @Override
    public int sumPoints() {
        int points = 0;
        points += calculatePointsTickets(player.getTickets()).get("total");
        points += calculatePointsRoutes(player.getBuiltRoutes());
        return points;
    }

    @Override
    public Map<String, Integer> calculatePointsTickets(List<Ticket> tickets) {
        int pointsRealized = 0;
        int pointsUnrealized = 0;
        for (Ticket ticket : tickets) {
            if (ticket.isRealized()) {
                pointsRealized += ticket.getPoints();
            } else {
                pointsUnrealized += ticket.getPoints();
            }
        }
        Map<String, Integer> points = new HashMap<>();
        points.put("realized", pointsRealized);
        points.put("unrealized", pointsUnrealized);
        points.put("total", pointsRealized - pointsUnrealized);
        return points;
    }

    @Override
    public int calculatePointsRoutes(List<Route> routes) {
        int points = 0;
        for (Route route : routes) {
            if (game.getScoring().containsKey(route.getLength())) {
                @SuppressWarnings("ConstantConditions") int routePoints = game.getScoring().get(route.getLength());
                points += routePoints;
            }
        }
        return points;
    }
}