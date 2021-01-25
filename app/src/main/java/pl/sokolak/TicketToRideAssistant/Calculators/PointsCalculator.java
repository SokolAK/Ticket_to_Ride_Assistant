package pl.sokolak.TicketToRideAssistant.Calculators;

import android.content.Context;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.Domain.Route;
import pl.sokolak.TicketToRideAssistant.Domain.Ticket;
import pl.sokolak.TicketToRideAssistant.Util.Pair;

public interface PointsCalculator extends Serializable {
    void setPlayer(Player player);

    int sumPoints();

    int calculatePointsRoutes(List<Route> routes);

    Map<String, Integer> calculatePointsTickets(List<Ticket> tickets);

    List<Pair<String, String>> getPointsList(Context context);

    List<Pair<String, String>> getBonusesList(Context context);

    List<Pair<String, String>> getPossibleBonusesList(Context context);

    String getPointsTicketsString(int noRealized, int noUnrealized, int noTotal);
}