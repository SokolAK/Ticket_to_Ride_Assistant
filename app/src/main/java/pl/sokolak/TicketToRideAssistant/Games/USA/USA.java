package pl.sokolak.TicketToRideAssistant.Games.USA;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.Calculators.DefaultPointsCalculator;
import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Pair;
import pl.sokolak.TicketToRideAssistant.Util.Triplet;

public class USA extends Game {
    Context context;

    public USA(Context context) {
        super(context);
        this.context = context;

        databaseName = "TtRA_USA.db";
        databaseVersion = 1;

        title = "USA";
        startCards = 4;
        maxNoOfCardsToDraw = 2;
        numberOfCars = 45;

        prepareBaseGame(title);
        addTicketDecks();

        pointsCalculator = new USAPointsCalculator(this);
    }

    private void addTicketDecks() {
        ticketsDecks.add(Triplet.create
                ("Tickets_Base_Short",
                        context.getString(R.string.TtRA_USA_Tickets_Base_Short),
                        true));
        ticketsDecks.add(Triplet.create
                ("Tickets_Base_BigCities",
                        context.getString(R.string.TtRA_USA_Tickets_Base_BigCities),
                        true));
        ticketsDecks.add(Triplet.create
                ("Tickets_1910_Classic",
                        context.getString(R.string.TtRA_USA_Tickets_1910_Classic),
                        false));
        ticketsDecks.add(Triplet.create
                ("Tickets_1910_BigCities",
                        context.getString(R.string.TtRA_USA_Tickets_1910_BigCities),
                        false));
        updateTickets();
    }
}
