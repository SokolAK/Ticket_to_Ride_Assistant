package com.kroko.TicketToRideAssistant.Logic;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

@Data
public class Player implements Serializable {
    private Game game;

    private int points;
    private int numberOfStations;
    private int numberOfCars;
    private int[] cardsNumbers;
    private ArrayList<Route> builtRoutes = new ArrayList<>();
    private ArrayList<Route> builtStations = new ArrayList<>();
    private ArrayList<Ticket> tickets = new ArrayList<>();

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

    //public void addPoints(int points) {
    //   this.points += points;
    //}
    //public void spendPoints(int points) {
      //  this.points -= points;
    //}

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
        builtRoutes.add(route);
        updatePoints();
    }
    public void removeRoute(int i) {
        builtRoutes.remove(i);
        updatePoints();
    }

    public void addRouteStation(Route route) {
        builtStations.add(route);
        numberOfStations--;
        updatePoints();
    }
    public void removeRouteStation(int i) {
        builtStations.remove(i);
        numberOfStations++;
        updatePoints();
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        updatePoints();
    }
    public void removeTicket(int i) {
        tickets.remove(i);
        updatePoints();
    }
    public void checkIfTicketsRealized() {
        for(Ticket ticket: tickets) {
            boolean realized = ticket.checkIfRealized(this);
            ticket.setRealized(realized);
        }
        updatePoints();
    }

    @SuppressWarnings("ConstantConditions")
    public void updatePoints() {
        points = 0;
        for(Ticket ticket: tickets) {
            if(ticket.isRealized()) {
                points += ticket.getPoints();
            } else {
                points -= ticket.getPoints();
            }
        }
        for(Route route: builtRoutes) {
            points += game.getScoring().get(route.getLength());
        }
        points += numberOfStations*game.getStationPoints();
    }
}

