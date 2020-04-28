package com.kroko.tickettorideassistant;

import java.util.HashMap;

import lombok.Data;

@Data
public class Game {
    private String title;
    private int startCards;
    private int drawCards;
    private int stations;
    private int stationPoints;
    private HashMap<Integer, Integer> scoring = new HashMap<>();

    public Game() {
    }

    public void prepare(int gameId) {
        switch (gameId) {
            case 0:
                title = "Ticket to Ride. Europe";

                startCards = 4;
                drawCards = 2;
                stations = 3;
                stationPoints = 4;

                scoring.put(1, 1);
                scoring.put(2, 2);
                scoring.put(3, 4);
                scoring.put(4, 7);
                scoring.put(6, 15);
                scoring.put(8, 21);

                break;
        }
    }
}
