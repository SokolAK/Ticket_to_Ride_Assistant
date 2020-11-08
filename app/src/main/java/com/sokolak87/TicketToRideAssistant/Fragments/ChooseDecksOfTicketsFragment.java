package com.sokolak87.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.sokolak87.TicketToRideAssistant.Logic.Game;
import com.sokolak87.TicketToRideAssistant.Logic.Player;
import com.sokolak87.TicketToRideAssistant.Logic.TtRA_Application;
import com.sokolak87.TicketToRideAssistant.R;
import com.sokolak87.TicketToRideAssistant.Util.Triplet;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseDecksOfTicketsFragment extends Fragment implements View.OnClickListener {

    public ChooseDecksOfTicketsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_choose_decks_of_tickets, container, false);

        ListView checkableList = drawer.findViewById(R.id.decks_of_tickets_list);
        checkableList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        Game game = ((TtRA_Application) requireActivity().getApplication()).game;
        List<String> items = new ArrayList<>();
        for (Triplet deck : game.getTicketsDecks()) {
            items.add((String) deck.second);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), R.layout.checkable_item_layout, R.id.checkable_text_view, items);
        checkableList.setAdapter(adapter);
        for (int i = 0; i < game.getTicketsDecks().size(); ++i) {
            Triplet deck = game.getTicketsDecks().get(i);
            checkableList.setItemChecked(i, (Boolean) deck.third);
        }

        checkableList.setOnItemClickListener((parent, view, position, id) -> {
            game.getTicketsDecks().get(position).third = !game.getTicketsDecks().get(position).third;
        });

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_tickets_decks);

        Button buttonAccept = drawer.findViewById(R.id.accept_button);
        buttonAccept.setOnClickListener(this);

        return drawer;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accept_button:
                Game game = ((TtRA_Application) requireActivity().getApplication()).game;
                Player player = ((TtRA_Application) requireActivity().getApplication()).player;
                game.updateTickets();
                player.updateTickets();
                requireActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
}
