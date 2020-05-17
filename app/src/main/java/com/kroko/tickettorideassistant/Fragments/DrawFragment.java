package com.kroko.TicketToRideAssistant.Fragments;

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
import com.google.android.material.navigation.NavigationView;
import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Logic.Player;
import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.Logic.TtRA_Application;
import com.kroko.TicketToRideAssistant.UI.CardsCarFragment;

public class DrawFragment extends Fragment implements View.OnClickListener {
    private Game game;
    private Player player;
    private int[] cardCounter;
    private int[] cardsNumbers;
    private int maxCards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        game = ((TtRA_Application) getActivity().getApplication()).game;
        player = ((TtRA_Application) getActivity().getApplication()).player;
        cardCounter = new int[1];
        cardsNumbers = new int[game.getCards().size()];
        for(int i = 0; i < game.getCards().size(); ++i) {
            game.getCards().get(i).setClickable(1);
            game.getCards().get(i).setVisible(1);
            cardsNumbers[i] = 0;
        }

        Game game = ((TtRA_Application) getActivity().getApplication()).game;
        Player player = ((TtRA_Application) getActivity().getApplication()).player;
        maxCards = game.getMaxNoOfCardsToDraw();

        View drawer = inflater.inflate(R.layout.fragment_draw_cards, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_drawCards);

        ImageView acceptIcon = drawer.findViewById(R.id.accept_icon);
        acceptIcon.setOnClickListener(this);
        ImageView resetIcon = drawer.findViewById(R.id.reset_icon);
        resetIcon.setOnClickListener(this);

        TextView drawCardsValue = drawer.findViewById(R.id.drawCards_value);
        drawCardsValue.setText(String.valueOf(maxCards));

        refreshCards();
        return drawer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.accept_icon:
                if(cardCounter[0] == 0) {
                    String text = getString(R.string.too_little_cards);
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
                else {
                    Player player = ((TtRA_Application) getActivity().getApplication()).player;
                    player.addCards(cardsNumbers);
                    clearDrawCards();
                    refreshCards();
                    returnToTopPage();
                }
                break;

            case R.id.reset_icon:
                clearDrawCards();
                refreshCards();
                break;
        }
    }

    private void clearDrawCards() {
        cardCounter[0] = 0;
        Game game = ((TtRA_Application) getActivity().getApplication()).game;
        for(int i = 0; i < game.getCards().size(); ++i) {
            game.getCards().get(i).setClickable(1);
            game.getCards().get(i).setVisible(1);
            cardsNumbers[i] = 0;
        }
    }
    private void refreshCards() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CardsCarFragment cardsCarFragment = new CardsCarFragment(cardsNumbers,cardCounter,maxCards);
        cardsCarFragment.setActive(true);
        cardsCarFragment.setActiveLong(true);
        cardsCarFragment.setOneColor(false);
        ft.replace(R.id.cards_container, cardsCarFragment);
        //ft.addToBackStack(null);
        ft.commit();
    }

    private void returnToTopPage() {
        ((MainActivity)getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }
}