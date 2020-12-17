package pl.sokolak.TicketToRideAssistant.UI;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class TextTextItem {
    private String text1;
    private String text2;
    //private int itemId;
    private int textSize;

    public TextTextItem(String text1, String text2) {
        this.text1 = text1;
        this.text2 = text2;
    }

    /*    public int compareTo(TextTextItem y) {
        int comp = text1.compareTo(y.getText1());
        if (comp == 0) {
            comp = text2.compareTo(y.getText2());
        }
        return comp;
    }*/
}
