package pl.sokolak.TicketToRideAssistant.Domain;

import java.util.LinkedHashMap;
import java.util.Map;

import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCard;
import pl.sokolak.TicketToRideAssistant.R;

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
    private CarCardColor color;

    private boolean built;
    private CarCardColor builtColor;
    private int[] builtCardsNumber;
    private Map<CarCardColor, Integer> builtCarCards = new LinkedHashMap<>();

    private boolean builtStation;
    private CarCardColor builtStationColor;
    private int[] builtStationCardsNumber;
    private Map<CarCardColor, Integer> builtStationCarCards = new LinkedHashMap<>();

    public Route(String city1, String city2, int length, int locos, boolean tunnel, CarCardColor color) {
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

    @Override
    public String toString() {
        return city1 + "-" + city2;
    }
}
