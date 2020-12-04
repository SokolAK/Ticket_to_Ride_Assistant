package pl.sokolak.TicketToRideAssistant.Domain;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Data;
import pl.sokolak.TicketToRideAssistant.Calculators.DefaultPointsCalculator;
import pl.sokolak.TicketToRideAssistant.Calculators.PointsCalculator;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardsController;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.RouteCarCardsController;
import pl.sokolak.TicketToRideAssistant.Database.DbReader;
import pl.sokolak.TicketToRideAssistant.Games.Europe.Europe;
import pl.sokolak.TicketToRideAssistant.Games.Nordic.Nordic;
import pl.sokolak.TicketToRideAssistant.Games.USA.USA;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Triplet;


@Data
public abstract class Game {
    private int id;
    protected String title;
    protected int startCards;
    protected int maxNoOfCardsToDraw;
    protected int numberOfStations = 0;
    protected int stationPoints = 0;
    protected int numberOfCars;
    protected int maxExtraCardsForTunnel;
    protected int carsToLocoTradeRatio;
    protected boolean warehousesAvailable;
    protected boolean stationsAvailable;

    protected CarCardsController stationCarCardController;
    protected CarCardsController routeCarCardController = new RouteCarCardsController();

    private HashMap<Integer, Integer> scoring = new HashMap<>();
    protected HashMap<Integer, Integer> stationCost = new HashMap<>();
    private List<CarCardColor> carCardColors = new ArrayList<>();
    private List<Route> routes = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();
    protected String databaseName;
    protected int databaseVersion;
    private Context context;
    protected List<Triplet<String, String, Boolean>> ticketsDecks = new ArrayList<>();
    private long ticketHash;

    protected PointsCalculator pointsCalculator = new DefaultPointsCalculator(this);


    public Game(Context context) {
        this.context = context;
    }

    public static Game create(Context context, int gamePosition) {
        Game game = null;
        switch (gamePosition) {
            case 0:
                game = new USA(context);
                break;
            case 1:
                game = new Europe(context);
                break;
            case 2:
                game = new Nordic(context);
                break;
        }
        if (game != null) {
            game.setTitle(context.getResources().getStringArray(R.array.games)[gamePosition]);
        }

        return game;
    }

    public void prepareBaseGame(String title) {
        setTitle(title);
        setCarCards();
        routes = DbReader.readRoutes(context, databaseName, databaseVersion);
        scoring = DbReader.readScoring(context, databaseName, databaseVersion);
    }

    public void setCarCards() {
        carCardColors.add(CarCardColor.VIOLET);
        carCardColors.add(CarCardColor.ORANGE);
        carCardColors.add(CarCardColor.BLUE);
        carCardColors.add(CarCardColor.YELLOW);
        carCardColors.add(CarCardColor.BLACK);
        carCardColors.add(CarCardColor.GREEN);
        carCardColors.add(CarCardColor.RED);
        carCardColors.add(CarCardColor.WHITE);
        carCardColors.add(CarCardColor.LOCO);
    }

    public void updateTickets() {
        long newTicketHash = calculateTicketsHash();
        if (ticketHash != newTicketHash) {
            ticketHash = newTicketHash;
            tickets.clear();
            for (Triplet<String, String, Boolean> deck : ticketsDecks) {
                if (deck.third) {
                    tickets.addAll(DbReader.readTickets(context, databaseName, databaseVersion, (String) deck.first));
                }
            }
        }
    }

    private long calculateTicketsHash() {
        long hash = 0;
        for (Triplet<String, String, Boolean> deck : ticketsDecks) {
            int c = deck.third ? 1 : 0;
            hash = 31 * hash + c;
        }
        return hash;
    }

    public List<Ticket> getTickets(String city1) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getCity1().equals(city1) || ticket.getCity2().equals(city1)) {
                result.add(ticket);
            }
        }
        return result;
    }

    public Ticket getTicket(int id) {
        for (Ticket ticket : tickets) {
            if (ticket.getId() == id) {
                return ticket;
            }
        }
        return null;
    }

    public Route getRoute(int id) {
        for (Route route : routes) {
            if (route.getId() == id) {
                return route;
            }
        }
        return null;
    }

    public List<Route> getRoutes(String city1, boolean checkIfBuilt, boolean checkIfBuiltStation) {
        List<Route> result = new ArrayList<>();
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

    public List<Route> getRoutes(String city1, String city2, boolean checkIfBuilt, boolean checkIfBuiltStation) {
        List<Route> result = new ArrayList<>();
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
