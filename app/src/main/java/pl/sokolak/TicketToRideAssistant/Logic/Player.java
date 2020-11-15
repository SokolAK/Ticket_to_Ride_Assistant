package pl.sokolak.TicketToRideAssistant.Logic;

import pl.sokolak.TicketToRideAssistant.Util.Triplet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Player implements Serializable {
    private Game game;

    private int points;
    private int numberOfStations;
    private int numberOfCars;
    private int longestPathLength;
    private int[] cardsNumbers;
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
        cardsNumbers = new int[game.getCards().size()];
        for (int i = 0; i < game.getCards().size(); ++i) {
            cardsNumbers[i] = 0;
        }
    }

    public void addCards(int[] cardsNumbers) {
        for (int i = 0; i < this.cardsNumbers.length; ++i) {
            this.cardsNumbers[i] += cardsNumbers[i];
        }
    }

    public void spendCards(int[] cardsNumbers) {
        for (int i = 0; i < this.cardsNumbers.length; ++i) {
            this.cardsNumbers[i] -= cardsNumbers[i];
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
        checkIfTicketsRealized();
    }

    public void removeRoute(int i) {
        builtRoutes.remove(i);
        longestPathLength = findLongestPath();
        checkIfTicketsRealized();
    }

    public void addRouteStation(Route route) {
        builtStations.add(0, route);
        numberOfStations--;
        longestPathLength = findLongestPath();
        checkIfTicketsRealized();
    }

    public void removeRouteStation(int i) {
        builtStations.remove(i);
        numberOfStations++;
        longestPathLength = findLongestPath();
        checkIfTicketsRealized();
    }

    public void addTicket(Ticket ticket) {
        tickets.add(0, ticket);
        ticket.setInHand(true);
    }

    public void removeTicket(int i) {
        tickets.get(i).setInHand(false);
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
        points = 0;
        for (Ticket ticket : tickets) {
            if (ticket.isRealized()) {
                points += ticket.getPoints();
            } else {
                points -= ticket.getPoints();
            }
        }
        for (Route route : builtRoutes) {
            if (game.getScoring().containsKey(route.getLength())) {
                @SuppressWarnings("ConstantConditions") int routePoints = game.getScoring().get(route.getLength());
                points += routePoints;
            }
        }
        points += numberOfStations * game.getStationPoints();
    }

    private int findLongestPath() {
        ConnectionCalculator connectionCalc = new ConnectionCalculator(this);
        return connectionCalc.findLongestPath();
    }
}

