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
    private ArrayList<Ticket> ticketsShort = new ArrayList<>();
    private ArrayList<Ticket> ticketsLong = new ArrayList<>();

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

    public void addPoints(int points) {
        this.points += points;
    }
    public void spendPoints(int points) {
        this.points -= points;
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
    public void addStation(int stations) {
        this.numberOfStations += stations;
    }
    public void spendStation(int stations) {
        this.numberOfStations -= stations;
    }

    public void addCars(int cars) {
        this.numberOfCars += cars;
    }
    public void spendCars(int cars) {
        this.numberOfCars -= cars;
    }

    public void addRoute(Route route) {
        builtRoutes.add(route);
    }
    public void removeRoute(int i) {
        builtRoutes.remove(i);
    }

    public void addRouteStation(Route route) {
        builtStations.add(route);
    }
    public void removeRouteStation(int i) {
        builtStations.remove(i);
    }
}

