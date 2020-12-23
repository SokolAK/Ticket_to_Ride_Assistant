package pl.sokolak.TicketToRideAssistant.Util;

import pl.sokolak.TicketToRideAssistant.UI.Card;

public class ColorConverter {
    public static char CarCardColorToChar(Card.CarCardColor carCardColor) {
        switch (carCardColor) {
            case BLUE:
                return 'B';
            case RED:
                return 'R';
            case BLACK:
                return 'A';
            case GREEN:
                return 'G';
            case WHITE:
                return 'W';
            case ORANGE:
                return 'O';
            case VIOLET:
                return 'V';
            case YELLOW:
                return 'Y';
            case LOCO:
                return 'L';
            default:
                return '-';
        }
    }

    public static Card.CarCardColor CharToCarCardColor(char c) {
        switch (c) {
            case 'B':
                return Card.CarCardColor.BLUE;
            case 'R':
                return Card.CarCardColor.RED;
            case 'A':
                return Card.CarCardColor.BLACK;
            case 'G':
                return Card.CarCardColor.GREEN;
            case 'W':
                return Card.CarCardColor.WHITE;
            case 'O':
                return Card.CarCardColor.ORANGE;
            case 'V':
                return Card.CarCardColor.VIOLET;
            case 'Y':
                return Card.CarCardColor.YELLOW;
            case 'L':
                return Card.CarCardColor.LOCO;
            default:
                return Card.CarCardColor.NONE;
        }
    }
}