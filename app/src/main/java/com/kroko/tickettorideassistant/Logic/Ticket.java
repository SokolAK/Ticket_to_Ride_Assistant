package com.kroko.TicketToRideAssistant.Logic;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;

@Data
public class Ticket {
    private int id;
    private String city1;
    private String city2;
    private int points;
    private boolean realized;

    public Ticket(int id, String city1, String city2, int points) {
        this.id = id;
        this.city1 = city1;
        this.city2 = city2;
        this.points = points;
        this.realized = false;
    }
    public Ticket(Ticket ticket) {
        this.id = ticket.id;
        this.city1 = ticket.city1;
        this.city2 = ticket.city2;
        this.points = ticket.points;
        this.realized = ticket.realized;
    }

    @Override
    public String toString() {
        return city1 + "-" + city2 + " ★" + points;
    }

    public boolean checkIfRealized(Player player) {
        realized = false;
        Set<Route> set = new LinkedHashSet<>(player.getBuiltRoutes());
        set.addAll(player.getBuiltStations());
        List<Route> connectionList = new ArrayList<>(set);

        boolean ifCity1IsInBuiltRoutes = false;
        boolean ifCity2IsInBuiltRoutes = false;

        for (Route routes : connectionList) {
            if (city1.equals(routes.getCity1()) || city1.equals(routes.getCity2())) {
                ifCity1IsInBuiltRoutes = true;
            }
            if (city2.equals(routes.getCity1()) || city2.equals(routes.getCity2())) {
                ifCity2IsInBuiltRoutes = true;
            }
        }
        if (!ifCity1IsInBuiltRoutes || !ifCity2IsInBuiltRoutes) {
            return realized;
        }

        realized = checkIfConnected(connectionList, city1, "", city2);
        return realized;
    }

    private boolean checkIfConnected(List<Route> connectionList, String cityStart, String cityPrevious, String cityDestination) {
        ArrayList<String> cities2 = new ArrayList<>();
        for (Route playerRoute : connectionList) {
            if (cityStart.equals(playerRoute.getCity1()) && !cityPrevious.equals(playerRoute.getCity2())) {
                cities2.add(playerRoute.getCity2());
            }
            if (cityStart.equals(playerRoute.getCity2()) && !cityPrevious.equals(playerRoute.getCity1())) {
                cities2.add(playerRoute.getCity1());
            }
        }

        for (String city2 : cities2) {
            if (city2.equals(cityDestination)) {
                return true;
            }
            if (checkIfConnected(connectionList, city2, cityStart, cityDestination)) {
                return true;
            }
        }
        return false;
    }
}
