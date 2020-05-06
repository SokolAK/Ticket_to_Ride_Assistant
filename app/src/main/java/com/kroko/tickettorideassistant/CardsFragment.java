package com.kroko.TicketToRideAssistant;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.navigation.NavigationView;

public class CardsFragment extends Fragment {
    private int cardCounter;
    private int maxCards;
    private int[] cardNumbers;
    private static final String MAX_CARDS = "maxCards";
    private static final String CARD_COUNTER = "cardCounter";
    private static final String CARD_NUMBERS = "cardNumbers";

    public static CardsFragment newInstance(int cardCounter, int[] cardNumbers, int maxCards) {
        CardsFragment cardsFragment = new CardsFragment();
        Bundle args = new Bundle();
        args.putInt("maxCards", maxCards);
        args.putInt("cardCounter", cardCounter);
        args.putIntArray("cardNumbers", cardNumbers);
        cardsFragment.setArguments(args);
        return cardsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View drawer = inflater.inflate(R.layout.fragment_cards2, container, false);
        cardCounter = getArguments().getInt(CARD_COUNTER);
        cardNumbers = getArguments().getIntArray(CARD_NUMBERS);
        maxCards = getArguments().getInt(MAX_CARDS);

        RecyclerView cardRecycler = drawer.findViewById(R.id.cards2);
        Game game = ((TtRA_Application) getActivity().getApplication()).game;

        int[] cardImages = new int[9];
        for (int i = 0; i < cardImages.length; i++) {
            cardImages[i] = Card.cards[i].getImageResourceId();
        }

        CardImageAdapter adapter = new CardImageAdapter(cardImages, cardNumbers);
        cardRecycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
        cardRecycler.setLayoutManager(layoutManager);

        adapter.setListener(position -> {

            if (cardCounter < maxCards) {
                cardCounter++;
                cardNumbers[position]++;
                refreshPage();
            } else {
                String text = getString(R.string.too_much) + " " + maxCards;
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_drawCards);

        return drawer;
    }


    private void clearDrawCards() {
        cardCounter = 0;
        cardNumbers = new int[9];
    }

    private void refreshPage() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cards_container, CardsFragment.newInstance(cardCounter, cardNumbers, maxCards));
        ft.commit();
    }

    private void returnToTopPage() {
        ((MainActivity) getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

}