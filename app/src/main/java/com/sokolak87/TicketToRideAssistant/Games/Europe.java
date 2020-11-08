package com.sokolak87.TicketToRideAssistant.Games;

import android.content.Context;

import com.sokolak87.TicketToRideAssistant.Logic.DbReader;
import com.sokolak87.TicketToRideAssistant.Logic.Game;
import com.sokolak87.TicketToRideAssistant.R;
import com.sokolak87.TicketToRideAssistant.Util.Triplet;

public class Europe extends Game {
    public Europe(Context context, String title) {
        super(context, title);

        //GENERAL
        //------------------------------------------------------------------------------------------
        numberOfCars = 45;
        startCards = 4;
        maxNoOfCardsToDraw = 2;
        numberOfStations = 3;
        stationPoints = 4;
        maxExtraCardsForTunnel = 3;
        stationsAvailable = true;
        warehousesAvailable = true;

        //ROUTES
        //------------------------------------------------------------------------------------------
        scoring.put(1, 1);
        scoring.put(2, 2);
        scoring.put(3, 4);
        scoring.put(4, 7);
        scoring.put(6, 15);
        scoring.put(8, 21);

        //STATIONS
        //------------------------------------------------------------------------------------------
        stationCost.put(1, 1);
        stationCost.put(2, 2);
        stationCost.put(3, 3);

        //TICKETS
        //------------------------------------------------------------------------------------------
        databaseName = "TtRA_Europe.db";
        databaseVersion = 1;
        routes = DbReader.readRoutes(context, databaseName, databaseVersion);
        ticketsDecks.add(Triplet.create
                ("Tickets_Base_Long",
                        context.getString(R.string.TtRA_Europe_Tickets_Base_Long),
                        true));
        ticketsDecks.add(Triplet.create
                ("Tickets_Base_Short",
                        context.getString(R.string.TtRA_Europe_Tickets_Base_Short),
                        true));
        ticketsDecks.add(Triplet.create
                ("Tickets_Base_BigCities",
                        context.getString(R.string.TtRA_Europe_Tickets_Base_BigCities),
                        true));
        ticketsDecks.add(Triplet.create
                ("Tickets_1912_Long",
                        context.getString(R.string.TtRA_Europe_Tickets_1912_Long),
                        false));
        ticketsDecks.add(Triplet.create
                ("Tickets_1912_Short",
                        context.getString(R.string.TtRA_Europe_Tickets_1912_Short),
                        false));
        ticketsDecks.add(Triplet.create
                ("Tickets_1912_BigCities",
                        context.getString(R.string.TtRA_Europe_Tickets_1912_BigCities),
                        false));
        updateTickets();
    }
}
