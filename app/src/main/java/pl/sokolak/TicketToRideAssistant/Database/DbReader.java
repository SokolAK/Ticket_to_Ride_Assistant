package pl.sokolak.TicketToRideAssistant.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Route;
import pl.sokolak.TicketToRideAssistant.Domain.Ticket;

public final class DbReader {

    public static List<String> readGamesTitles(Context context, String databaseName, int databaseVersion) {
        DbHelper dbHelper = DbHelper.getInstance(context, databaseName, databaseVersion);
        dbHelper.checkDatabase();
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        List<String> gamesTitles = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Games", null);
        while (cursor.moveToNext()) {
            gamesTitles.add(cursor.getString(1));
        }
        cursor.close();
        database.close();
        return gamesTitles;
    }

    public static List<Ticket> readTickets(Context context, String databaseName, int databaseVersion, String tableName) {
        DbHelper dbHelper = DbHelper.getInstance(context, databaseName, databaseVersion);
        dbHelper.checkDatabase();
        SQLiteDatabase database = dbHelper.openDatabase();

        List<Ticket> tickets = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);
        while (cursor.moveToNext()) {
            String city1 = cursor.getString(1);
            String city2 = cursor.getString(2);
            int points = cursor.getInt(3);
            tickets.add(new Ticket(city1, city2, points, tableName));
        }
        cursor.close();
        database.close();
        dbHelper.close();
        return tickets;
    }

    public static List<Route> readRoutes(Context context, String databaseName, int databaseVersion) {
        DbHelper dbHelper = DbHelper.getInstance(context, databaseName, databaseVersion);
        dbHelper.checkDatabase();
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        List<Route> routes = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Routes", null);
        while (cursor.moveToNext()) {
            String city1 = cursor.getString(1);
            String city2 = cursor.getString(2);
            int length = cursor.getInt(3);
            int locos = cursor.getInt(4);
            boolean tunnel = cursor.getInt(5) > 0;
            char color = cursor.getString(6).charAt(0);
            routes.add(new Route(city1, city2, length, locos, tunnel, color));
        }
        cursor.close();
        database.close();
        return routes;
    }

    public static HashMap<Integer, Integer> readScoring(Context context, String databaseName, int databaseVersion) {
        DbHelper dbHelper = DbHelper.getInstance(context, databaseName, databaseVersion);
        dbHelper.checkDatabase();
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        HashMap<Integer, Integer> scoring = new HashMap<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Scoring", null);
        while (cursor.moveToNext()) {
            int routeLength = cursor.getInt(1);
            int score = cursor.getInt(2);
            scoring.put(routeLength, score);
        }
        cursor.close();
        database.close();
        return scoring;
    }

    public static HashMap<Integer, Integer> readStationCost(Context context, String databaseName, int databaseVersion) {
        DbHelper dbHelper = DbHelper.getInstance(context, databaseName, databaseVersion);
        dbHelper.checkDatabase();
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        HashMap<Integer, Integer> stationCost = new HashMap<>();
        Cursor cursor = database.rawQuery("SELECT * FROM StationCost", null);
        while (cursor.moveToNext()) {
            int stationNo = cursor.getInt(1);
            int cost = cursor.getInt(2);
            stationCost.put(stationNo, cost);
        }
        cursor.close();
        database.close();
        return stationCost;
    }

    public static Game readGeneralData(Context context, String databaseName, int databaseVersion, Game game) {
        DbHelper dbHelper = DbHelper.getInstance(context, databaseName, databaseVersion);
        dbHelper.checkDatabase();
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM Games", null);
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals(game.getTitle())) {
                game.setNumberOfCars(cursor.getInt(2));
                game.setStartCards(cursor.getInt(3));
                game.setMaxNoOfCardsToDraw(cursor.getInt(4));
                game.setMaxExtraCardsForTunnel(cursor.getInt(5));
                game.setStationsAvailable(cursor.getInt(6) > 0);
                game.setNumberOfStations(cursor.getInt(7));
                game.setStationPoints(cursor.getInt(8));
                game.setWarehousesAvailable(cursor.getInt(9) > 0);
                game.setCarsToLocoTradeRatio(cursor.getInt(10));
            }
        }
        cursor.close();
        database.close();
        return game;
    }
}
