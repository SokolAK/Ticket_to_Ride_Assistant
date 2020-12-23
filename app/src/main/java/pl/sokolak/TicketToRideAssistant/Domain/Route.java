package pl.sokolak.TicketToRideAssistant.Domain;

import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.UI.Card;

import lombok.Data;

@Data
public class Route {
    private static int counter;
    private int id;
    private String city1;
    private String city2;
    private int length;
    private int locos;
    private boolean tunnel;
    private boolean ferry;
    private Card.CarCardColor color;

    private boolean built;
    private Card.CarCardColor builtColor;
    private int[] builtCardsNumber;

    private boolean builtStation;
    private Card.CarCardColor builtStationColor;
    private int[] builtStationCardsNumber;

    public Route(String city1, String city2, int length, int locos, boolean tunnel, Card.CarCardColor color) {
        counter++;
        id = counter;
        this.city1 = city1;
        this.city2 = city2;
        this.length = length;
        this.locos = locos;
        this.tunnel = tunnel;
        this.color = color;
        this.built = false;
        this.builtStation = false;
        this.ferry = locos > 0;
    }

    public int getImageId(Game game, Card.CarCardColor carCardColor) {
        if(color == Card.CarCardColor.NONE) {
            if(length > locos) {
                return R.drawable.any;
            }
            else {
                return R.drawable.loco;
            }
        }
        else {
            for (Card card : game.getCards()) {
                if (card.getCarCardColor() == carCardColor) {
                    return card.getCarCardColor().getImageResourceId();
                }
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return city1 + "-" + city2;
    }
}
