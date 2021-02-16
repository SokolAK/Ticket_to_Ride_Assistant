package pl.sokolak.TicketToRideAssistant.Games.Nordic;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Route;
import pl.sokolak.TicketToRideAssistant.Domain.Ticket;
import pl.sokolak.TicketToRideAssistant.Games.USA.USAPointsCalculator;
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

public class Nordic extends Game {
    Context context;

    public Nordic(Context context) {
        super(context);
        this.context = context;

        databaseName = "TtRA_Nordic.db";
        databaseVersion = 1;

        title = "Nordic Countries";
        startCards = 4;
        maxNoOfCardsToDraw = 2;
        numberOfCars = 40;
        carsToLocoTradeRatio = 3;

        prepareBaseGame();
        //setStationCost(DbReader.readStationCost(context, databaseName, databaseVersion));
        ////////////////////////////////////////////////////////////////////////////////////////
        //NO SQLite
        setScoring(initScoring());
        setRoutes(initRoutes());
        addTicketDecks();
        updateTickets();
        ///////////////////////////////////////////////////////////////////////////////////////
        pointsCalculator = new NordicPointsCalculator(this);
    }

    private void addTicketDecks() {
        ticketsDecks.add(Triplet.create
                ("Tickets_Base",
                        context.getString(R.string.TtRA_Nordic_Tickets_Base),
                        true));
    }

    private HashMap<Integer, Integer> initScoring() {
        HashMap<Integer, Integer> scoring = new HashMap<>();
        scoring.put(1, 1);
        scoring.put(2, 2);
        scoring.put(3, 4);
        scoring.put(4, 7);
        scoring.put(5, 10);
        scoring.put(6, 15);
        scoring.put(9, 27);
        return scoring;
    }

