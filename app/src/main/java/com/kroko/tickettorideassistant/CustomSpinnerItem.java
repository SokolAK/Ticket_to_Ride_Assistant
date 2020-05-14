package com.kroko.TicketToRideAssistant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

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
