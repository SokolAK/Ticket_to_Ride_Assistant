package com.sokolak87.TicketToRideAssistant.Games;

import android.content.Context;

import com.sokolak87.TicketToRideAssistant.Logic.DbReader;
import com.sokolak87.TicketToRideAssistant.Logic.Game;
import com.sokolak87.TicketToRideAssistant.R;
import com.sokolak87.TicketToRideAssistant.Util.Triplet;

public class USA extends Game {
    public USA(Context context, String title) {
        super(context, title);

        //GENERAL
        //------------------------------------------------------------------------------------------
        numberOfCars = 45;
        startCards = 4;
        maxNoOfCardsToDraw = 2;
        numberOfStations = 0;
        stationPoints = 0;
        maxExtraCardsForTunnel = 0;

        //ROUTES
        //------------------------------------------------------------------------------------------
        scoring.put(1, 1);
        scoring.put(2, 2);
        scoring.put(3, 4);
        scoring.put(4, 7);
        scoring.put(6, 15);

        //STATIONS
        //------------------------------------------------------------------------------------------
        //stationCost.put(1, 1);
        //stationCost.put(2, 2);
        //stationCost.put(3, 3);

        //TICKETS
        //------------------------------------------------------------------------------------------
        databaseName = "TtRA_USA.db";
        databaseVersion = 1;
        routes = DbReader.readRoutes(context, databaseName, databaseVersion);
        ticketsDecks.add(Triplet.create
                ("Tickets_Base",
                        context.getString(R.string.TtRA_USA_Tickets_Base),
                        true));
        updateTickets();
    }
}
