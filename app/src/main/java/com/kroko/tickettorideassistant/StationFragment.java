package com.kroko.TicketToRideAssistant;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.navigation.NavigationView;


public class StationFragment extends Fragment implements View.OnClickListener {

    private int cardCounter;
    private int numberOfStation;
    private int stationCost;
    private int[] cardNumbers = new int[9];

    public StationFragment() {}
    public StationFragment(int cardCounter, int[] cardNumbers) {
        this.cardCounter = cardCounter;
        this.cardNumbers = cardNumbers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_draw, container, false);
        Player player = ((TtRA_Application) getActivity().getApplication()).player;
        Game game = ((TtRA_Application) getActivity().getApplication()).game;

        numberOfStation = game.getNumberOfStations() - player.getStations() + 1;
        if(numberOfStation <= game.getNumberOfStations()) {
            selectScreen(drawer, game, player);
        }
        else {
            blankScreen(drawer);
        }

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_buildStation);

        return drawer;
    }

    public void selectScreen(View drawer, Game game, Player player) {

        RecyclerView cardRecycler = drawer.findViewById(R.id.cards);
        stationCost = game.getStationCost().get(numberOfStation);

        int[] cardImages = new int[9];
        for (int i = 0; i < cardImages.length; i++) {
            cardImages[i] = Card.cards[i].getImageResourceId();
        }

        CardImageAdapter cardImageAdapter = new CardImageAdapter(cardImages, cardNumbers);
        cardRecycler.setAdapter(cardImageAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
        cardRecycler.setLayoutManager(layoutManager);

        cardImageAdapter.setListener(position -> {
            if (cardCounter < stationCost) {
                cardCounter++;
                cardNumbers[position]++;
                refreshPage();
            } else {
                String text = getString(R.string.station_cost) + " " + String.valueOf(stationCost);
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

        ImageView acceptIcon = drawer.findViewById(R.id.accept_icon);
        acceptIcon.setOnClickListener(this);
        ImageView resetIcon = drawer.findViewById(R.id.reset_icon);
        resetIcon.setOnClickListener(this);

        TextView maxNoDrawCards = drawer.findViewById(R.id.drawCards_value);
        maxNoDrawCards.setText(String.valueOf(game.getMaxNoOfCardsToDraw()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_icon:
                if (cardCounter < stationCost) {
                    String text = getString(R.string.station_cost) + " " + String.valueOf(stationCost);
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
                else {
                    Player player = ((TtRA_Application) getActivity().getApplication()).player;
                    for (int i = 0; i < cardNumbers.length; i++) {
                        String color = Card.cards[i].getName();
                        int cardNumber = player.getCards().get(color) - cardNumbers[i];
                        player.getCards().put(color, cardNumber);
                    }
                    player.setStations(player.getStations()-1);

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

    public void blankScreen(View drawer) {
        ImageView acceptIcon = drawer.findViewById(R.id.accept_icon);
        ImageView resetIcon = drawer.findViewById(R.id.reset_icon);
        acceptIcon.setVisibility(View.GONE);
        resetIcon.setVisibility(View.GONE);
    }
    private void clearDrawCards() {
        cardCounter = 0;
        cardNumbers = new int[9];
    }

    private void refreshPage() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new StationFragment(cardCounter, cardNumbers));
        ft.commit();
    }

    private void returnToTopPage() {
        ((MainActivity) getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }
}