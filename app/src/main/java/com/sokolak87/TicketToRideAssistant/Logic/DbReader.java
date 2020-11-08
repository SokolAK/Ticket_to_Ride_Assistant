package com.sokolak87.TicketToRideAssistant.Logic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

public final class DbReader {

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
}
