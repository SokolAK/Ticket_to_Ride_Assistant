package com.kroko.TicketToRideAssistant;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    private int maxExtraCardsForTunnel;
    private HashMap<Integer, Integer> scoring = new HashMap<>();
    private HashMap<Integer, Integer> stationCost = new HashMap<>();
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Route> routes = new ArrayList<>();
    private String databaseName;
    private int databaseVersion;
    private Context context;

    public Game(Context context) {
        this.context = context;
    }

    public void prepare(int gameId) {
        switch (gameId) {
            case 0:
                title = "Ticket to Ride. Europe";

                numberOfCars = 20;
                startCards = 4;
                maxNoOfCardsToDraw = 10;
                numberOfStations = 3;
                stationPoints = 4;
                maxExtraCardsForTunnel = 3;

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

                databaseName = "TtRA_Europe.db";
                databaseVersion = 1;
                routes = readRoutes(databaseName, databaseVersion);

                break;
        }
    }

    private ArrayList<Route> readRoutes(String databaseName, int databaseVersion) {
        SQLiteDatabase database;
        DbHelper dbHelper = new DbHelper(context, databaseName, databaseVersion);
        dbHelper.checkDatabase();
        dbHelper.openDatabase();
        database = dbHelper.getReadableDatabase();

        ArrayList<Route> routes = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Routes", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String city1 = cursor.getString(1);
            String city2 = cursor.getString(2);
            int length = cursor.getInt(3);
            int locos = cursor.getInt(4);
            boolean tunnel = cursor.getInt(5) > 0;
            char color = cursor.getString(6).charAt(0);
            routes.add(new Route(id, city1, city2, length, locos, tunnel, color));
        }
        cursor.close();
        database.close();

        return routes;
    }

    public Route getRoute(int id) {
        for(Route route: routes) {
            if(route.get_id() == id) {
                return route;
            }
        }
        return null;
    }

    public void removeRoute(Route route) {
        int i = 0;
        int id = route.get_id();
        for(; i < routes.size(); ++ i) {
            if(routes.get(i).get_id() == id) {
                break;
            }
        }
        routes.remove(i);
    }
}
