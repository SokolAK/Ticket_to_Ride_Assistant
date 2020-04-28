package com.kroko.tickettorideassistant;

public class Card {
    private String name;
    private int imageResourceId;

    public static final Card[] cards = {
            new Card("violet", R.drawable.violet),
            new Card("orange", R.drawable.orange),
            new Card("blue", R.drawable.blue),
            new Card("yellow", R.drawable.yellow),
            new Card("black", R.drawable.black),
            new Card("green", R.drawable.green),
            new Card("red", R.drawable.red),
            new Card("white", R.drawable.white),
            new Card("loco", R.drawable.loco)
    };

    private Card(String name, int imageResourceId) {
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
