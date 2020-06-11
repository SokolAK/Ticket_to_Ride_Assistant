package com.kroko.TicketToRideAssistant.Logic;

import android.app.Application;

import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Logic.Player;

public class TtRA_Application extends Application {

    public Game game = null;
    public Player player = new Player();
}
