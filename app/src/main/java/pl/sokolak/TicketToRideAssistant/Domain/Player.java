package pl.sokolak.TicketToRideAssistant.Domain;

import pl.sokolak.TicketToRideAssistant.Calculators.ConnectionCalculator;
import pl.sokolak.TicketToRideAssistant.Calculators.PointsCalculator;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardTile;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardsObserver;
import pl.sokolak.TicketToRideAssistant.Util.Triplet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Player implements Serializable, CarCardsObserver {
    private Game game;

    private int points;
    private int numberOfStations;
    private int numberOfCars;
    private int longestPathLength;
    private int[] cardsNumbers; //TO REMOVE
    private Map<CarCardColor, Integer> carCards = new LinkedHashMap<>();
    private List<Route> builtRoutes = new ArrayList<>();
    private List<Route> builtStations = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();

    public void setGame(Game game) {
        this.game = game;
    }

    public void prepare() {
        numberOfCars = game.getNumberOfCars();
        numberOfStations = game.getNumberOfStations();
        points = numberOfStations * game.getStationPoints();
        cardsNumbers = new int[game.getCarCardColors().size()];
        for (int i = 0; i < game.getCarCardColors().size(); ++i) {
            cardsNumbers[i] = 0;
        }

        for(CarCardColor carCardColor : game.getCarCardColors()) {
            carCards.put(carCardColor, 0);
        }
    }

    public void addCards(Map<CarCardColor, Integer> carCards) {
        for (Map.Entry<CarCardColor, Integer> card : carCards.entrySet()) {
            CarCardColor key = card.getKey();
            int value = getCarCards().get(key) + card.getValue();
            getCarCards().put(key, value);
        }
    }

    public void spendCards(Map<CarCardColor, Integer> carCards) {
        for (Map.Entry<CarCardColor, Integer> card : carCards.entrySet()) {
            CarCardColor key = card.getKey();
            int value = getCarCards().get(key) - card.getValue();
            getCarCards().put(key, value);
        }
    }

    public void addCars(int cars) {
        this.numberOfCars += cars;
    }

    public void spendCars(int cars) {
        this.numberOfCars -= cars;
    }

    public void addRoute(Route route) {
        builtRoutes.add(0, route);
        //updatePoints();
        longestPathLength = findLongestPath();
        updatePoints();
        checkIfTicketsRealized();
    }

    public void removeRoute(int i) {
        builtRoutes.remove(i);
        longestPathLength = findLongestPath();
        updatePoints();
        checkIfTicketsRealized();
    }

    public void addRouteStation(Route route) {
        builtStations.add(0, route);
        numberOfStations--;
        updatePoints();
        //longestPathLength = findLongestPath();
        checkIfTicketsRealized();
    }

    public void removeRouteStation(int i) {
        builtStations.remove(i);
        numberOfStations++;
        updatePoints();
        //longestPathLength = findLongestPath();
        checkIfTicketsRealized();
    }

    public void addTicket(Ticket ticket) {
        tickets.add(0, ticket);
        updatePoints();
        ticket.setInHand(true);
    }

    public void removeTicket(int i) {
        tickets.get(i).setInHand(false);
        updatePoints();
        tickets.remove(i);
    }

    public void updateTickets() {
        for (int i = 0; i < tickets.size(); ++i) {
            for (Triplet deck : game.getTicketsDecks()) {
                if (tickets.get(i).getDeckName().equals(deck.first)) {
                    if (!(Boolean) deck.third) {
                        removeTicket(i);
                        --i;
                        break;
                    }
                }
            }
        }
        checkIfTicketsRealized();
    }

    public void checkIfTicketsRealized() {
        for (Ticket ticket : game.getTickets()) {
            boolean realized = ticket.checkIfRealized(this);
            ticket.setRealized(realized);
        }
    }

    public void updatePoints() {
        PointsCalculator pointsCalculator = game.getPointsCalculator();
        //pointsCalculator.setPlayer(this);
        points = pointsCalculator.sumPoints();
    }

    private int findLongestPath() {
        ConnectionCalculator connectionCalc = new ConnectionCalculator(this);
        return connectionCalc.findLongestPath();
    }

    @Override
    public void updateCarCards(List<CarCardTile> carCardTiles) {
        for(CarCardTile carCardTile : carCardTiles) {
            carCards.put(carCardTile.getCarCardColor(), carCardTile.getAmount());
        }
    }
}

