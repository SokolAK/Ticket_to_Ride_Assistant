package pl.sokolak.TicketToRideAssistant.Games.Europe;

import android.content.Context;

import pl.sokolak.TicketToRideAssistant.Database.DbReader;
import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Games.USA.USAPointsCalculator;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.Util.Triplet;

public class Europe extends Game {
    Context context;

    public Europe(Context context) {
        super(context);
        this.context = context;

        databaseName = "TtRA_Europe.db";
        databaseVersion = 1;

        title = "Europe";
        startCards = 4;
        maxNoOfCardsToDraw = 2;
        numberOfStations = 3;
        stationPoints = 4;
        numberOfCars = 45;
        maxExtraCardsForTunnel = 3;
        warehousesAvailable = true;
        stationsAvailable = false;

        prepareBaseGame();
        stationCost = DbReader.readStationCost(context, databaseName, databaseVersion);
        addTicketDecks();
        pointsCalculator = new EuropePointsCalculator(this);
    }

    private void addTicketDecks() {
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
