package com.kroko.tickettorideassistant;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

public class CardsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //RecyclerView cardRecycler = (RecyclerView)inflater.inflate(R.layout.fragment_cards, container, false);

        View drawer = inflater.inflate(R.layout.fragment_status, container, false);
        RecyclerView cardRecycler = drawer.findViewById(R.id.cards);

        int[] cardImages = new int[9];
        for (int i = 0; i < cardImages.length; i++) {
            cardImages[i] = Card.cards[i].getImageResourceId();
        }
        int[] cardNumbers = new int[9];
        for (int i = 0; i < cardNumbers.length; i++) {
            String color = Card.cards[i].getName();
            cardNumbers[i] = ((TtRA_Application) getActivity().getApplication()).player.getCards().get(color);
            //cardNumbers[i] = ((TtRA_Application) getActivity().getApplication()).player.getNumberOfCards(i);
        }

        CardImageAdapter adapter = new CardImageAdapter(cardImages, cardNumbers);
        cardRecycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        cardRecycler.setLayoutManager(layoutManager);

        return drawer;
    }
}