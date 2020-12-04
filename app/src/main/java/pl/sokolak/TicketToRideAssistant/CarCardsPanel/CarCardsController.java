package pl.sokolak.TicketToRideAssistant.CarCardsPanel;

import android.content.Context;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.Domain.Route;

public abstract class CarCardsController {
    @Getter @Setter
    protected Game game;
    @Getter @Setter
    protected Player player;
    @Getter @Setter
    protected Route route;

    public void updateAdd(List<CarCardTile> carCardTiles) {
    }

    public void updateRemove(List<CarCardTile> carCardTiles) {
        for (CarCardTile carCardTile : carCardTiles) {
            carCardTile.setActiveLong(carCardTile.getAmount() > 0);
        }
    }

    public void init(List<CarCardTile> carCardTiles) {
        updateAdd(carCardTiles);
        updateRemove(carCardTiles);
    }

    public boolean isConditionMet(Context context, List<CarCardTile> carCardTiles) {
        return false;
    }

    public void setCardsVisibility(List<CarCardTile> carCardTiles, Route route) {
    }
}
