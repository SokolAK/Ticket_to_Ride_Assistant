package com.kroko.TicketToRideAssistant;

import java.io.Serializable;
import java.util.HashMap;

import lombok.Data;

@Data
public class Player implements Serializable {
    private Game game;

    private int[] cards;
    private int points;
    private int stations;
    private int cars;

    public void setGame(Game game) {
        this.game = game;
    }

    public void prepare() {
        cars = game.getNumberOfCars();
        stations = game.getNumberOfStations();
        points = stations*game.getStationPoints();
        cards = new int[game.getCards().size()];
        for(int i=0; i<cards.length; ++i) {
            cards[i] = 0;
        }
    }

}

