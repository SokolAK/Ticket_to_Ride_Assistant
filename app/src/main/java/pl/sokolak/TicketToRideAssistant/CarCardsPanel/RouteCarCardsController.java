package pl.sokolak.TicketToRideAssistant.CarCardsPanel;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import pl.sokolak.TicketToRideAssistant.Domain.CarCardColor;
import pl.sokolak.TicketToRideAssistant.Domain.Route;
import pl.sokolak.TicketToRideAssistant.R;

public class RouteCarCardsController extends CarCardsController {

    @Override
    public void init(List<CarCardTile> carCardTiles) {
    }

    @Override
    public void updateAdd(List<CarCardTile> carCardTiles) {
        int noLocos = 0;
        int noCards = 0;
        int maxNoCards = route.isTunnel() ? game.getMaxExtraCardsForTunnel() : 0;
        maxNoCards += route.getLength();

        for (CarCardTile carCardTile : carCardTiles) {
            if (carCardTile.isVisible()) {
                if (carCardTile.getCarCardColor().equals(CarCardColor.LOCO)) {
                    noLocos = carCardTile.getAmount();
                }
                noCards += carCardTile.getAmount();

                if (carCardTile.getAmount() >= player.getCarCards().get(carCardTile.getCarCardColor())) {
                    carCardTile.setActive(false);
                }
            }
        }

        if (noCards - noLocos >= maxNoCards - route.getLocos()) {
            for (CarCardTile carCardTile : carCardTiles) {
                if (carCardTile.isVisible()) {
                    if (!carCardTile.getCarCardColor().equals(CarCardColor.LOCO)) {
                        carCardTile.setActive(false);
                    }
                }
            }
        }

        if (noCards >= maxNoCards) {
            for (CarCardTile carCardTile : carCardTiles) {
                if (carCardTile.isVisible()) {
                    carCardTile.setActive(false);
                }
            }
        }
    }

    @Override
    public void updateRemove(List<CarCardTile> carCardTiles) {
        int noLocos = 0;
        int noCards = 0;
        for (CarCardTile carCardTile : carCardTiles) {
            if (carCardTile.isVisible()) {
                carCardTile.setActive(true);
            }
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
        if (player.getNumberOfCars() < route.getLength()) {
            Toast.makeText(context, R.string.too_little_cars, Toast.LENGTH_SHORT).show();
            return false;
        }

        int noCards = 0;
        for (CarCardTile carCardTile : carCardTiles) {
            noCards += carCardTile.getAmount();
        }

        CarCardColor color = route.getColor();
        noCards = 0;
        for (CarCardTile carCardTile : carCardTiles) {
            if (color.equals(CarCardColor.NONE) ||
                    carCardTile.getCarCardColor().equals(color) ||
                    carCardTile.getCarCardColor().equals(CarCardColor.LOCO))
                noCards += carCardTile.getAmount();
        }
        if (noCards >= route.getLength())
            return true;

        Toast.makeText(context, R.string.too_little_cards, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void setCardsVisibility(List<CarCardTile> carCardTiles, Route route) {
        for (CarCardTile carCardTile : carCardTiles) {
            if (route.getColor().equals(CarCardColor.NONE) ||
                    carCardTile.getCarCardColor().equals(route.getColor()) ||
                    carCardTile.getCarCardColor().equals(CarCardColor.LOCO)) {
                carCardTile.setActive(true);
                carCardTile.setActiveLong(true);
                carCardTile.setVisible(true);
            } else {
                carCardTile.setActive(false);
                carCardTile.setActiveLong(false);
                carCardTile.setVisible(false);
            }
        }
    }
}
