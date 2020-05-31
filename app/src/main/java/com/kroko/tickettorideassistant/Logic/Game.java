package com.kroko.TicketToRideAssistant.Logic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.UI.Card;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Data;

@Data
public class Game {
    private String title;
    private int startCards;
    private int maxNoOfCardsToDraw;
    private int numberOfStations;
    private int stationPoints;
    private int numberOfCars;
    private int maxExtraCardsForTunnel;
    private HashMap<Integer, Integer> scoring = new HashMap<>();
    private HashMap<Integer, Integer> stationCost = new HashMap<>();
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Route> routes = new ArrayList<>();
    private ArrayList<Ticket> tickets = new ArrayList<>();
    private String databaseName;
    private int databaseVersion;
    private Context context;

    public Game(Context context) {
        this.context = context;
    }

    public void prepare(int gameId) {
        switch (gameId) {
            case 0:
                title = "Ticket to Ride. Europe";

                numberOfCars = 45;
                startCards = 4;
                maxNoOfCardsToDraw = 10;
                numberOfStations = 3;
                stationPoints = 4;
                maxExtraCardsForTunnel = 3;

                scoring.put(1, 1);
                scoring.put(2, 2);
                scoring.put(3, 4);
                scoring.put(4, 7);
                scoring.put(6, 15);
                scoring.put(8, 21);

                stationCost.put(1, 1);
                stationCost.put(2, 2);
                stationCost.put(3, 3);

                cards.add(new Card('V', R.drawable.violet));
                cards.add(new Card('O', R.drawable.orange));
                cards.add(new Card('B', R.drawable.blue));
                cards.add(new Card('Y', R.drawable.yellow));
                cards.add(new Card('A', R.drawable.black));
                cards.add(new Card('G', R.drawable.green));
                cards.add(new Card('R', R.drawable.red));
                cards.add(new Card('W', R.drawable.white));
                cards.add(new Card('L', R.drawable.loco));

                databaseName = "TtRA_Europe.db";
                databaseVersion = 1;
                routes = DbReader.readRoutes(context, databaseName, databaseVersion);
                tickets.addAll(DbReader.readTickets(context, databaseName, databaseVersion, "Tickets_Short_Base"));
                tickets.addAll(DbReader.readTickets(context, databaseName, databaseVersion, "Tickets_Long_Base"));

                break;
        }
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
