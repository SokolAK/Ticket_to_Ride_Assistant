package pl.sokolak.TicketToRideAssistant.Calculators;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.Util.Pair;

public interface StatusCalculator extends Serializable {
    List<Pair<String, String>> getStatusList(Context context);
    void setPlayer(Player player);
}
