package pl.sokolak.TicketToRideAssistant.CarCardsPanel;

import java.util.List;

public interface CarCardsObserver {
    void updateCarCards(List<CarCardTile> carCardTiles);
}
