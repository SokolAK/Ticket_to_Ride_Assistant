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

public class DrawFragment extends Fragment implements View.OnClickListener {

    private int cardCounter;
    private int[] cardNumbers = new int[9];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View drawer = inflater.inflate(R.layout.fragment_draw, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_drawCards);
        return drawer;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "DUPA", Toast.LENGTH_SHORT).show();
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
        //FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        //ft.replace(R.id.content_frame, new DrawFragment(cardCounter,cardNumbers));
        //ft.commit();
    }

    private void returnToTopPage() {
        ((MainActivity)getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public DrawFragment() {
    }
    public DrawFragment(int cardCounter, int[] cardNumbers) {
        this.cardCounter = cardCounter;
        this.cardNumbers = cardNumbers;
    }
}