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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

public class DrawFragment extends Fragment implements View.OnClickListener {
    private Game game;
    private Player player;

    private int[] cardCounter;
    private int[] cardNumbers;
    private int maxCards;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        game = ((TtRA_Application) getActivity().getApplication()).game;
        player = ((TtRA_Application) getActivity().getApplication()).player;
        cardCounter = new int[1];
        cardNumbers = new int[game.getCards().size()];

        Game game = ((TtRA_Application) getActivity().getApplication()).game;
        Player player = ((TtRA_Application) getActivity().getApplication()).player;
        maxCards = game.getMaxNoOfCardsToDraw();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.cards_container, new CardsFragment(cardCounter, cardNumbers, maxCards , true));
        ft.addToBackStack(null);
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        View drawer = inflater.inflate(R.layout.fragment_draw, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_drawCards);

        ImageView acceptIcon = drawer.findViewById(R.id.accept_icon);
        acceptIcon.setOnClickListener(this);
        ImageView resetIcon = drawer.findViewById(R.id.reset_icon);
        resetIcon.setOnClickListener(this);

        return drawer;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_icon:
                if(cardCounter[0] == 0) {
                    String text = getString(R.string.too_little);
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
                else {
                    Player player = ((TtRA_Application) getActivity().getApplication()).player;
                    for (int i = 0; i < cardNumbers.length; i++) {
                        int cardNumber = player.getCards()[i] + cardNumbers[i];
                        player.getCards()[i] = cardNumber;
                    }
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
        cardNumbers = new int[game.getCards().size()];
    }
    private void refreshCards() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.cards_container, new CardsFragment(cardCounter, cardNumbers, maxCards, true));
        ft.addToBackStack(null);
        ft.commit();
    }

    private void returnToTopPage() {
        ((MainActivity)getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public DrawFragment() {

    }
}