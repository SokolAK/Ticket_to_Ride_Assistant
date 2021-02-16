package pl.sokolak.TicketToRideAssistant.Games.USA;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Route;
import pl.sokolak.TicketToRideAssistant.Domain.Ticket;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Triplet;

import static pl.sokolak.TicketToRideAssistant.UI.Card.CarCardColor.BLACK;
import static pl.sokolak.TicketToRideAssistant.UI.Card.CarCardColor.BLUE;
import static pl.sokolak.TicketToRideAssistant.UI.Card.CarCardColor.GREEN;
import static pl.sokolak.TicketToRideAssistant.UI.Card.CarCardColor.NONE;
import static pl.sokolak.TicketToRideAssistant.UI.Card.CarCardColor.ORANGE;
import static pl.sokolak.TicketToRideAssistant.UI.Card.CarCardColor.RED;
import static pl.sokolak.TicketToRideAssistant.UI.Card.CarCardColor.VIOLET;
import static pl.sokolak.TicketToRideAssistant.UI.Card.CarCardColor.WHITE;
import static pl.sokolak.TicketToRideAssistant.UI.Card.CarCardColor.YELLOW;

public class USA extends Game {
    Context context;

    public USA(Context context) {
        super(context);
        this.context = context;

        databaseName = "TtRA_USA.db";
        databaseVersion = 1;

        title = "USA";
        startCards = 4;
        maxNoOfCardsToDraw = 2;
        numberOfCars = 45;

        prepareBaseGame();
        ////////////////////////////////////////////////////////////////////////////////////////
        //NO SQLite
        setScoring(initScoring());
        setRoutes(initRoutes());
        addTicketDecks();
        updateTickets();
        ///////////////////////////////////////////////////////////////////////////////////////
        pointsCalculator = new USAPointsCalculator(this);
    }

    private void addTicketDecks() {
        ticketsDecks.add(Triplet.create
                ("Tickets_Base_Short",
                        context.getString(R.string.TtRA_USA_Tickets_Base_Short),
                        true));
        ticketsDecks.add(Triplet.create
                ("Tickets_MysteryTrain",
                        context.getString(R.string.TtRA_USA_Tickets_MysteryTrain),
                        false));
        ticketsDecks.add(Triplet.create
                ("Tickets_1910",
                        context.getString(R.string.TtRA_USA_Tickets_1910),
                        false));
        ticketsDecks.add(Triplet.create
                ("Tickets_1910_Standard",
                        context.getString(R.string.TtRA_USA_Tickets_1910_Standard),
                        false));
        //updateTickets();
    }

    private HashMap<Integer, Integer> initScoring() {
        HashMap<Integer, Integer> scoring = new HashMap<>();
        scoring.put(1, 1);
        scoring.put(2, 2);
        scoring.put(3, 4);
        scoring.put(4, 7);
        scoring.put(5, 10);
        scoring.put(6, 15);
        return scoring;
    }

