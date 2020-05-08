package com.kroko.TicketToRideAssistant;

import java.io.Serializable;
import java.util.HashMap;

import lombok.Data;

@Data
public class Player implements Serializable {

    private HashMap<String, Integer> cards = new HashMap<>();
    private int points;
    private int stations;
    private int cars;

    public Player() {
        cards.put("violet", 0);
        cards.put("white", 0);
        cards.put("blue", 0);
        cards.put("yellow", 0);
        cards.put("orange", 0);
        cards.put("black", 0);
        cards.put("red", 0);
        cards.put("green", 0);
        cards.put("loco", 0);
    }

    public void prepare(Game game) {
        cars = game.getNumberOfCars();
        stations = game.getNumberOfStations();
        points = stations*game.getStationPoints();
    }

}
