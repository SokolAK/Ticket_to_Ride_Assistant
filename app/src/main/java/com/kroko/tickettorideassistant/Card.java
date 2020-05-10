package com.kroko.TicketToRideAssistant;

public class Card {
    private String name;
    private int imageResourceId;

    public Card(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }
    public int getImageResourceId() {
        return imageResourceId;
    }
}
