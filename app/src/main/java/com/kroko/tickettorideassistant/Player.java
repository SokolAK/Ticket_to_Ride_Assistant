package com.kroko.TicketToRideAssistant;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

@Data
public class Player implements Serializable {
    private Game game;

    private int points;
    private int stations;
    private int cars;
    private int[] cardsNumbers;
    private ArrayList<Route> builtRoutes = new ArrayList<>();

    public void setGame(Game game) {
        this.game = game;
    }

    public void prepare() {
        cars = game.getNumberOfCars();
        stations = game.getNumberOfStations();
        points = stations * game.getStationPoints();
        cardsNumbers = new int[game.getCards().size()];
        for (int i = 0; i < game.getCards().size(); ++i) {
            cardsNumbers[i] = 0;
        }
    }

    public void addPoints(int points) {
        this.points += points;
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

    public void spendCars(int cars) {
        this.cars -= cars;
    }

    public void addRoute(Route route) {
        builtRoutes.add(route);
    }
}

