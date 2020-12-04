package pl.sokolak.TicketToRideAssistant.Util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardTile;
import pl.sokolak.TicketToRideAssistant.Domain.CarCardColor;

public class carCardsConverter {
    public static Map<CarCardColor, Integer> carCardsTilesToCarCards(List<CarCardTile> carCardTiles) {
        Map<CarCardColor, Integer> carCards = new LinkedHashMap<>();
        for (CarCardTile carCardTile : carCardTiles) {
            carCards.put(carCardTile.getCarCardColor(), carCardTile.getAmount());
        }
        return carCards;
    }
}
