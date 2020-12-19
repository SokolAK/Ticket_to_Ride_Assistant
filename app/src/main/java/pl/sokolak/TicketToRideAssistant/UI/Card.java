package pl.sokolak.TicketToRideAssistant.UI;

import lombok.Data;

@Data
public class Card {
    private char color;
    private int imageResourceId;
    private boolean  clickable;
    private boolean visible;

    public Card(char color, int imageResourceId) {
        this.color = color;
        this.imageResourceId = imageResourceId;
        this.clickable = false;
        this.visible = true;
    }
}
