package pl.sokolak.TicketToRideAssistant.Util;

import pl.sokolak.TicketToRideAssistant.Domain.CarCardColor;

public class ColorConverter {
    public static char CarCardColorToChar(CarCardColor carCardColor) {
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

    public static CarCardColor CharToCarCardColor(char c) {
        switch (c) {
            case 'B':
                return CarCardColor.BLUE;
            case 'R':
                return CarCardColor.RED;
            case 'A':
                return CarCardColor.BLACK;
            case 'G':
                return CarCardColor.GREEN;
            case 'W':
                return CarCardColor.WHITE;
            case 'O':
                return CarCardColor.ORANGE;
            case 'V':
                return CarCardColor.VIOLET;
            case 'Y':
                return CarCardColor.YELLOW;
            case 'L':
                return CarCardColor.LOCO;
            default:
                return CarCardColor.NONE;
        }
    }
}
