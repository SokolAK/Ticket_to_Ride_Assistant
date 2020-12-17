package pl.sokolak.TicketToRideAssistant.Games;

import android.content.Context;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Triplet;

public class Nordic extends Game {
    public Nordic(Context context) {
        super(context);

        databaseName = "TtRA_Nordic.db";
        databaseVersion = 1;
        prepareBaseGame("Nordic Countries");

        //TICKETS
        //------------------------------------------------------------------------------------------
        ticketsDecks.add(Triplet.create
                ("Tickets_Base",
                        context.getString(R.string.TtRA_Nordic_Tickets_Base),
                        true));
        updateTickets();
    }
}
