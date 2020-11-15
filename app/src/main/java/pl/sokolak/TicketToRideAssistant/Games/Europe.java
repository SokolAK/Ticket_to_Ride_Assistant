package pl.sokolak.TicketToRideAssistant.Games;

import android.content.Context;

import pl.sokolak.TicketToRideAssistant.Logic.DbReader;
import pl.sokolak.TicketToRideAssistant.Logic.Game;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Triplet;

public class Europe extends Game {
    public Europe(Context context, String title) {
        super(context, title);

        databaseName = "TtRA_Europe.db";
        databaseVersion = 1;
        prepareBaseGame();
        stationCost = DbReader.readStationCost(context, databaseName, databaseVersion);

        //TICKETS
        //------------------------------------------------------------------------------------------
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
