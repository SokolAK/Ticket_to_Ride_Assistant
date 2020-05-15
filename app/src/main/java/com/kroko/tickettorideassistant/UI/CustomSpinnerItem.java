package com.kroko.TicketToRideAssistant.UI;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomSpinnerItem {
    private String city;
    private int image;
    private int routeId;

    public int compareTo(CustomSpinnerItem y) {
        int comp = city.compareTo(y.getCity());
        if(comp != 0) {
            return comp;
        }
        else {
            return image - y.getImage();
        }
    }
}
