package com.sokolak87.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.sokolak87.TicketToRideAssistant.Logic.Game;
import com.sokolak87.TicketToRideAssistant.Logic.Player;
import com.sokolak87.TicketToRideAssistant.R;
import com.sokolak87.TicketToRideAssistant.Logic.TtRA_Application;
import com.sokolak87.TicketToRideAssistant.UI.CardsCarFragment;

public class DrawFragment extends Fragment implements View.OnClickListener {
    private Game game;
    private Player player;
    private int[] cardCounter;
    private int[] cardsNumbers;
    private int maxCards;
    private String title;

    public DrawFragment() {
    }

    public DrawFragment(int maxCards, String title) {
        this.maxCards = maxCards;
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        game = ((TtRA_Application) requireActivity().getApplication()).game;
        player = ((TtRA_Application) requireActivity().getApplication()).player;
        cardCounter = new int[1];
        cardsNumbers = new int[game.getCards().size()];
        for (int i = 0; i < game.getCards().size(); ++i) {
            game.getCards().get(i).setClickable(1);
            game.getCards().get(i).setVisible(1);
            cardsNumbers[i] = 0;
        }

        game = ((TtRA_Application) requireActivity().getApplication()).game;
        player = ((TtRA_Application) requireActivity().getApplication()).player;

        View drawer = inflater.inflate(R.layout.fragment_draw_cards, container, false);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(title);

        ImageView acceptIcon = drawer.findViewById(R.id.accept_button);
        acceptIcon.setOnClickListener(this);
        ImageView resetIcon = drawer.findViewById(R.id.reset_button);
        resetIcon.setOnClickListener(this);

        TextView drawCardsLabel = drawer.findViewById(R.id.drawCards_label);
        TextView drawCardsValue = drawer.findViewById(R.id.drawCards_value);
        if (maxCards != 0) {
            drawCardsValue.setText(String.valueOf(maxCards));
            drawCardsLabel.setText(R.string.draw_cards_label);
        } else {
            drawCardsValue.setText("");
            drawCardsLabel.setText("");
        }

        refreshCards();
        return drawer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.accept_button:
                if (cardCounter[0] == 0) {
                    String text = getString(R.string.too_little_cards);
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                } else {
                    player.addCards(cardsNumbers);
                    clearDrawCards();
                    refreshCards();
                    returnToTopPage();
                }
                break;

            case R.id.reset_button:
                clearDrawCards();
                refreshCards();
                break;
        }
    }

    private void clearDrawCards() {
        cardCounter[0] = 0;
        for (int i = 0; i < game.getCards().size(); ++i) {
            game.getCards().get(i).setClickable(1);
            game.getCards().get(i).setVisible(1);
            cardsNumbers[i] = 0;
        }
    }

    private void refreshCards() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CardsCarFragment cardsCarFragment = CardsCarFragment.builder().cardsNumbers(cardsNumbers).
                cardCounter(cardCounter).maxCards(maxCards).
                active(true).activeLong(true).
                build();
        ft.replace(R.id.cards_container, cardsCarFragment);
        ft.commit();
    }

    private void returnToTopPage() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}