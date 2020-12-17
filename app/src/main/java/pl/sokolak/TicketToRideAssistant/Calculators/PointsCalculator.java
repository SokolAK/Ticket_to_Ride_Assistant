package pl.sokolak.TicketToRideAssistant.Calculators;

import java.util.List;
import java.util.Map;

import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.Domain.Route;
import pl.sokolak.TicketToRideAssistant.Domain.Ticket;

public interface PointsCalculator {
    void setPlayer(Player player);
    int sumPoints();
    Map<String, Integer> calculatePointsTickets(List<Ticket> tickets);
    int calculatePointsRoutes(List<Route> routes);
}
