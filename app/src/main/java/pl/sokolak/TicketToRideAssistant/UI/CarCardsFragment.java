package pl.sokolak.TicketToRideAssistant.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.TtRA_Application;

import lombok.Builder;

@Builder
public class CarCardsFragment extends Fragment {
    @Builder.Default
    private final int[] cardCounter = new int[1];
    private final int maxCards;
    private final int[] cardsNumbers;
    private final boolean active;
    private final boolean activeLong;
    private final boolean oneColor;
    private final int[] maxCardsNumbers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Game game = ((TtRA_Application) requireActivity().getApplication()).game;
        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        RecyclerView cardRecycler = view.findViewById(R.id.card_recycler);

        int[] cardImages = new int[game.getCards().size()];
        for (int i = 0; i < cardImages.length; i++) {
            cardImages[i] = game.getCards().get(i).getImageResourceId();
        }

        CardImageAdapter adapter = new CardImageAdapter(game.getCards(), cardsNumbers);
        cardRecycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 3, GridLayoutManager.HORIZONTAL, false);
        cardRecycler.setLayoutManager(layoutManager);

        adapter.setListener(position -> {
            if (active) {
                if (maxCardsNumbers != null) {
                    if (cardsNumbers[position] == maxCardsNumbers[position]) {
                        game.getCards().get(position).setClickable(false);
                    }
                }
                if (game.getCards().get(position).isVisible()) {
                    if (cardCounter[0] < maxCards || maxCards == 0) {
                        cardCounter[0]++;
                        cardsNumbers[position]++;
                    }
                    if (oneColor) {
                        if (game.getCards().get(position).getColor() != 'L') {
                            for (int i = 0; i < game.getCards().size(); ++i) {
                                if (i != position && game.getCards().get(i).getColor() != 'L') {
                                    game.getCards().get(i).setClickable(false);
                                    game.getCards().get(i).setVisible(false);
                                }
                            }
                        }
                    }
                }
                refreshPage();
            }
        });
        adapter.setListenerLong(position -> {
            if (activeLong) {
                if (cardsNumbers[position] > 0) {
                    cardsNumbers[position]--;
                }
                if (cardCounter[0] > 0) {
                    cardCounter[0]--;
                }
                refreshPage();
            }
        });

        return view;
    }

    private void refreshPage() {
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        CarCardsFragment carCardsFragment = CarCardsFragment.builder().cardsNumbers(cardsNumbers).
                cardCounter(cardCounter).maxCards(maxCards).maxCardsNumbers(maxCardsNumbers).
                active(active).activeLong(activeLong).oneColor(oneColor).
                build();
        ft.replace(R.id.cards_container, carCardsFragment);
        ft.commit();
    }
}