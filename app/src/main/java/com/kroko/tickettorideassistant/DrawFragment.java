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
    private SharedViewModel viewModel;
    private int[] cardCounter = new int[1];
    private int[] cardNumbers = new int[9];
    private int maxCards;
    private int[] availableCards;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        maxCards = ((TtRA_Application) getActivity().getApplication()).game.getMaxNoOfCardsToDraw();
        availableCards = new int[] {1,1,1,1,1,1,1,1,1};
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.cards_container, CardsFragment.newInstance(cardCounter, cardNumbers, maxCards, availableCards));
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
        cardCounter[0] = 0;
        //viewModel.setCardCounter(0);
        cardNumbers = new int[9];
    }
    private void refreshPage() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.cards_container, CardsFragment.newInstance(cardCounter, cardNumbers, maxCards, availableCards));
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