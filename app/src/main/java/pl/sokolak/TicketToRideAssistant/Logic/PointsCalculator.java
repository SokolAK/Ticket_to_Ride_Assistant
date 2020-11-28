package pl.sokolak.TicketToRideAssistant.Logic;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import pl.sokolak.TicketToRideAssistant.R;

public class PointsCalculator {
    private Player player;
    private Game game;

    public PointsCalculator(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public int sumPoints() {
        int points = 0;

        points += calculatePointsTickets(player.getTickets()).get("total");
        points += calculatePointsRoutes(player.getBuiltRoutes());
        points += player.getNumberOfStations() * game.getStationPoints();

        return points;
    }

    public List<String> getPointsList(Context context) {
        List<String> points = new ArrayList<>();

        points.add(getPointsTicketsString(context));
        points.add(getPointsRoutesString(context));
        if(game.isStationsAvailable())
            points.add(getPointsStationsString(context));

        return points;
    }

    private String getPointsTicketsString(Context context) {
        Map<String, Integer> pointsTickets = calculatePointsTickets(player.getTickets());
        StringBuilder builder = new StringBuilder();
        builder.append(context.getResources().getString(R.string.tickets));
        builder.append(": ");
        builder.append(Objects.requireNonNull(pointsTickets.get("realized")).toString());
        builder.append(" - ");
        builder.append(Objects.requireNonNull(pointsTickets.get("unrealized")).toString());
        builder.append(" = ");
        builder.append(Objects.requireNonNull(pointsTickets.get("total")).toString());
        return builder.toString();
    }

    private Map<String, Integer> calculatePointsTickets(List<Ticket> tickets) {
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

    private String getPointsRoutesString(Context context) {
        return context.getResources().getString(R.string.claimed_routes) + ": " +
                calculatePointsRoutes(player.getBuiltRoutes());
    }

    private int calculatePointsRoutes(List<Route> routes) {
        int points = 0;
        for (Route route : routes) {
            if (game.getScoring().containsKey(route.getLength())) {
                @SuppressWarnings("ConstantConditions") int routePoints = game.getScoring().get(route.getLength());
                points += routePoints;
            }
        }
        return points;
    }

    private String getPointsStationsString(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append(context.getResources().getString(R.string.stations));
        builder.append(": ");
        builder.append(player.getNumberOfStations());
        builder.append(" * ");
        builder.append(game.getStationPoints());
        builder.append(" = ");
        builder.append(player.getNumberOfStations() * game.getStationPoints());
        return builder.toString();
    }
}
