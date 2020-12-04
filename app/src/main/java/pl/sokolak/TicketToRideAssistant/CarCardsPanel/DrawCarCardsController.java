package pl.sokolak.TicketToRideAssistant.CarCardsPanel;

import java.util.List;


public class DrawCarCardsController extends CarCardsController {
    private final int maxCards;

    public DrawCarCardsController() {
        this(99);
    }

    public DrawCarCardsController(int maxCards) {
        this.maxCards = maxCards;
    }

    @Override
    public void updateAdd(List<CarCardTile> carCardTiles) {
        int noCards = 0;
        for (CarCardTile carCardTile : carCardTiles) {
            noCards += carCardTile.getAmount();
            carCardTile.setActive(true);
        }

        if (noCards == maxCards) {
            for (CarCardTile carCardTile : carCardTiles) {
                carCardTile.setActive(false);
            }
        }
    }
}
