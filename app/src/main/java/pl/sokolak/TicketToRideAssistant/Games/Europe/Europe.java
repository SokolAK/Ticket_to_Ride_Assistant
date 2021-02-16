package pl.sokolak.TicketToRideAssistant.Games.Europe;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.Database.DbReader;
import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Route;
import pl.sokolak.TicketToRideAssistant.Domain.Ticket;
import pl.sokolak.TicketToRideAssistant.Games.USA.USAPointsCalculator;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Triplet;

import static pl.sokolak.TicketToRideAssistant.UI.Card.CarCardColor.*;

public class Europe extends Game implements Serializable {
    Context context;

    public Europe(Context context) {
        super(context);
        this.context = context;

        databaseName = "TtRA_Europe.db";
        databaseVersion = 1;

        title = "Europe";
        startCards = 4;
        maxNoOfCardsToDraw = 2;
        numberOfStations = 3;
        stationPoints = 4;
        numberOfCars = 45;
        maxExtraCardsForTunnel = 3;
        warehousesAvailable = true;
        stationsAvailable = true;

        prepareBaseGame();
        //setStationCost(DbReader.readStationCost(context, databaseName, databaseVersion));
        ////////////////////////////////////////////////////////////////////////////////////////
        //NO SQLite
        setStationCost(initStationCost());
        setScoring(initScoring());
        setRoutes(initRoutes());
        addTicketDecks();
        updateTickets();
        ///////////////////////////////////////////////////////////////////////////////////////
        pointsCalculator = new EuropePointsCalculator(this);
        statusCalculator = new EuropeStatusCalculator(this);
    }

    private void addTicketDecks() {
        ticketsDecks.add(Triplet.create
                ("Tickets_Base_Long",
                        context.getString(R.string.TtRA_Europe_Tickets_Base_Long),
                        true));
        ticketsDecks.add(Triplet.create
                ("Tickets_Base_Short",
                        context.getString(R.string.TtRA_Europe_Tickets_Base_Short),
                        true));
        ticketsDecks.add(Triplet.create
                ("Tickets_Base_BigCities",
                        context.getString(R.string.TtRA_Europe_Tickets_Base_BigCities),
                        true));
        ticketsDecks.add(Triplet.create
                ("Tickets_1912_Long",
                        context.getString(R.string.TtRA_Europe_Tickets_1912_Long),
                        false));
        ticketsDecks.add(Triplet.create
                ("Tickets_1912_Short",
                        context.getString(R.string.TtRA_Europe_Tickets_1912_Short),
                        false));
        ticketsDecks.add(Triplet.create
                ("Tickets_1912_BigCities",
                        context.getString(R.string.TtRA_Europe_Tickets_1912_BigCities),
                        false));
        //updateTickets();
    }

    private HashMap<Integer, Integer> initStationCost() {
        HashMap<Integer, Integer> stationCost = new HashMap<>();
        stationCost.put(1, 1);
        stationCost.put(2, 2);
        stationCost.put(3, 3);
        return stationCost;
    }

    private HashMap<Integer, Integer> initScoring() {
        HashMap<Integer, Integer> scoring = new HashMap<>();
        scoring.put(1, 1);
        scoring.put(2, 2);
        scoring.put(3, 4);
        scoring.put(4, 7);
        scoring.put(6, 15);
        scoring.put(8, 21);
        return scoring;
    }

