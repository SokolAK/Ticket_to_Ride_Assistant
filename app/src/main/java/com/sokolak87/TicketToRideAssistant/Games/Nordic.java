package com.sokolak87.TicketToRideAssistant.Games;

import android.content.Context;

import com.sokolak87.TicketToRideAssistant.Logic.DbReader;
import com.sokolak87.TicketToRideAssistant.Logic.Game;
import com.sokolak87.TicketToRideAssistant.R;
import com.sokolak87.TicketToRideAssistant.Util.Triplet;

public class Nordic extends Game {
    public Nordic(Context context, String title) {
        super(context, title);

        //GENERAL
        //------------------------------------------------------------------------------------------
        numberOfCars = 40;
        startCards = 4;
        maxNoOfCardsToDraw = 2;
        maxExtraCardsForTunnel = 3;

        //ROUTES
        //------------------------------------------------------------------------------------------
        scoring.put(1, 1);
        scoring.put(2, 2);
        scoring.put(3, 4);
        scoring.put(4, 7);
        scoring.put(5, 10);
        scoring.put(6, 15);
        scoring.put(9, 27);

        //TICKETS
        //------------------------------------------------------------------------------------------
        databaseName = "TtRA_Nordic.db";
        databaseVersion = 1;
        routes = DbReader.readRoutes(context, databaseName, databaseVersion);
        ticketsDecks.add(Triplet.create
                ("Tickets_Base",
                        context.getString(R.string.TtRA_Nordic_Tickets_Base),
                        true));
        updateTickets();
    }
}
