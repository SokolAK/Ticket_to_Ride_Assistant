package com.kroko.TicketToRideAssistant.Logic;

import android.content.Context;

import com.kroko.TicketToRideAssistant.Games.Europe;
import com.kroko.TicketToRideAssistant.Games.USA;
import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.UI.Card;
import com.kroko.TicketToRideAssistant.Util.*;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Data;


@Data
public class Game {
    protected String title;
    protected int startCards;
    protected int maxNoOfCardsToDraw;
    protected int numberOfStations;
    protected int stationPoints;
    protected int numberOfCars;
    protected int maxExtraCardsForTunnel;
    protected HashMap<Integer, Integer> scoring = new HashMap<>();
    protected HashMap<Integer, Integer> stationCost = new HashMap<>();
    protected ArrayList<Card> cards = new ArrayList<>();
    protected ArrayList<Route> routes = new ArrayList<>();
    protected ArrayList<Ticket> tickets = new ArrayList<>();
    protected String databaseName;
    protected int databaseVersion;
    protected Context context;
    protected ArrayList<Triplet<String,String,Boolean>> ticketsDecks = new ArrayList<>();
    protected long ticketHash = 0;

    public Game(Context context, String title) {
        this.context = context;
        this.title = title;
        setCards();
    }

    public static Game create(Context context, int gameId, String title) {
        switch (gameId) {
            case 0:
                return new USA(context, title);
            case 1:
                return new Europe(context, title);
        }
        return null;
    }

    private void setCards() {
        cards.add(new Card('V', R.drawable.violet));
        cards.add(new Card('O', R.drawable.orange));
        cards.add(new Card('B', R.drawable.blue));
        cards.add(new Card('Y', R.drawable.yellow));
        cards.add(new Card('A', R.drawable.black));
        cards.add(new Card('G', R.drawable.green));
        cards.add(new Card('R', R.drawable.red));
        cards.add(new Card('W', R.drawable.white));
        cards.add(new Card('L', R.drawable.loco));
    }

    public void updateTickets() {
        long newTicketHash = calculateTicketsHash();
        if(ticketHash != newTicketHash) {
            ticketHash = newTicketHash;
            tickets.clear();
            for (Triplet deck : ticketsDecks) {
                if ((Boolean) deck.third) {
                    tickets.addAll(DbReader.readTickets(context, databaseName, databaseVersion, (String) deck.first));
                }
            }
        }
    }

    private long calculateTicketsHash() {
        long hash = 0;
        for(Triplet deck: ticketsDecks) {
            int c = (Boolean)deck.third ? 1 : 0;
            hash = 31 * hash + c;
        }
        return hash;
    }

    public ArrayList<Ticket> getTickets(String city1) {
        ArrayList<Ticket> result = new ArrayList<>();
        for (Ticket ticket: tickets) {
            if (ticket.getCity1().equals(city1) || ticket.getCity2().equals(city1)) {
                result.add(ticket);
            }
        }
        return result;
    }
    public Ticket getTicket(int id) {
        for (Ticket ticket: tickets) {
            if (ticket.getId() == id) {
                return ticket;
            }
        }
        return null;
    }
    public void removeTicket(int id) {
        for(int i =0; i<tickets.size(); ++i)
            if(tickets.get(i).getId() == id)
                tickets.remove(i);
    }

    public Route getRoute(int id) {
        for (Route route : routes) {
            if (route.getId() == id) {
                return route;
            }
        }
        return null;
    }

    public ArrayList<Route> getRoutes(String city1, boolean checkIfBuilt, boolean checkIfBuiltStation) {
        ArrayList<Route> result = new ArrayList<>();
        for (Route route : routes) {
            if (!route.isBuilt() || !checkIfBuilt) {
                if (!route.isBuiltStation() || !checkIfBuiltStation) {
                    if (route.getCity1().equals(city1) || route.getCity2().equals(city1)) {
                        result.add(route);
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<Route> getRoutes(String city1, String city2, boolean checkIfBuilt, boolean checkIfBuiltStation) {
        ArrayList<Route> result = new ArrayList<>();
        for (Route route : routes) {
            if (!route.isBuilt() || !checkIfBuilt) {
                if (!route.isBuiltStation() || !checkIfBuiltStation) {
                    if ((route.getCity1().equals(city1) && route.getCity2().equals(city2)) ||
                            (route.getCity2().equals(city1) && route.getCity1().equals(city2))) {
                        result.add(route);
                    }
                }
            }
        }
        return result;
    }
}
