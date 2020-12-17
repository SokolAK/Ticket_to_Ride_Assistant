package pl.sokolak.TicketToRideAssistant.Games;

import android.content.Context;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Triplet;

public class USA extends Game {
    public USA(Context context) {
        super(context);

        databaseName = "TtRA_USA.db";
        databaseVersion = 1;
        prepareBaseGame("USA");

        //TICKETS
        //------------------------------------------------------------------------------------------
        ticketsDecks.add(Triplet.create
                ("Tickets_Base",
                        context.getString(R.string.TtRA_USA_Tickets_Base),
                        true));
        updateTickets();
    }
}
