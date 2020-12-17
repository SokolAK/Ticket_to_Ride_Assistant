package pl.sokolak.TicketToRideAssistant.Calculators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.Domain.Route;
import pl.sokolak.TicketToRideAssistant.Domain.Ticket;

public class DefaultPointsCalculator implements PointsCalculator {
    private Player player;
    private Game game;

    public DefaultPointsCalculator(Game game) {
        this.game = game;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public int sumPoints() {
        int points = 0;

        points += calculatePointsTickets(player.getTickets()).get("total");
        points += calculatePointsRoutes(player.getBuiltRoutes());
        points += player.getNumberOfStations() * game.getStationPoints();

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
