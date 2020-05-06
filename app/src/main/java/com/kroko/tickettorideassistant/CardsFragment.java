package com.kroko.TicketToRideAssistant;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

public class CardsFragment extends Fragment implements View.OnClickListener {

    private int cardCounter;
    private int[] cardNumbers = new int[9];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View drawer = inflater.inflate(R.layout.fragment_cards, container, false);
        RecyclerView cardRecycler = drawer.findViewById(R.id.cards);

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

            if (cardCounter < game.getMaxNoOfCardsToDraw()) {
                cardCounter++;
                cardNumbers[position]+=2;
                refreshPage();
            }
            else
            {
                String text = getString(R.string.too_much) + " " + String.valueOf(game.getMaxNoOfCardsToDraw());
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

        ImageView acceptIcon = drawer.findViewById(R.id.accept_icon);
        acceptIcon.setOnClickListener(this);
        ImageView resetIcon = drawer.findViewById(R.id.reset_icon);
        resetIcon.setOnClickListener(this);

        TextView maxNoDrawCards = drawer.findViewById(R.id.drawCards_value);
        maxNoDrawCards.setText(String.valueOf(game.getMaxNoOfCardsToDraw()));

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_drawCards);

        return drawer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_icon:
                if(cardCounter == 0) {
                    String text = getString(R.string.too_little);
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
                else {
                    Player player = ((TtRA_Application) getActivity().getApplication()).player;
                    for (int i = 0; i < cardNumbers.length; i++) {
                        String color = Card.cards[i].getName();
                        int cardNumber = player.getCards().get(color) + cardNumbers[i];
                        player.getCards().put(color, cardNumber);
                    }
                    clearDrawCards();
                    refreshPage();
                    returnToTopPage();
                }
                break;
            case R.id.reset_icon:
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
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new CardsFragment(cardCounter,cardNumbers));
        ft.commit();
    }

    private void returnToTopPage() {
        ((MainActivity)getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public CardsFragment() {
    }
    public CardsFragment(int cardCounter, int[] cardNumbers) {
        this.cardCounter = cardCounter;
        this.cardNumbers = cardNumbers;
    }
}