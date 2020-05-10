package com.kroko.TicketToRideAssistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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

                numberOfCars = 45;
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

                cards.add(new Card("violet", R.drawable.violet));
                cards.add(new Card("orange", R.drawable.orange));
                cards.add(new Card("blue", R.drawable.blue));
                cards.add(new Card("yellow", R.drawable.yellow));
                cards.add(new Card("black", R.drawable.black));
                cards.add(new Card("green", R.drawable.green));
                cards.add(new Card("red", R.drawable.red));
                cards.add(new Card("white", R.drawable.white));
                cards.add(new Card("loco", R.drawable.loco));

                break;
        }
    }
}
