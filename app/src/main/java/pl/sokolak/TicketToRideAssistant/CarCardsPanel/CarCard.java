package pl.sokolak.TicketToRideAssistant.CarCardsPanel;

import lombok.Data;
import pl.sokolak.TicketToRideAssistant.Domain.CarCardColor;

@Data
public class CarCard {
    private CarCardColor carCardColor;
    private int clickable;
    private int visible;
    private int amount;

    public CarCard(CarCardColor carCardColor) {
        this.carCardColor = carCardColor;
        this.clickable = 0;
        this.visible = 1;
    }
}
