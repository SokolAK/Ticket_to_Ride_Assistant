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
    private static int counter;
    private int id;
    private String city1;
    private String city2;
    private int points;
    private boolean inHand;
    private boolean realized;
    private String deckName;

    public Ticket(String city1, String city2, int points, String deckName) {
        counter++;
        id = counter;
        this.city1 = city1;
        this.city2 = city2;
        this.points = points;
        this.realized = false;
        this.inHand = false;
        this.deckName = deckName;
    }
    public Ticket(Ticket ticket) {
        counter++;
        id = counter;
        this.city1 = ticket.city1;
        this.city2 = ticket.city2;
        this.points = ticket.points;
        this.realized = ticket.realized;
        this.inHand = ticket.inHand;
        this.deckName = ticket.deckName;
    }

    @Override
    public String toString() {
        return city1 + " - " + city2 + " ★" + points;
    }

    public boolean checkIfRealized(Player player) {
        ConnectionCalculator connectionCalc = new ConnectionCalculator(player);

        if(!connectionCalc.checkIfCityIsOnPlayersList(city1) && !connectionCalc.checkIfCityIsOnPlayersList(city2))
            return false;

        return connectionCalc.checkIfConnected(city1, "", city2);
    }
}
