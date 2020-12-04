package pl.sokolak.TicketToRideAssistant.Games.Nordic;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardTile;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.RouteCarCardsController;
import pl.sokolak.TicketToRideAssistant.Domain.CarCardColor;
import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Route;
import pl.sokolak.TicketToRideAssistant.R;

public class NordicRouteCarCardsController extends RouteCarCardsController {
    public NordicRouteCarCardsController(Game game) {
        this.game = game;
    }

    @Override
    public void updateAdd(List<CarCardTile> carCardTiles) {
        int noLocos = 0;
        int noCards = 0;
        int maxNoCards = route.isTunnel() ? game.getMaxExtraCardsForTunnel() : 0;
        maxNoCards += route.getLength();
        maxNoCards += route.getLocos() * 2;

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

        if (noCards - noLocos >= maxNoCards + 2 * route.getLocos()) {
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
    public boolean isConditionMet(Context context, List<CarCardTile> carCardTiles) {
        int maxNoCards = route.isTunnel() ? game.getMaxExtraCardsForTunnel() : 0;
        maxNoCards += route.getLength();
        maxNoCards += route.getLocos() * 2;

        if (player.getNumberOfCars() < maxNoCards) {
            Toast.makeText(context, R.string.too_little_cars, Toast.LENGTH_SHORT).show();
            return false;
        }

        int noCards = 0;
        for (CarCardTile carCardTile : carCardTiles) {
            noCards += carCardTile.getAmount();
        }

        noCards = 0;
        for (CarCardTile carCardTile : carCardTiles) {
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
            if (route.getLocos() > 0 ||
                    route.getColor().equals(CarCardColor.NONE) ||
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