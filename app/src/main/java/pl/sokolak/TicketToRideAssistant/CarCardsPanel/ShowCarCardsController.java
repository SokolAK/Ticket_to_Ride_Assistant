package pl.sokolak.TicketToRideAssistant.CarCardsPanel;

import java.util.List;
import java.util.Map;

import pl.sokolak.TicketToRideAssistant.Domain.CarCardColor;

public class ShowCarCardsController extends CarCardsController {

    @Override
    public void init(List<CarCardTile> carCardTiles) {
        carCardTiles.clear();
        for (Map.Entry<CarCardColor, Integer> card : player.getCarCards().entrySet()) {
            carCardTiles.add(new CarCardTile(card.getKey(), card.getValue(), false, false, true));
        }
    }
}
