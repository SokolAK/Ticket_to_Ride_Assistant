package com.kroko.TicketToRideAssistant;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Data;

@Data
public class Game {
    private String title;
    private int startCards;
    private int maxNoOfCardsToDraw;
    private int numberOfStations;
    private int stationPoints;
    private int numberOfCars;
    private HashMap<Integer, Integer> scoring = new HashMap<>();
    private HashMap<Integer, Integer> stationCost = new HashMap<>();
    private ArrayList<Card> cards = new ArrayList<>();

    public Game() {
    }

    public void prepare(int gameId) {
        switch (gameId) {
            case 0:
                title = "Ticket to Ride. Europe";

                numberOfCars = 10;
                startCards = 4;
                maxNoOfCardsToDraw = 2;
                numberOfStations = 3;
                stationPoints = 4;

                scoring.put(1, 1);
                scoring.put(2, 2);
                scoring.put(3, 4);
                scoring.put(4, 7);
                scoring.put(6, 15);
                scoring.put(8, 21);

                stationCost.put(1, 1);
                stationCost.put(2, 2);
                stationCost.put(3, 3);

                cards.add(new Card('V', R.drawable.violet));
                cards.add(new Card('O', R.drawable.orange));
                cards.add(new Card('B', R.drawable.blue));
                cards.add(new Card('Y', R.drawable.yellow));
                cards.add(new Card('A', R.drawable.black));
                cards.add(new Card('G', R.drawable.green));
                cards.add(new Card('R', R.drawable.red));
                cards.add(new Card('W', R.drawable.white));
                cards.add(new Card('L', R.drawable.loco));

                break;
        }
    }
}
