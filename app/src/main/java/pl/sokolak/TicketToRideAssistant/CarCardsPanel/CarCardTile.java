package pl.sokolak.TicketToRideAssistant.CarCardsPanel;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.sokolak.TicketToRideAssistant.Domain.CarCardColor;

@Data
@AllArgsConstructor
public class CarCardTile {
    private CarCardColor carCardColor;
    private int amount;
    private boolean active;
    private boolean activeLong;
    private boolean visible;
}
