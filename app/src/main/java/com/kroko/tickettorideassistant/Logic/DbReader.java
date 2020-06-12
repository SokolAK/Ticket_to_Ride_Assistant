package com.kroko.TicketToRideAssistant.Logic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public final class DbReader {

    public static ArrayList<Ticket> readTickets(Context context, String databaseName, int databaseVersion, String tableName) {
        DbHelper dbHelper = DbHelper.getInstance(context, databaseName, databaseVersion);
        dbHelper.checkDatabase();
        //dbHelper.openDatabase();
        //SQLiteDatabase database = dbHelper.getReadableDatabase();
        SQLiteDatabase database = dbHelper.openDatabase();

        ArrayList<Ticket> tickets = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
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

    public static ArrayList<Route> readRoutes(Context context, String databaseName, int databaseVersion) {
        DbHelper dbHelper = DbHelper.getInstance(context, databaseName, databaseVersion);
        dbHelper.checkDatabase();
        //dbHelper.openDatabase();
        SQLiteDatabase database = dbHelper.getReadableDatabase();

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
}
