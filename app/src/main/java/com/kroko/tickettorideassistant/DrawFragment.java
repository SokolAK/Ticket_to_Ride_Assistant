package com.kroko.tickettorideassistant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

public class DrawFragment extends Fragment implements View.OnClickListener {

    private int cardCounter;
    private int[] cardNumbers = new int[9];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View drawer = inflater.inflate(R.layout.fragment_draw, container, false);
        RecyclerView cardRecycler = drawer.findViewById(R.id.cards);

        Player player = ((TtRA_Application) getActivity().getApplication()).player;
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

            if (cardCounter < game.getDrawCards()) {
                //String color = Card.cards[position].getName();
                //int numberOfCards = player.getCards().get(color);
                //player.getCards().put(color, numberOfCards + 1);
                cardCounter++;
                cardNumbers[position]++;

                refreshPage();
            }
        });
        Button resetButton = drawer.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);
        Button acceptButton = drawer.findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(this);

        return drawer;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            cardCounter = savedInstanceState.getInt("cardCounter");
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("cardCounter", cardCounter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_button:
                Player player = ((TtRA_Application) getActivity().getApplication()).player;
                for (int i = 0; i < cardNumbers.length; i++) {
                    String color = Card.cards[i].getName();
                    int cardNumber = player.getCards().get(color) + cardNumbers[i];
                    player.getCards().put(color, cardNumber);
                }
                clearDrawCards();
                refreshPage();
                break;
            case R.id.reset_button:
                clearDrawCards();
                refreshPage();
                break;
        }
    }

    private void clearDrawCards() {
        cardCounter = 0;
        cardNumbers = new int[9];
    }
    private void refreshPage() {
        ViewPager pager = getActivity().findViewById(R.id.pager);
        pager.getAdapter().notifyDataSetChanged();
    }
}