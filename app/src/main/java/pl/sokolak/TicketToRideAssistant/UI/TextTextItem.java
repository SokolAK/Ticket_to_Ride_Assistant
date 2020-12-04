package pl.sokolak.TicketToRideAssistant.UI;

import lombok.Data;

@Data
public class TextTextItem {
    private String text1;
    private String text2;
    private int textSize;

    public TextTextItem(String text1, String text2) {
        this.text1 = text1;
        this.text2 = text2;
    }
}
