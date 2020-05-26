package com.kroko.TicketToRideAssistant.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Fragments.MainActivity;
import com.kroko.TicketToRideAssistant.Logic.Player;
import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.Logic.TtRA_Application;

public class CardsCarFragment extends Fragment {
    private int[] cardCounter;
    private int maxCards;
    private int[] cardsNumbers;
    private boolean active;
    private boolean activeLong;
    private boolean oneColor;
    private int[] maxCardsNumbers;

    public static class Builder {
        private final int[] cardsNumbers;
        private int[] cardCounter = new int[1];
        private int[] maxCardsNumbers;
        private int maxCards = 0;
        private boolean active = false;
        private boolean activeLong = false;
        private boolean oneColor = false;

        public Builder(int[] cardsNumbers) {
            this.cardsNumbers = cardsNumbers;
        }

        public Builder cardCounter(int[] cardCounter) {
            this.cardCounter = cardCounter;
            return this;
        }

        public Builder maxCardsNumbers(int[] maxCardsNumbers) {
            this.maxCardsNumbers = maxCardsNumbers;
            return this;
        }

        public Builder maxCards(int maxCards) {
            this.maxCards = maxCards;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder activeLong(boolean activeLong) {
            this.activeLong = activeLong;
            return this;
        }

        public Builder oneColor(boolean oneColor) {
            this.oneColor = oneColor;
            return this;
        }

        public CardsCarFragment build() {
            return new CardsCarFragment(this);
        }
    }

    private CardsCarFragment(Builder builder) {
        cardsNumbers = builder.cardsNumbers;
        cardCounter = builder.cardCounter;
        maxCardsNumbers = builder.maxCardsNumbers;
        maxCards = builder.maxCards;
        active = builder.active;
        activeLong = builder.activeLong;
        oneColor = builder.oneColor;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Game game = ((TtRA_Application) getActivity().getApplication()).game;
        Player player = ((TtRA_Application) getActivity().getApplication()).player;
        View drawer = inflater.inflate(R.layout.fragment_cards, container, false);
        RecyclerView cardRecycler = drawer.findViewById(R.id.card_recycler);

        int[] cardImages = new int[game.getCards().size()];
        for (int i = 0; i < cardImages.length; i++) {
            cardImages[i] = game.getCards().get(i).getImageResourceId();
        }

        CardImageAdapter adapter = new CardImageAdapter(game.getCards(), cardsNumbers);
        cardRecycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
        cardRecycler.setLayoutManager(layoutManager);

        adapter.setListener(position -> {
            if (active) {
                if (maxCardsNumbers != null) {
                    if (cardsNumbers[position] == maxCardsNumbers[position]) {
                        game.getCards().get(position).setClickable(0);
                    }
                }
                if (game.getCards().get(position).getClickable() == 1) {
                    if (cardCounter[0] < maxCards || maxCards == 0) {
                        cardCounter[0]++;
                        cardsNumbers[position]++;
                    }
                    if (oneColor) {
                        if (game.getCards().get(position).getColor() != 'L') {
                            for (int i = 0; i < game.getCards().size(); ++i) {
                                if (i != position && game.getCards().get(i).getColor() != 'L') {
                                    game.getCards().get(i).setClickable(0);
                                    game.getCards().get(i).setVisible(0);
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

        return drawer;
    }

    private void refreshPage() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        CardsCarFragment cardsCarFragment = new Builder(cardsNumbers).
                cardCounter(cardCounter).maxCards(maxCards).maxCardsNumbers(maxCardsNumbers).
                active(active).activeLong(activeLong).oneColor(oneColor).
                build();
        ft.replace(R.id.cards_container, cardsCarFragment);
        ft.commit();
    }

    private void returnToTopPage() {
        ((MainActivity) getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }


}