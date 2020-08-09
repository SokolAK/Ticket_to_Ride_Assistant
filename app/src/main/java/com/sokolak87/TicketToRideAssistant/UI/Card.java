package com.sokolak87.TicketToRideAssistant.UI;

import lombok.Data;

@Data
public class Card {
    private char color;
    private int imageResourceId;
    private int clickable;
    private int visible;

    public Card(char color, int imageResourceId) {
        this.color = color;
        this.imageResourceId = imageResourceId;
        this.clickable = 0;
        this.visible = 1;
    }
}
