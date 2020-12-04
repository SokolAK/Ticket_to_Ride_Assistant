package pl.sokolak.TicketToRideAssistant.Domain;

import lombok.Getter;
import pl.sokolak.TicketToRideAssistant.R;

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
