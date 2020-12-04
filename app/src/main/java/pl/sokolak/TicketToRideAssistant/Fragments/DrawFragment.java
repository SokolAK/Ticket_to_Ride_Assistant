package pl.sokolak.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardTile;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardsController;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardsFragment;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.CarCardsObserver;
import pl.sokolak.TicketToRideAssistant.CarCardsPanel.DrawCarCardsController;
import pl.sokolak.TicketToRideAssistant.Domain.CarCardColor;
import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.TtRA_Application;

import static pl.sokolak.TicketToRideAssistant.Util.carCardsConverter.carCardsTilesToCarCards;

public class DrawFragment extends Fragment implements View.OnClickListener, CarCardsObserver {
    private Game game;
    private Player player;
    private int maxCards;
    private String title;
    private List<CarCardTile> carCardTiles = new ArrayList<>();
    private CarCardsController carCardsController;

    public DrawFragment() {
    }

    public DrawFragment(int maxCards, String title) {
        this.maxCards = maxCards;
        this.title = title;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        game = ((TtRA_Application) requireActivity().getApplication()).game;
        player = ((TtRA_Application) requireActivity().getApplication()).player;

        carCardsController = new DrawCarCardsController(maxCards);
        carCardsController.setGame(game);

        View view = inflater.inflate(R.layout.fragment_draw_cards, container, false);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(title);

        ImageView acceptIcon = view.findViewById(R.id.accept_button);
        acceptIcon.setOnClickListener(this);
        ImageView resetIcon = view.findViewById(R.id.reset_button);
        resetIcon.setOnClickListener(this);

        TextView drawCardsLabel = view.findViewById(R.id.drawCards_label);
        TextView drawCardsValue = view.findViewById(R.id.drawCards_value);
        if (maxCards >= 0) {
            drawCardsValue.setText(String.valueOf(maxCards));
            drawCardsLabel.setText(R.string.draw_cards_label);
        } else {
            drawCardsValue.setText("");
            drawCardsLabel.setText("");
        }

        clearDrawCards();
        return view;
    }

    private void clearDrawCards() {
        carCardTiles = new ArrayList<>();
        for (CarCardColor card : game.getCarCardColors()) {
            carCardTiles.add(new CarCardTile(card, 0, true, true, true));
        }
        updateCarCardsFragment(carCardTiles);
    }

    private void updateCarCardsFragment(List<CarCardTile> carCardTiles) {
        CarCardsFragment carCardsFragment = new CarCardsFragment(carCardTiles, carCardsController);
        carCardsFragment.getCarCardsObservers().add(this);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.cards_container, carCardsFragment);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.accept_button:
                player.addCards(carCardsTilesToCarCards(carCardTiles));
                returnToTopPage();
                break;

            case R.id.reset_button:
                clearDrawCards();
                break;
        }
    }

    private void returnToTopPage() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void updateCarCards(List<CarCardTile> carCardTiles) {
        updateCarCardsFragment(carCardTiles);
    }
}