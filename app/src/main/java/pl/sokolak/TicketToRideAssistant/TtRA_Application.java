package pl.sokolak.TicketToRideAssistant;

import android.app.Application;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;

public class TtRA_Application extends Application {
    public Game game;
    public Player player = new Player();
}
