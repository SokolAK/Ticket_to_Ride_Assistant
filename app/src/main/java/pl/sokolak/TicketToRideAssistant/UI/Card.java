package pl.sokolak.TicketToRideAssistant.UI;

import lombok.Data;
import lombok.Getter;
import pl.sokolak.TicketToRideAssistant.R;

@Data
public class Card {
    //private char color;
    private CarCardColor carCardColor;
    //private int imageResourceId;
    private boolean activeShort;
    private boolean activeLong;

    private boolean clickable; //TO REMOVE
    private boolean visible;

    public Card(CarCardColor carCardColor, int imageResourceId) {
        this.carCardColor = carCardColor;
        //this.imageResourceId = imageResourceId;
        this.clickable = false;
        this.visible = true;
    }

//    public Card(char color, int imageResourceId) { //TO REMOVE
//        //this.color = color;
//        //this.imageResourceId = imageResourceId;
//        this.clickable = false;
//        this.visible = true;
//    }

    public enum CarCardColor {
        VIOLET(R.drawable.violet),
        ORANGE(R.drawable.orange),
        BLUE(R.drawable.blue),
        YELLOW(R.drawable.yellow),
        BLACK(R.drawable.black),
        GREEN(R.drawable.green),
        RED(R.drawable.red),
        WHITE(R.drawable.white),
        LOCO(R.drawable.loco),
        NONE(0);

        @Getter
        private int imageResourceId;

        CarCardColor(int imageResourceId) {
            this.imageResourceId = imageResourceId;
        }
    }
}
