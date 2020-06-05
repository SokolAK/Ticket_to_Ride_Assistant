package com.kroko.TicketToRideAssistant.UI;

import lombok.AllArgsConstructor;
import lombok.Data;

import static java.util.Comparator.comparing;

@Data
@AllArgsConstructor
public class CustomItem {
    private String text;
    private int imageResource;
    private int itemId;

    public int compareTo(CustomItem y) {
        int comp = text.compareTo(y.getText());
        if(comp == 0) {
            comp = Integer.compare(imageResource, y.getImageResource());
        }
        return comp;
    }
}
