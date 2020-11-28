package pl.sokolak.TicketToRideAssistant.Logic;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.UI.TextTextItem;

import static pl.sokolak.TicketToRideAssistant.Util.DimensionUtils.getDimension;

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
