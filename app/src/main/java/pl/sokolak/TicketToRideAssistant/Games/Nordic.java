package pl.sokolak.TicketToRideAssistant.Games;

import android.content.Context;

import pl.sokolak.TicketToRideAssistant.Logic.Game;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Triplet;

public class Nordic extends Game {
    public Nordic(Context context, String title) {
        super(context, title);

        databaseName = "TtRA_Nordic.db";
        databaseVersion = 1;
        prepareBaseGame();

        //TICKETS
        //------------------------------------------------------------------------------------------
        ticketsDecks.add(Triplet.create
                ("Tickets_Base",
                        context.getString(R.string.TtRA_Nordic_Tickets_Base),
                        true));
        updateTickets();
    }
}
