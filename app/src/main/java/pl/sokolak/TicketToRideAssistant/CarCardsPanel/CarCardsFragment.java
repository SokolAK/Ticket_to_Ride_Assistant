package pl.sokolak.TicketToRideAssistant.CarCardsPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pl.sokolak.TicketToRideAssistant.R;

public class CarCardsFragment extends Fragment {
    private final List<CarCardTile> carCardTiles;
    @Getter @Setter
    private List<CarCardsObserver> carCardsObservers = new ArrayList<>();
    @Getter
    private final CarCardsController carCardsController;

    public CarCardsFragment(List<CarCardTile> carCardTiles, CarCardsController carCardsController) {
        this.carCardTiles = carCardTiles;
        this.carCardsController = carCardsController;
        this.carCardsController.init(carCardTiles);
    }

    private void sendNotificationToObservers() {
        for(CarCardsObserver carCardsObserver : carCardsObservers) {
            carCardsObserver.updateCarCards(carCardTiles);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        CardImageAdapter adapter = configureCardsImages(view);


        adapter.setListener(position -> {
            CarCardTile tile = carCardTiles.get(position);
            carCardsController.updateAdd(carCardTiles);
            if (tile.isActive()) {
                tile.setAmount(tile.getAmount() + 1);
                sendNotificationToObservers();
            }
        });

        adapter.setListenerLong(position -> {
            carCardsController.updateRemove(carCardTiles);
            CarCardTile tile = carCardTiles.get(position);
            if (tile.isActiveLong()) {
                tile.setAmount(tile.getAmount() - 1);
                sendNotificationToObservers();
            }
        });

        return view;
    }

    private CardImageAdapter configureCardsImages(View view) {
        RecyclerView cardRecycler = view.findViewById(R.id.card_recycler);
        CardImageAdapter adapter = new CardImageAdapter(carCardTiles);
        cardRecycler.setAdapter(adapter);

        int spanCount = 1;
        while(carCardTiles.size() > spanCount*3)
            spanCount++;
        GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), spanCount, GridLayoutManager.HORIZONTAL, false);
        cardRecycler.setLayoutManager(layoutManager);
        return adapter;
    }
}