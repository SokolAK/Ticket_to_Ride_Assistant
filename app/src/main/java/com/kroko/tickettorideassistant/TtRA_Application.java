package com.kroko.TicketToRideAssistant;

import android.app.Application;

public class TtRA_Application extends Application {

    public Game game = new Game(this);
    public Player player = new Player();
}
