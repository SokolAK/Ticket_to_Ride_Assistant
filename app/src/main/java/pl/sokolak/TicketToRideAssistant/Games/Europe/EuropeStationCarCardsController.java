package pl.sokolak.TicketToRideAssistant.Games.Europe;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardTile;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardsController;
import pl.sokolak.TicketToRideAssistant.Domain.CarCardColor;
import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.R;

public class EuropeStationCarCardsController extends CarCardsController {

    @Override
    public void init(List<CarCardTile> carCardTiles) {
        updateAdd(carCardTiles);
        updateRemove(carCardTiles);
    }

    @Override
    public void updateAdd(List<CarCardTile> carCardTiles) {
        int noLocos = 0;
        int noCards = 0;
        for (CarCardTile carCardTile : carCardTiles) {
            carCardTile.setVisible(true);
            if (carCardTile.getCarCardColor().equals(CarCardColor.LOCO)) {
                noLocos = carCardTile.getAmount();
            }
            noCards += carCardTile.getAmount();

            if(carCardTile.getAmount() >= player.getCarCards().get(carCardTile.getCarCardColor())) {
                carCardTile.setActive(false);
            }
        }
        if (noLocos < noCards) {
            for (CarCardTile carCardTile : carCardTiles) {
                if (!carCardTile.getCarCardColor().equals(CarCardColor.LOCO) && carCardTile.getAmount() == 0) {
                    carCardTile.setVisible(false);
                    carCardTile.setActive(false);
                }
            }
        }
        if (noCards >= game.getStationCost().get(game.getNumberOfStations() - player.getNumberOfStations() + 1)) {
            for (CarCardTile carCardTile : carCardTiles) {
                carCardTile.setActive(false);
            }
        }
    }

    @Override
    public void updateRemove(List<CarCardTile> carCardTiles) {
        int noLocos = 0;
        int noCards = 0;
        for (CarCardTile carCardTile : carCardTiles) {
            carCardTile.setVisible(true);
            carCardTile.setActive(true);
        }
        for (CarCardTile carCardTile : carCardTiles) {
            if (carCardTile.getCarCardColor().equals(CarCardColor.LOCO)) {
                noLocos = carCardTile.getAmount();
            }
            noCards += carCardTile.getAmount();
        }
        if (noLocos < noCards) {
            for (CarCardTile carCardTile : carCardTiles) {
                if (!carCardTile.getCarCardColor().equals(CarCardColor.LOCO) && carCardTile.getAmount() == 0) {
                    carCardTile.setVisible(false);
                    carCardTile.setActive(false);
                }
            }
        }
        for (CarCardTile carCardTile : carCardTiles) {
            carCardTile.setActiveLong(carCardTile.getAmount() > 0);
        }
    }

    @Override
    public boolean isConditionMet(Context context, List<CarCardTile> carCardTiles) {
        int noCards = 0;
        for (CarCardTile carCardTile : carCardTiles) {
            noCards += carCardTile.getAmount();
        }

        if (noCards == game.getStationCost().get(game.getNumberOfStations() - player.getNumberOfStations() + 1))
            return true;

        Toast.makeText(context, R.string.too_little_cards, Toast.LENGTH_SHORT).show();
        return false;
    }
}
