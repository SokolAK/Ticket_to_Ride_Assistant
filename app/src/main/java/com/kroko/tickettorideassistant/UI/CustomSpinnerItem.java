package com.kroko.TicketToRideAssistant.UI;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomSpinnerItem {
    private String text;
    private int imageResource;
    private int itemId;

    public int compareTo(CustomSpinnerItem y) {
        int comp = text.compareTo(y.getText());
        if(comp != 0) {
            return comp;
        }
        else {
            return imageResource - y.getImageResource();
        }
    }
}
