package com.kroko.TicketToRideAssistant;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.navigation.NavigationView;

public class CardsFragment extends Fragment {
    private Game game;
    private Player player;

    boolean clickable;
    private SharedViewModel viewModel;
    private int[] cardCounter;
    private int maxCards;
    private int[] cardNumbers;
    private int[] availableCards;


    public CardsFragment() {

    }

    public CardsFragment(int[] cardNumbers) {
        this();
        this.cardNumbers = cardNumbers;
        this.availableCards = new int[cardNumbers.length];
        for (int i = 0; i < this.availableCards.length; ++i)
            this.availableCards[i] = 1;
    }

    public CardsFragment(int[] cardCounter, int[] cardNumbers, int maxCards, boolean clickable) {
        this(cardNumbers);
        this.cardCounter = cardCounter;
        this.maxCards = maxCards;
        this.clickable = clickable;
        this.availableCards = new int[cardNumbers.length];
        for (int i = 0; i < this.availableCards.length; ++i)
            this.availableCards[i] = 1;
    }

    public CardsFragment(int[] cardCounter, int[] cardNumbers, int maxCards, int[] availableCards, boolean clickable) {
        this(cardNumbers);
        this.cardCounter = cardCounter;
        this.maxCards = maxCards;
        this.clickable = clickable;
        this.availableCards = availableCards;
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

        CardImageAdapter adapter = new CardImageAdapter(cardImages, cardNumbers, availableCards);
        cardRecycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
        cardRecycler.setLayoutManager(layoutManager);

        adapter.setListener(position -> {
            if (clickable && availableCards[position] == 1) {
                if (cardCounter[0] < maxCards) {
                    for (int i = 0; i < availableCards.length; ++i) {
                        if(i != position && !game.getCards().get(position).getName().equals("loco")  && !game.getCards().get(i).getName().equals("loco")  ) {
                            availableCards[i] = 0;
                        }
                    }

                    cardCounter[0]++;
                    //viewModel.setCardCounter(cardCounter);
                    cardNumbers[position]++;

                    refreshPage();
                } else {
                    String text = getString(R.string.too_much) + " " + maxCards;
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        //toolbar.setTitle(R.string.nav_drawCards);

        return drawer;
    }

    private void refreshPage() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cards_container, new CardsFragment(cardCounter, cardNumbers, maxCards, availableCards, clickable));
        ft.commit();
    }

    private void returnToTopPage() {
        ((MainActivity) getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

}