    private List<Route> initRoutes() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route("Vancouver", "Calgary", 3, 0, false, NONE));
        routes.add(new Route("Vancouver", "Seattle", 1, 0, false, NONE));
        routes.add(new Route("Vancouver", "Seattle", 1, 0, false, NONE));
        routes.add(new Route("Seattle", "Calgary", 4, 0, false, NONE));
        routes.add(new Route("Seattle", "Portland", 1, 0, false, NONE));
        routes.add(new Route("Seattle", "Portland", 1, 0, false, NONE));
        routes.add(new Route("Calgary", "Helena", 4, 0, false, NONE));
        routes.add(new Route("Seattle", "Helena", 6, 0, false, YELLOW));
        routes.add(new Route("Portland", "San Francisco", 5, 0, false, GREEN));
        routes.add(new Route("Portland", "San Francisco", 5, 0, false, VIOLET));
        routes.add(new Route("San Francisco", "Salt Lake City", 5, 0, false, ORANGE));
        routes.add(new Route("San Francisco", "Salt Lake City", 5, 0, false, WHITE));
        routes.add(new Route("Salt Lake City", "Portland", 6, 0, false, BLUE));
        routes.add(new Route("Salt Lake City", "Denver", 3, 0, false, RED));
        routes.add(new Route("Salt Lake City", "Denver", 3, 0, false, YELLOW));
        routes.add(new Route("Denver", "Helena", 4, 0, false, GREEN));
        routes.add(new Route("Helena", "Salt Lake City", 3, 0, false, VIOLET));
        routes.add(new Route("San Francisco", "Los Angeles", 3, 0, false, YELLOW));
        routes.add(new Route("San Francisco", "Los Angeles", 3, 0, false, VIOLET));
        routes.add(new Route("Los Angeles", "Las Vegas", 2, 0, false, NONE));
        routes.add(new Route("Las Vegas", "Salt Lake City", 3, 0, false, ORANGE));
        routes.add(new Route("Los Angeles", "El Paso", 6, 0, false, BLACK));
        routes.add(new Route("Los Angeles", "Phoenix", 3, 0, false, NONE));
        routes.add(new Route("Phoenix", "El Paso", 3, 0, false, NONE));
        routes.add(new Route("El Paso", "Santa Fe", 2, 0, false, NONE));
        routes.add(new Route("Santa Fe", "Phoenix", 3, 0, false, NONE));
        routes.add(new Route("Phoenix", "Denver", 5, 0, false, WHITE));
        routes.add(new Route("Denver", "Santa Fe", 2, 0, false, NONE));
        routes.add(new Route("Calgary", "Winnipeg", 6, 0, false, WHITE));
        routes.add(new Route("Winnipeg", "Helena", 4, 0, false, BLUE));
        routes.add(new Route("Helena", "Duluth", 6, 0, false, ORANGE));
        routes.add(new Route("Helena", "Omaha", 5, 0, false, RED));
        routes.add(new Route("Omaha", "Duluth", 2, 0, false, NONE));
        routes.add(new Route("Omaha", "Duluth", 2, 0, false, NONE));
        routes.add(new Route("Duluth", "Winnipeg", 4, 0, false, BLACK));
        routes.add(new Route("Omaha", "Denver", 4, 0, false, VIOLET));
        routes.add(new Route("Denver", "Kansas City", 4, 0, false, BLACK));
        routes.add(new Route("Denver", "Kansas City", 4, 0, false, ORANGE));
        routes.add(new Route("Omaha", "Kansas City", 1, 0, false, NONE));
        routes.add(new Route("Omaha", "Kansas City", 1, 0, false, NONE));
        routes.add(new Route("Kansas City", "Oklahoma City", 2, 0, false, NONE));
        routes.add(new Route("Kansas City", "Oklahoma City", 2, 0, false, NONE));
        routes.add(new Route("Oklahoma City", "Denver", 4, 0, false, RED));
        routes.add(new Route("Oklahoma City", "Santa Fe", 3, 0, false, BLUE));
        routes.add(new Route("Oklahoma City", "El Paso", 5, 0, false, YELLOW));
        routes.add(new Route("El Paso", "Dallas", 4, 0, false, RED));
        routes.add(new Route("El Paso", "Houston", 6, 0, false, GREEN));
        routes.add(new Route("Houston", "Dallas", 1, 0, false, NONE));
        routes.add(new Route("Houston", "Dallas", 1, 0, false, NONE));
        routes.add(new Route("Dallas", "Oklahoma City", 2, 0, false, NONE));
        routes.add(new Route("Dallas", "Oklahoma City", 2, 0, false, NONE));
        routes.add(new Route("Houston", "New Orleans", 1, 0, false, NONE));
        routes.add(new Route("New Orleans", "Little Rock", 3, 0, false, GREEN));
        routes.add(new Route("Little Rock", "Dallas", 2, 0, false, NONE));
        routes.add(new Route("Little Rock", "Oklahoma City", 2, 0, false, NONE));
        routes.add(new Route("Little Rock", "Saint Louis", 2, 0, false, NONE));
        routes.add(new Route("Saint Louis", "Kansas City", 2, 0, false, BLUE));
        routes.add(new Route("Saint Louis", "Kansas City", 2, 0, false, VIOLET));
        routes.add(new Route("Saint Louis", "Chicago", 2, 0, false, GREEN));
        routes.add(new Route("Saint Louis", "Chicago", 2, 0, false, WHITE));
        routes.add(new Route("Chicago", "Omaha", 4, 0, false, BLUE));
        routes.add(new Route("Chicago", "Duluth", 3, 0, false, RED));
        routes.add(new Route("Duluth", "Sault St. Marie", 3, 0, false, NONE));
        routes.add(new Route("Sault St. Marie", "Winnipeg", 6, 0, false, NONE));
        routes.add(new Route("Duluth", "Toronto", 6, 0, false, VIOLET));
        routes.add(new Route("Toronto", "Sault St. Marie", 2, 0, false, NONE));
        routes.add(new Route("Sault St. Marie", "Montréal", 5, 0, false, BLACK));
        routes.add(new Route("Montréal", "Toronto", 3, 0, false, NONE));
        routes.add(new Route("Toronto", "Chicago", 4, 0, false, WHITE));
        routes.add(new Route("Chicago", "Pittsburgh", 3, 0, false, ORANGE));
        routes.add(new Route("Chicago", "Pittsburgh", 3, 0, false, BLACK));
        routes.add(new Route("Pittsburgh", "Toronto", 2, 0, false, NONE));
        routes.add(new Route("Pittsburgh", "New York", 2, 0, false, WHITE));
        routes.add(new Route("Pittsburgh", "New York", 2, 0, false, GREEN));
        routes.add(new Route("New York", "Montréal", 3, 0, false, BLUE));
        routes.add(new Route("Montréal", "Boston", 2, 0, false, NONE));
        routes.add(new Route("Montréal", "Boston", 2, 0, false, NONE));
        routes.add(new Route("Boston", "New York", 2, 0, false, YELLOW));
        routes.add(new Route("Boston", "New York", 2, 0, false, RED));
        routes.add(new Route("New York", "Washington", 2, 0, false, ORANGE));
        routes.add(new Route("New York", "Washington", 2, 0, false, BLACK));
        routes.add(new Route("Washington", "Pittsburgh", 2, 0, false, NONE));
        routes.add(new Route("Pittsburgh", "Raleigh", 2, 0, false, NONE));
        routes.add(new Route("Raleigh", "Nashville", 3, 0, false, BLACK));
        routes.add(new Route("Nashville", "Pittsburgh", 4, 0, false, YELLOW));
        routes.add(new Route("Pittsburgh", "Saint Louis", 5, 0, false, GREEN));
        routes.add(new Route("Saint Louis", "Nashville", 2, 0, false, NONE));
        routes.add(new Route("Washington", "Raleigh", 2, 0, false, NONE));
        routes.add(new Route("Washington", "Raleigh", 2, 0, false, NONE));
        routes.add(new Route("Raleigh", "Charleston", 2, 0, false, NONE));
        routes.add(new Route("Charleston", "Atlanta", 2, 0, false, NONE));
        routes.add(new Route("Nashville", "Raleigh", 2, 0, false, NONE));
        routes.add(new Route("Nashville", "Raleigh", 2, 0, false, NONE));
        routes.add(new Route("Atlanta", "Nashville", 1, 0, false, NONE));
        routes.add(new Route("Nashville", "Little Rock", 3, 0, false, WHITE));
        routes.add(new Route("New Orleans", "Atlanta", 4, 0, false, YELLOW));
        routes.add(new Route("New Orleans", "Atlanta", 4, 0, false, ORANGE));
        routes.add(new Route("New Orleans", "Miami", 6, 0, false, RED));
        routes.add(new Route("Atlanta", "Miami", 5, 0, false, BLUE));
        routes.add(new Route("Charleston", "Miami", 4, 0, false, VIOLET));
        return routes;
    }

    public void updateTickets() {
        setTickets(initTickets());
    }

    private List<Ticket> initTickets() {
        List<Ticket> tickets = new ArrayList<>();
        long newTicketHash = calculateTicketsHash();
        if (ticketHash != newTicketHash) {
            ticketHash = newTicketHash;
            tickets.clear();
            for (Triplet<String, String, Boolean> deck : ticketsDecks) {
                if (deck.third) {
                    switch (deck.first) {
                        case "Tickets_Base_Short":
                            tickets.addAll(addTicketsBaseShort(deck.first));
                            break;
                        case "Tickets_MysteryTrain":
                            tickets.addAll(addTicketsMysteryTrain(deck.first));
                            break;
                        case "Tickets_1910":
                            tickets.addAll(addTickets1910(deck.first));
                            break;
                        case "Tickets_1910_Standard":
                            tickets.addAll(addTickets1910Standard(deck.first));
                            break;
                    }
                }
            }
        }
        return tickets;
    }

    private List<Ticket> addTicketsBaseShort(String deckName) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket("Boston", "Miami", 12, deckName));
        tickets.add(new Ticket("Calgary", "Phoenix", 13, deckName));
        tickets.add(new Ticket("Calgary", "Salt Lake City", 7, deckName));
        tickets.add(new Ticket("Chicago", "New Orleans", 7, deckName));
        tickets.add(new Ticket("Chicago", "Santa Fe", 9, deckName));
        tickets.add(new Ticket("Dallas", "New York", 11, deckName));
        tickets.add(new Ticket("Denver", "El Paso", 4, deckName));
        tickets.add(new Ticket("Denver", "Pittsburgh", 11, deckName));
        tickets.add(new Ticket("Duluth", "El Paso", 10, deckName));
        tickets.add(new Ticket("Duluth", "Houston", 8, deckName));
        tickets.add(new Ticket("Helena", "Los Angeles", 8, deckName));
        tickets.add(new Ticket("Kansas City", "Houston", 5, deckName));
        tickets.add(new Ticket("Los Angeles", "Chicago", 16, deckName));
        tickets.add(new Ticket("Los Angeles", "Miami", 20, deckName));
        tickets.add(new Ticket("Los Angeles", "New York", 21, deckName));
        tickets.add(new Ticket("Montréal", "Atlanta", 9, deckName));
        tickets.add(new Ticket("Montréal", "New Orleans", 13, deckName));
        tickets.add(new Ticket("New York", "Atlanta", 6, deckName));
        tickets.add(new Ticket("Portland", "Nashville", 17, deckName));
        tickets.add(new Ticket("Portland", "Phoenix", 11, deckName));
        tickets.add(new Ticket("San Francisco", "Atlanta", 17, deckName));
        tickets.add(new Ticket("Sault St. Marie", "Nashville", 8, deckName));
        tickets.add(new Ticket("Sault St. Marie", "Oklahoma City", 9, deckName));
        tickets.add(new Ticket("Seattle", "Los Angeles", 9, deckName));
        tickets.add(new Ticket("Seattle", "New York", 22, deckName));
        tickets.add(new Ticket("Toronto", "Miami", 10, deckName));
        tickets.add(new Ticket("Vancouver", "Montréal", 20, deckName));
        tickets.add(new Ticket("Vancouver", "Santa Fe", 13, deckName));
        tickets.add(new Ticket("Winnipeg", "Houston", 12, deckName));
        tickets.add(new Ticket("Winnipeg", "Little Rock", 11, deckName));
        return tickets;
    }

    private List<Ticket> addTicketsMysteryTrain(String deckName) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket("Boston", "Washington", 4, deckName));
        tickets.add(new Ticket("Montréal", "Chicago", 7, deckName));
        tickets.add(new Ticket("Vancouver", "Portland", 2, deckName));
        tickets.add(new Ticket("Winnipeg", "Omaha", 6, deckName));
        return tickets;
    }

    private List<Ticket> addTickets1910(String deckName) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket("Chicago", "Atlanta", 5, deckName));
        tickets.add(new Ticket("Chicago", "Boston", 7, deckName));
        tickets.add(new Ticket("Chicago", "New York", 5, deckName));
        tickets.add(new Ticket("Denver", "Saint Louis", 6, deckName));
        tickets.add(new Ticket("Duluth", "Dallas", 7, deckName));
        tickets.add(new Ticket("Houston", "Washington", 10, deckName));
        tickets.add(new Ticket("Kansas City", "Boston", 11, deckName));
        tickets.add(new Ticket("Las Vegas", "Miami", 21, deckName));
        tickets.add(new Ticket("Las Vegas", "New York", 19, deckName));
        tickets.add(new Ticket("Los Angeles", "Atlanta", 15, deckName));
        tickets.add(new Ticket("Los Angeles", "Calgary", 12, deckName));
        tickets.add(new Ticket("Los Angeles", "Oklahoma City", 9, deckName));
        tickets.add(new Ticket("Montréal", "Dallas", 13, deckName));
        tickets.add(new Ticket("Montréal", "Raleich", 7, deckName));
        tickets.add(new Ticket("Nashville", "New York", 6, deckName));
        tickets.add(new Ticket("New York", "Miami", 10, deckName));
        tickets.add(new Ticket("Omaha", "New Orleans", 8, deckName));
        tickets.add(new Ticket("Phoenix", "Boston", 19, deckName));
        tickets.add(new Ticket("Portland", "Houston", 16, deckName));
        tickets.add(new Ticket("Salt Lake City", "Chicago", 11, deckName));
        tickets.add(new Ticket("Salt Lake City", "Kansas City", 7, deckName));
        tickets.add(new Ticket("San Francisco", "Sault St. Marie", 17, deckName));
        tickets.add(new Ticket("San Francisco", "Washington", 21, deckName));
        tickets.add(new Ticket("Sault St. Marie", "Miami", 12, deckName));
        tickets.add(new Ticket("Seattle", "New York", 10, deckName));
        tickets.add(new Ticket("Seattle", "Oklahoma City", 14, deckName));
        tickets.add(new Ticket("Saint Louis", "Miami", 8, deckName));
        tickets.add(new Ticket("Toronto", "Charleston", 6, deckName));
        tickets.add(new Ticket("Vancouver", "Denver", 11, deckName));
        tickets.add(new Ticket("Washington", "Atlanta", 4, deckName));
        tickets.add(new Ticket("Winnipeg", "Santa Fe", 10, deckName));
        tickets.add(new Ticket("Calgary", "Nashville", 14, deckName));
        tickets.add(new Ticket("Vancouver", "Duluth", 13, deckName));
        tickets.add(new Ticket("Pittsburgh", "New Orleans", 8, deckName));
        tickets.add(new Ticket("Portland", "Pittsburgh", 19, deckName));
        return tickets;
    }

    private List<Ticket> addTickets1910Standard(String deckName) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket("Calgary", "Phoenix", 13, deckName));
        tickets.add(new Ticket("Calgary", "Salt Lake City", 7, deckName));
        tickets.add(new Ticket("Chicago", "New Orleans", 7, deckName));
        tickets.add(new Ticket("Chicago", "Santa Fe", 9, deckName));
        tickets.add(new Ticket("Dallas", "New York", 11, deckName));
        tickets.add(new Ticket("Duluth", "Houston", 8, deckName));
        tickets.add(new Ticket("Helena", "Los Angeles", 8, deckName));
        tickets.add(new Ticket("Kansas City", "Houston", 5, deckName));
        tickets.add(new Ticket("Los Angeles", "Chicago", 16, deckName));
        tickets.add(new Ticket("Los Angeles", "Miami", 19, deckName));
        tickets.add(new Ticket("Los Angeles", "New York", 20, deckName));
        tickets.add(new Ticket("Montréal", "Atlanta", 9, deckName));
        tickets.add(new Ticket("Montréal", "New Orleans", 13, deckName));
        tickets.add(new Ticket("New York", "Atlanta", 6, deckName));
        tickets.add(new Ticket("Portland", "Nashville", 17, deckName));
        tickets.add(new Ticket("Sault St. Marie", "Nashville", 8, deckName));
        tickets.add(new Ticket("Sault St. Marie", "Oklahoma City", 8, deckName));
        tickets.add(new Ticket("Seattle", "Los Angeles", 9, deckName));
        tickets.add(new Ticket("Seattle", "New York", 20, deckName));
        tickets.add(new Ticket("Toronto", "Miami", 10, deckName));
        tickets.add(new Ticket("Vancouver", "Santa Fe", 13, deckName));
        tickets.add(new Ticket("Winnipeg", "Houston", 12, deckName));
        tickets.add(new Ticket("Winnipeg", "Little Rock", 11, deckName));
        tickets.add(new Ticket("Denver", "El Paso", 4, deckName));
        tickets.add(new Ticket("Duluth", "El Paso", 10, deckName));
        tickets.add(new Ticket("Portland", "Phoenix", 11, deckName));
        tickets.add(new Ticket("San Francisco", "Atlanta", 17, deckName));
        tickets.add(new Ticket("Vancouver", "Montréal", 20, deckName));
        tickets.add(new Ticket("Denver", "Pittsburgh", 11, deckName));
        tickets.add(new Ticket("Boston", "Miami", 12, deckName));
        return tickets;
    }
}
