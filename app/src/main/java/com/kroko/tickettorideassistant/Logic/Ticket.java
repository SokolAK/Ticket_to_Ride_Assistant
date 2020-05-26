package com.kroko.TicketToRideAssistant.Logic;

import java.util.ArrayList;
import java.util.HashSet;
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
    private boolean inHand;
    private boolean realized;

    public Ticket(int id, String city1, String city2, int points) {
        this.id = id;
        this.city1 = city1;
        this.city2 = city2;
        this.points = points;
        this.realized = false;
        this.inHand = false;
    }
    public Ticket(Ticket ticket) {
        this.id = ticket.id;
        this.city1 = ticket.city1;
        this.city2 = ticket.city2;
        this.points = ticket.points;
        this.realized = ticket.realized;
        this.inHand = ticket.inHand;
    }

    @Override
    public String toString() {
        return city1 + "-" + city2 + " ★" + points;
    }

    public boolean checkIfRealized(Player player) {
        HashSet<Route> connectionList = new HashSet<>(player.getBuiltRoutes());
        connectionList.addAll(player.getBuiltStations());
        //List<Route> connectionList = new ArrayList<>(set);

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
            return false;
        }

        return checkIfConnected(connectionList, city1, "", city2);
    }

    private boolean checkIfConnected(HashSet<Route> connectionList, String cityStart, String cityPrevious, String cityDestination) {
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
