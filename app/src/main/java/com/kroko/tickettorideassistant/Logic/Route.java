package com.kroko.TicketToRideAssistant.Logic;

import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.UI.Card;

import lombok.Data;

@Data
public class Route {
    private int _id;
    private String city1;
    private String city2;
    private int length;
    private int locos;
    private boolean tunnel;
    private char color;

    private boolean built;
    private char builtColor;
    private int[] builtCardsNumber;

    public Route(int _id, String city1, String city2, int length, int locos, boolean tunnel, char color) {
        this._id = _id;
        this.city1 = city1;
        this.city2 = city2;
        this.length = length;
        this.locos = locos;
        this.tunnel = tunnel;
        this.color = color;
        this.built = false;
    }

    public int getImageId(Game game, char color) {
        if(color == '-') {
            if(length > locos) {
                return R.drawable.any;
            }
            else {
                return R.drawable.loco;
            }
        }
        else {
            for (Card card : game.getCards()) {
                if (card.getColor() == color) {
                    return card.getImageResourceId();
                }
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return city1 + " - " + city2;
    }
}
