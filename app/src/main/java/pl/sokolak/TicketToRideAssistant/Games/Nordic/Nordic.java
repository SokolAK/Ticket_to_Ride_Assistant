package pl.sokolak.TicketToRideAssistant.Games.Nordic;

import android.content.Context;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Games.USA.USAPointsCalculator;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Triplet;

public class Nordic extends Game {
    Context context;

    public Nordic(Context context) {
        super(context);
        this.context = context;

        databaseName = "TtRA_Nordic.db";
        databaseVersion = 1;

        title = "Nordic Countries";
        startCards = 4;
        maxNoOfCardsToDraw = 2;
        numberOfCars = 40;
        carsToLocoTradeRatio = 3;

        prepareBaseGame();
        addTicketDecks();
        pointsCalculator = new NordicPointsCalculator(this);
    }

    private void addTicketDecks() {
        ticketsDecks.add(Triplet.create
                ("Tickets_Base",
                        context.getString(R.string.TtRA_Nordic_Tickets_Base),
                        true));
    }
}