    private List<Route> initRoutes() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route("Honningsvåg", "Kirkenes", 2, 1, false, GREEN));
        routes.add(new Route("Honningsvåg", "Tromsø", 4, 2, false, VIOLET));
        routes.add(new Route("Tromsø", "Narvik", 3, 1, false, YELLOW));
        routes.add(new Route("Narvik", "Kiruna", 1, 0, true, VIOLET));
        routes.add(new Route("Narvik", "Kiruna", 1, 0, true, WHITE));
        routes.add(new Route("Kiruna", "Boden", 3, 0, false, BLACK));
        routes.add(new Route("Kiruna", "Boden", 3, 0, false, ORANGE));
        routes.add(new Route("Boden", "Tornio", 1, 0, false, GREEN));
        routes.add(new Route("Tornio", "Rovaniemi", 1, 0, false, RED));
        routes.add(new Route("Rovaniemi", "Kirkenes", 5, 0, false, BLUE));
        routes.add(new Route("Kirkenes", "Murmansk", 3, 1, false, WHITE));
        routes.add(new Route("Murmansk", "Lieksa", 9, 0, false, NONE));
        routes.add(new Route("Lieksa", "Kajaani", 1, 0, false, BLUE));
        routes.add(new Route("Kajaani", "Oulu", 2, 0, false, YELLOW));
        routes.add(new Route("Oulu", "Rovaniemi", 2, 0, false, ORANGE));
        routes.add(new Route("Oulu", "Tornio", 1, 0, false, WHITE));
        routes.add(new Route("Narvik", "Mo i Rana", 4, 2, false, ORANGE));
        routes.add(new Route("Mo i Rana", "Trondheim", 6, 2, false, RED));
        routes.add(new Route("Mo i Rana", "Trondheim", 5, 0, true, GREEN));
        routes.add(new Route("Trondheim", "Östersund", 2, 0, true, BLACK));
        routes.add(new Route("Östersund", "Sundsvall", 2, 0, false, GREEN));
        routes.add(new Route("Sundsvall", "Umeå", 3, 0, false, VIOLET));
        routes.add(new Route("Sundsvall", "Umeå", 3, 0, false, YELLOW));
        routes.add(new Route("Umeå", "Boden", 3, 0, false, WHITE));
        routes.add(new Route("Umeå", "Boden", 3, 0, false, RED));
        routes.add(new Route("Sundsvall", "Vaasa", 3, 1, false, BLUE));
        routes.add(new Route("Vaasa", "Umeå", 1, 1, false, NONE));
        routes.add(new Route("Vaasa", "Oulu", 3, 0, false, BLACK));
        routes.add(new Route("Oulu", "Kuopio", 3, 0, false, NONE));
        routes.add(new Route("Kuopio", "Kajaani", 2, 0, false, GREEN));
        routes.add(new Route("Kuopio", "Lieksa", 1, 0, false, BLACK));
        routes.add(new Route("Kuopio", "Vaasa", 4, 0, false, NONE));
        routes.add(new Route("Vaasa", "Tampere", 2, 0, false, VIOLET));
        routes.add(new Route("Tampere", "Lahti", 1, 0, false, BLUE));
        routes.add(new Route("Lahti", "Kuopio", 3, 0, false, BLACK));
        routes.add(new Route("Kuopio", "Imatra", 2, 0, false, VIOLET));
        routes.add(new Route("Imatra", "Lahti", 2, 0, false, YELLOW));
        routes.add(new Route("Imatra", "Helsinki", 3, 0, false, RED));
        routes.add(new Route("Helsinki", "Lahti", 1, 0, false, BLACK));
        routes.add(new Route("Tampere", "Turku", 1, 0, false, RED));
        routes.add(new Route("Turku", "Helsinki", 1, 0, false, WHITE));
        routes.add(new Route("Helsinki", "Tampere", 1, 0, false, ORANGE));
        routes.add(new Route("Helsinki", "Tallinn", 2, 1, false, VIOLET));
        routes.add(new Route("Helsinki", "Stockholm", 4, 1, false, YELLOW));
        routes.add(new Route("Helsinki", "Stockholm", 4, 2, false, NONE));
        routes.add(new Route("Turku", "Stockholm", 3, 1, false, BLUE));
        routes.add(new Route("Tallinn", "Stockholm", 4, 2, false, GREEN));
        routes.add(new Route("Stockholm", "Sundsvall", 4, 0, false, NONE));
        routes.add(new Route("Stockholm", "Sundsvall", 4, 0, false, NONE));
        routes.add(new Route("Stockholm", "Örebro", 2, 0, false, VIOLET));
        routes.add(new Route("Stockholm", "Örebro", 2, 0, false, BLACK));
        routes.add(new Route("Stockholm", "Norrköping", 1, 0, false, ORANGE));
        routes.add(new Route("Stockholm", "Norrköping", 1, 0, false, RED));
        routes.add(new Route("Norrköping", "Karlskrona", 3, 0, false, WHITE));
        routes.add(new Route("Norrköping", "Karlskrona", 3, 0, false, YELLOW));
        routes.add(new Route("Karlskrona", "København", 2, 1, false, GREEN));
        routes.add(new Route("Karlskrona", "København", 2, 1, false, BLUE));
        routes.add(new Route("København", "Århus", 1, 1, false, NONE));
        routes.add(new Route("København", "Göteborg", 2, 1, false, BLACK));
        routes.add(new Route("Göteborg", "Norrköping", 3, 0, false, NONE));
        routes.add(new Route("Norrköping", "Örebro", 2, 0, false, NONE));
        routes.add(new Route("Örebro", "Göteborg", 2, 0, false, BLUE));
        routes.add(new Route("Göteborg", "Oslo", 2, 0, false, ORANGE));
        routes.add(new Route("Oslo", "Örebro", 2, 0, false, YELLOW));
        routes.add(new Route("Oslo", "Örebro", 2, 0, false, GREEN));
        routes.add(new Route("Örebro", "Sundsvall", 4, 0, false, ORANGE));
        routes.add(new Route("Oslo", "Ålborg", 3, 1, false, WHITE));
        routes.add(new Route("Ålborg", "Göteborg", 2, 1, false, NONE));
        routes.add(new Route("Ålborg", "Århus", 1, 0, false, VIOLET));
        routes.add(new Route("Ålborg", "Kristiansand", 2, 1, false, RED));
        routes.add(new Route("Kristiansand", "Stavanger", 2, 0, true, GREEN));
        routes.add(new Route("Kristiansand", "Stavanger", 3, 1, false, ORANGE));
        routes.add(new Route("Stavanger", "Bergen", 2, 1, false, VIOLET));
        routes.add(new Route("Bergen", "Oslo", 4, 0, true, BLUE));
        routes.add(new Route("Bergen", "Oslo", 4, 0, true, RED));
        routes.add(new Route("Oslo", "Kristiansand", 2, 0, false, BLACK));
        routes.add(new Route("Oslo", "Lillehammer", 2, 0, true, VIOLET));
        routes.add(new Route("Lillehammer", "Trondheim", 3, 0, true, ORANGE));
        routes.add(new Route("Lillehammer", "Åndalsnes", 2, 0, true, YELLOW));
        routes.add(new Route("Åndalsnes", "Trondheim", 2, 1, false, WHITE));
        routes.add(new Route("Åndalsnes", "Bergen", 5, 2, false, BLACK));
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
                        case "Tickets_Base":
                            tickets.addAll(addTicketsBase(deck.first));
                            break;
                    }
                }
            }
        }
        return tickets;
    }

    private List<Ticket> addTicketsBase(String deckName) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket("København", "Murmansk", 24, deckName));
        tickets.add(new Ticket("Oslo", "Honningsvåg", 21, deckName));
        tickets.add(new Ticket("København", "Narvik", 18, deckName));
        tickets.add(new Ticket("Stavanger", "Rovaniemi", 18, deckName));
        tickets.add(new Ticket("Bergen", "Tornio", 17, deckName));
        tickets.add(new Ticket("Stockholm", "Tromsø", 17, deckName));
        tickets.add(new Ticket("Bergen", "Narvik", 16, deckName));
        tickets.add(new Ticket("København", "Oulu", 14, deckName));
        tickets.add(new Ticket("Helsinki", "Kirkenes", 13, deckName));
        tickets.add(new Ticket("Narvik", "Tallinn", 13, deckName));
        tickets.add(new Ticket("Göteborg", "Oulu", 12, deckName));
        tickets.add(new Ticket("Helsinki", "Bergen", 12, deckName));
        tickets.add(new Ticket("Kristiansand", "Mo i Rana", 12, deckName));
        tickets.add(new Ticket("Narvik", "Murmansk", 12, deckName));
        tickets.add(new Ticket("Norrköping", "Boden", 11, deckName));
        tickets.add(new Ticket("Tromsø", "Vaasa", 11, deckName));
        tickets.add(new Ticket("Ålborg", "Umeå", 11, deckName));
        tickets.add(new Ticket("Helsinki", "Kiruna", 10, deckName));
        tickets.add(new Ticket("Helsinki", "København", 10, deckName));
        tickets.add(new Ticket("Oslo", "Mo i Rana", 10, deckName));
        tickets.add(new Ticket("Stockholm", "Kajaani", 10, deckName));
        tickets.add(new Ticket("Tampere", "Kristiansand", 10, deckName));
        tickets.add(new Ticket("Turku", "Trondheim", 10, deckName));
        tickets.add(new Ticket("Örebro", "Kuopio", 10, deckName));
        tickets.add(new Ticket("Oslo", "Vaasa", 9, deckName));
        tickets.add(new Ticket("Bergen", "København", 8, deckName));
        tickets.add(new Ticket("Helsinki", "Östersund", 8, deckName));
        tickets.add(new Ticket("Oslo", "Helsinki", 8, deckName));
        tickets.add(new Ticket("Stavanger", "Karlskrona", 8, deckName));
        tickets.add(new Ticket("Stockholm", "Bergen", 8, deckName));
        tickets.add(new Ticket("Bergen", "Trondheim", 7, deckName));
        tickets.add(new Ticket("Göteborg", "Turku", 7, deckName));
        tickets.add(new Ticket("Stockholm", "Imatra", 7, deckName));
        tickets.add(new Ticket("Stockholm", "Umeå", 7, deckName));
        tickets.add(new Ticket("Göteborg", "Åndalsnes", 6, deckName));
        tickets.add(new Ticket("Stockholm", "København", 6, deckName));
        tickets.add(new Ticket("Sundsvall", "Lahti", 6, deckName));
        tickets.add(new Ticket("Tampere", "Boden", 6, deckName));
        tickets.add(new Ticket("Tornio", "Imatra", 6, deckName));
        tickets.add(new Ticket("Århus", "Lillehammer", 6, deckName));
        tickets.add(new Ticket("Helsinki", "Lieksa", 5, deckName));
        tickets.add(new Ticket("Ålborg", "Norrköping", 5, deckName));
        tickets.add(new Ticket("Oslo", "København", 4, deckName));
        tickets.add(new Ticket("Oslo", "Stavanger", 4, deckName));
        tickets.add(new Ticket("Oslo", "Stockholm", 4, deckName));
        tickets.add(new Ticket("Tampere", "Tallinn", 3, deckName));
        return tickets;
    }
}