    private List<Route> initRoutes() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route("Amsterdam", "Essen", 3, 0, false, YELLOW));
        routes.add(new Route("Berlin", "Warszawa", 4, 0, false, YELLOW));
        routes.add(new Route("Bruxelles", "Paris", 2, 0, false, YELLOW));
        routes.add(new Route("Constantinople", "Bucuresti", 3, 0, false, YELLOW));
        routes.add(new Route("København", "Stockholm", 3, 0, false, YELLOW));
        routes.add(new Route("Madrid", "Barcelona", 2, 0, false, YELLOW));
        routes.add(new Route("München", "Zürich", 2, 0, true, YELLOW));
        routes.add(new Route("Smolensk", "Wilno", 3, 0, false, YELLOW));
        routes.add(new Route("Brindisi", "Roma", 2, 0, false, WHITE));
        routes.add(new Route("Budapest", "Wien", 1, 0, false, WHITE));
        routes.add(new Route("Frankfurt", "Amsterdam", 2, 0, false, WHITE));
        routes.add(new Route("København", "Stockholm", 3, 0, false, WHITE));
        routes.add(new Route("Pamplona", "Madrid", 3, 0, true, WHITE));
        routes.add(new Route("Paris", "Frankfurt", 3, 0, false, WHITE));
        routes.add(new Route("Petrograd", "Moskva", 4, 0, false, WHITE));
        routes.add(new Route("Sevastopol", "Bucuresti", 4, 0, false, WHITE));
        routes.add(new Route("Berlin", "Warszawa", 4, 0, false, VIOLET));
        routes.add(new Route("Brest", "Pamplona", 4, 0, false, VIOLET));
        routes.add(new Route("Frankfurt", "München", 2, 0, false, VIOLET));
        routes.add(new Route("Madrid", "Lisboa", 3, 0, false, VIOLET));
        routes.add(new Route("Marseille", "Zürich", 2, 0, true, VIOLET));
        routes.add(new Route("Paris", "Dieppe", 1, 0, false, VIOLET));
        routes.add(new Route("Sarajevo", "Budapest", 3, 0, false, VIOLET));
        routes.add(new Route("Sofia", "Athína", 3, 0, false, VIOLET));
        routes.add(new Route("Berlin", "Frankfurt", 3, 0, false, RED));
        routes.add(new Route("Bruxelles", "Paris", 2, 0, false, RED));
        routes.add(new Route("Budapest", "Wien", 1, 0, false, RED));
        routes.add(new Route("Marseille", "Pamplona", 4, 0, false, RED));
        routes.add(new Route("Smolensk", "Kyїv", 3, 0, false, RED));
        routes.add(new Route("Sochi", "Erzurum", 3, 0, true, RED));
        routes.add(new Route("Warszawa", "Wilno", 3, 0, false, RED));
        routes.add(new Route("Zágráb", "Sarajevo", 3, 0, false, RED));
        routes.add(new Route("Budapest", "Zágráb", 2, 0, false, ORANGE));
        routes.add(new Route("Cádiz", "Madrid", 3, 0, false, ORANGE));
        routes.add(new Route("Dieppe", "Brest", 2, 0, false, ORANGE));
        routes.add(new Route("Edinburgh", "London", 4, 0, false, ORANGE));
        routes.add(new Route("Moskva", "Smolensk", 2, 0, false, ORANGE));
        routes.add(new Route("München", "Wien", 3, 0, false, ORANGE));
        routes.add(new Route("Paris", "Frankfurt", 3, 0, false, ORANGE));
        routes.add(new Route("Smyrna", "Angora", 3, 0, true, ORANGE));
        routes.add(new Route("Dieppe", "Bruxelles", 2, 0, false, GREEN));
        routes.add(new Route("Essen", "Frankfurt", 2, 0, false, GREEN));
        routes.add(new Route("Kharkov", "Rostov", 2, 0, false, GREEN));
        routes.add(new Route("Pamplona", "Paris", 4, 0, false, GREEN));
        routes.add(new Route("Sarajevo", "Athína", 4, 0, false, GREEN));
        routes.add(new Route("Wien", "Berlin", 3, 0, false, GREEN));
        routes.add(new Route("Wilno", "Riga", 4, 0, false, GREEN));
        routes.add(new Route("Zürich", "Venezia", 2, 0, true, GREEN));
        routes.add(new Route("Berlin", "Essen", 2, 0, false, BLUE));
        routes.add(new Route("Constantinople", "Sofia", 3, 0, false, BLUE));
        routes.add(new Route("Frankfurt", "Bruxelles", 2, 0, false, BLUE));
        routes.add(new Route("Lisboa", "Cádiz", 2, 0, false, BLUE));
        routes.add(new Route("Pamplona", "Paris", 4, 0, false, BLUE));
        routes.add(new Route("Venezia", "München", 2, 0, true, BLUE));
        routes.add(new Route("Warszawa", "Wien", 4, 0, false, BLUE));
        routes.add(new Route("Wilno", "Petrograd", 4, 0, false, BLUE));
        routes.add(new Route("Berlin", "Frankfurt", 3, 0, false, BLACK));
        routes.add(new Route("Bruxelles", "Amsterdam", 1, 0, false, BLACK));
        routes.add(new Route("Edinburgh", "London", 4, 0, false, BLACK));
        routes.add(new Route("Erzurum", "Angora", 3, 0, false, BLACK));
        routes.add(new Route("Pamplona", "Madrid", 3, 0, true, BLACK));
        routes.add(new Route("Paris", "Brest", 3, 0, false, BLACK));
        routes.add(new Route("Riga", "Danzig", 3, 0, false, BLACK));
        routes.add(new Route("Venezia", "Roma", 2, 0, false, BLACK));
        routes.add(new Route("Amsterdam", "London", 2, 2, false, NONE));
        routes.add(new Route("Angora", "Constantinople", 2, 0, true, NONE));
        routes.add(new Route("Athína", "Brindisi", 4, 1, false, NONE));
        routes.add(new Route("Athína", "Smyrna", 2, 1, false, NONE));
        routes.add(new Route("Barcelona", "Pamplona", 2, 0, true, NONE));
        routes.add(new Route("Bucuresti", "Kyїv", 4, 0, false, NONE));
        routes.add(new Route("Bucuresti", "Sofia", 2, 0, true, NONE));
        routes.add(new Route("Budapest", "Bucuresti", 4, 0, true, NONE));
        routes.add(new Route("Constantinople", "Smyrna", 2, 0, true, NONE));
        routes.add(new Route("Danzig", "Berlin", 4, 0, false, NONE));
        routes.add(new Route("Erzurum", "Sevastopol", 4, 2, false, NONE));
        routes.add(new Route("Essen", "København", 3, 1, false, NONE));
        routes.add(new Route("Essen", "København", 3, 1, false, NONE));
        routes.add(new Route("Kharkov", "Moskva", 4, 0, false, NONE));
        routes.add(new Route("Kyїv", "Budapest", 6, 0, true, NONE));
        routes.add(new Route("Kyїv", "Kharkov", 4, 0, false, NONE));
        routes.add(new Route("Kyїv", "Warszawa", 4, 0, false, NONE));
        routes.add(new Route("London", "Dieppe", 2, 1, false, NONE));
        routes.add(new Route("London", "Dieppe", 2, 1, false, NONE));
        routes.add(new Route("Marseille", "Barcelona", 4, 0, false, NONE));
        routes.add(new Route("Palermo", "Brindisi", 3, 1, false, NONE));
        routes.add(new Route("Paris", "Marseille", 4, 0, false, NONE));
        routes.add(new Route("Petrograd", "Riga", 4, 0, false, NONE));
        routes.add(new Route("Roma", "Marseille", 4, 0, true, NONE));
        routes.add(new Route("Roma", "Palermo", 4, 1, false, NONE));
        routes.add(new Route("Rostov", "Sevastopol", 4, 0, false, NONE));
        routes.add(new Route("Sevastopol", "Constantinople", 4, 2, false, NONE));
        routes.add(new Route("Sevastopol", "Sochi", 2, 1, false, NONE));
        routes.add(new Route("Smyrna", "Palermo", 6, 2, false, NONE));
        routes.add(new Route("Sochi", "Rostov", 2, 0, false, NONE));
        routes.add(new Route("Sofia", "Sarajevo", 2, 0, true, NONE));
        routes.add(new Route("Stockholm", "Petrograd", 8, 0, true, NONE));
        routes.add(new Route("Warszawa", "Danzig", 2, 0, false, NONE));
        routes.add(new Route("Wien", "Zágráb", 2, 0, false, NONE));
        routes.add(new Route("Wilno", "Kyїv", 2, 0, false, NONE));
        routes.add(new Route("Zágráb", "Venezia", 2, 0, false, NONE));
        routes.add(new Route("Zürich", "Paris", 3, 0, true, NONE));
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
                        case "Tickets_Base_Long":
                            tickets.addAll(addTicketsBaseLong(deck.first));
                            break;
                        case "Tickets_Base_BigCities":
                            tickets.addAll(addTicketsBaseBigCities(deck.first));
                            break;
                        case "Tickets_1912_Short":
                            tickets.addAll(addTickets1912Short(deck.first));
                            break;
                        case "Tickets_1912_Long":
                            tickets.addAll(addTickets1912Long(deck.first));
                            break;
                        case "Tickets_1912_BigCities":
                            tickets.addAll(addTickets1912BigCities(deck.first));
                            break;
                    }
                }
            }
        }
        return tickets;
    }

    private List<Ticket> addTicketsBaseShort(String deckName) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket("Zürich", "Budapest", 6, deckName));
        tickets.add(new Ticket("Bruxelles", "Danzig", 9, deckName));
        tickets.add(new Ticket("Brest", "Marseille", 7, deckName));
        tickets.add(new Ticket("Zágráb", "Brindisi", 6, deckName));
        tickets.add(new Ticket("Barcelona", "Bruxelles", 8, deckName));
        tickets.add(new Ticket("Frankfurt", "Smolensk", 13, deckName));
        tickets.add(new Ticket("Rostov", "Erzurum", 5, deckName));
        tickets.add(new Ticket("Marseille", "Essen", 8, deckName));
        tickets.add(new Ticket("Smolensk", "Rostov", 8, deckName));
        tickets.add(new Ticket("Kyїv", "Sochi", 8, deckName));
        tickets.add(new Ticket("Kyїv", "Petrograd", 6, deckName));
        tickets.add(new Ticket("Budapest", "Sofia", 5, deckName));
        tickets.add(new Ticket("Venezia", "Constantinople", 10, deckName));
        tickets.add(new Ticket("Palermo", "Constantinople", 8, deckName));
        tickets.add(new Ticket("Brest", "Venezia", 8, deckName));
        tickets.add(new Ticket("Zürich", "Brindisi", 6, deckName));
        tickets.add(new Ticket("Amsterdam", "Pamplona", 7, deckName));
        tickets.add(new Ticket("Barcelona", "München", 8, deckName));
        tickets.add(new Ticket("Essen", "Kyїv", 10, deckName));
        tickets.add(new Ticket("Warszawa", "Smolensk", 6, deckName));
        tickets.add(new Ticket("Riga", "Bucuresti", 10, deckName));
        tickets.add(new Ticket("Sarajevo", "Sevastopol", 8, deckName));
        tickets.add(new Ticket("Frankfurt", "København", 5, deckName));
        tickets.add(new Ticket("Amsterdam", "Wilno", 12, deckName));
        tickets.add(new Ticket("Sofia", "Smyrna", 5, deckName));
        return tickets;
    }

    private List<Ticket> addTicketsBaseLong(String deckName) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket("København", "Erzurum", 21, deckName));
        tickets.add(new Ticket("Cádiz", "Stockholm", 21, deckName));
        tickets.add(new Ticket("Brest", "Petrograd", 20, deckName));
        tickets.add(new Ticket("Palermo", "Moskva", 20, deckName));
        tickets.add(new Ticket("Edinburgh", "Athína", 21, deckName));
        tickets.add(new Ticket("Lisboa", "Danzig", 20, deckName));
        return tickets;
    }

    private List<Ticket> addTicketsBaseBigCities(String deckName) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket("Roma", "Smyrna", 8, deckName));
        tickets.add(new Ticket("Berlin", "Roma", 9, deckName));
        tickets.add(new Ticket("Berlin", "Moskva", 12, deckName));
        tickets.add(new Ticket("Paris", "Zágráb", 7, deckName));
        tickets.add(new Ticket("London", "Berlin", 7, deckName));
        tickets.add(new Ticket("Paris", "Wien", 8, deckName));
        tickets.add(new Ticket("London", "Wien", 10, deckName));
        tickets.add(new Ticket("Athína", "Wilno", 11, deckName));
        tickets.add(new Ticket("Madrid", "Zürich", 8, deckName));
        tickets.add(new Ticket("Athína", "Angora", 5, deckName));
        tickets.add(new Ticket("Berlin", "Bucuresti", 8, deckName));
        tickets.add(new Ticket("Madrid", "Dieppe", 8, deckName));
        tickets.add(new Ticket("Stockholm", "Wien", 11, deckName));
        tickets.add(new Ticket("Edinburgh", "Paris", 7, deckName));
        tickets.add(new Ticket("Angora", "Kharkov", 10, deckName));
        return tickets;
    }

    private List<Ticket> addTickets1912Short(String deckName) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket("Edinburgh", "Essen", 9, deckName));
        tickets.add(new Ticket("Sofia", "Kyїv", 6, deckName));
        tickets.add(new Ticket("Sochi", "Smyrna", 9, deckName));
        tickets.add(new Ticket("Riga", "Kharkov", 10, deckName));
        tickets.add(new Ticket("Pamplona", "Palermo", 12, deckName));
        tickets.add(new Ticket("München", "Sarajevo", 7, deckName));
        tickets.add(new Ticket("Bucuresti", "Erzurum", 7, deckName));
        tickets.add(new Ticket("Amsterdam", "Venezia", 6, deckName));
        tickets.add(new Ticket("Venezia", "Warszawa", 8, deckName));
        tickets.add(new Ticket("Warszawa", "Budapest", 5, deckName));
        tickets.add(new Ticket("München", "Petrograd", 14, deckName));
        tickets.add(new Ticket("Warszawa", "Sevastopol", 12, deckName));
        tickets.add(new Ticket("Stockholm", "Wilno", 12, deckName));
        tickets.add(new Ticket("Danzig", "Budapest", 7, deckName));
        tickets.add(new Ticket("Dieppe", "København", 9, deckName));
        tickets.add(new Ticket("Cádiz", "Frankfurt", 13, deckName));
        tickets.add(new Ticket("Bruxelles", "Stockholm", 10, deckName));
        tickets.add(new Ticket("Lisboa", "Cádiz", 2, deckName));
        tickets.add(new Ticket("Dieppe", "Marseille", 5, deckName));
        return tickets;
    }

    private List<Ticket> addTickets1912Long(String deckName) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket("Essen", "Angora", 16, deckName));
        tickets.add(new Ticket("London", "Sochi", 20, deckName));
        tickets.add(new Ticket("Pamplona", "Kyїv", 18, deckName));
        tickets.add(new Ticket("Paris", "Sevastopol", 17, deckName));
        tickets.add(new Ticket("Riga", "Brindisi", 17, deckName));
        tickets.add(new Ticket("Amsterdam", "Rostov", 19, deckName));
        return tickets;
    }

    private List<Ticket> addTickets1912BigCities(String deckName) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket("Paris", "Moskva", 18, deckName));
        tickets.add(new Ticket("Paris", "Berlin", 7, deckName));
        tickets.add(new Ticket("London", "Paris", 3, deckName));
        tickets.add(new Ticket("Wien", "Roma", 6, deckName));
        tickets.add(new Ticket("Roma", "Moskva", 17, deckName));
        tickets.add(new Ticket("Paris", "Roma", 10, deckName));
        tickets.add(new Ticket("Wien", "Angora", 10, deckName));
        tickets.add(new Ticket("Madrid", "Angora", 21, deckName));
        tickets.add(new Ticket("Madrid", "Wien", 13, deckName));
        tickets.add(new Ticket("London", "Madrid", 10, deckName));
        tickets.add(new Ticket("London", "Angora", 20, deckName));
        tickets.add(new Ticket("Moskva", "Angora", 14, deckName));
        tickets.add(new Ticket("Moskva", "Athína", 14, deckName));
        tickets.add(new Ticket("Madrid", "Moskva", 25, deckName));
        tickets.add(new Ticket("London", "Roma", 10, deckName));
        tickets.add(new Ticket("Berlin", "Angora", 13, deckName));
        tickets.add(new Ticket("Madrid", "Roma", 10, deckName));
        tickets.add(new Ticket("Madrid", "Athína", 16, deckName));
        tickets.add(new Ticket("Wien", "Athína", 8, deckName));
        tickets.add(new Ticket("Paris", "Madrid", 7, deckName));
        tickets.add(new Ticket("London", "Athína", 16, deckName));
        tickets.add(new Ticket("Berlin", "Athína", 11, deckName));
        tickets.add(new Ticket("Roma", "Athína", 6, deckName));
        tickets.add(new Ticket("Madrid", "Berlin", 13, deckName));
        tickets.add(new Ticket("Berlin", "Wien", 3, deckName));
        tickets.add(new Ticket("Wien", "Moskva", 12, deckName));
        tickets.add(new Ticket("Paris", "Athína", 13, deckName));
        tickets.add(new Ticket("London", "Moskva", 19, deckName));
        tickets.add(new Ticket("Roma", "Angora", 11, deckName));
        tickets.add(new Ticket("Paris", "Angora", 17, deckName));
        return tickets;
    }
}
