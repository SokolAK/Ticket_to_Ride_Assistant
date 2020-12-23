package pl.sokolak.TicketToRideAssistant.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.TtRA_Application;
import pl.sokolak.TicketToRideAssistant.UI.CarCardsFragment2;
import pl.sokolak.TicketToRideAssistant.UI.Card;
import pl.sokolak.TicketToRideAssistant.UI.CarCardsFragment;
import pl.sokolak.TicketToRideAssistant.UI.TextTextItem;
import pl.sokolak.TicketToRideAssistant.UI.TextTextItemAdapter;
import pl.sokolak.TicketToRideAssistant.Util.Pair;

import static pl.sokolak.TicketToRideAssistant.Util.DimensionUtils.getDimension;

public class TopFragment extends Fragment {
    private Player player;
    private Game game;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);
        player = ((TtRA_Application) requireActivity().getApplication()).player;
        game = ((TtRA_Application) requireActivity().getApplication()).game;
        player.updatePoints();

        Activity activity = requireActivity();
        ListView pointsList = view.findViewById(R.id.status_container);
        pointsList.setEnabled(false);
        List<TextTextItem> rows = getStatusList(activity);
        TextTextItemAdapter adapter = new TextTextItemAdapter(getContext(), rows);
        pointsList.setAdapter(adapter);


        for (Card card : game.getCards()) {
            card.setClickable(true);
            card.setVisible(true);
        }

        SwitchCompat switchControl = view.findViewById(R.id.switch_control);
        if (switchControl != null) {
            switchControl.setChecked(false);
            switchControl.setOnCheckedChangeListener((buttonView, isChecked) -> {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                if (isChecked) {
                    CarCardsFragment carCardsFragment = CarCardsFragment.builder().cardsNumbers(player.getCardsNumbers()).
                            active(true).activeLong(true).
                            build();

                    FrameLayout cardsContainer = view.findViewById(R.id.cards_container);
                    cardsContainer.setBackgroundResource(R.color.cardsUnlocked);
                    switchControl.setText(R.string.cards_unlocked);
                    switchControl.setTextColor(getResources().getColor(R.color.cardsUnlocked));
                    ft.replace(R.id.cards_container, carCardsFragment);
                } else {
                    CarCardsFragment carCardsFragment = CarCardsFragment.builder().cardsNumbers(player.getCardsNumbers()).build();

                    FrameLayout cardsContainer = view.findViewById(R.id.cards_container);
                    cardsContainer.setBackgroundResource(R.color.colorBackDark);
                    switchControl.setText(R.string.cards_locked);
                    switchControl.setTextColor(getResources().getColor(R.color.cardsLocked));
                    ft.replace(R.id.cards_container, carCardsFragment);
                }
                ft.commit();
            });
        }

        switchControl.setChecked(true);
        switchControl.setChecked(false);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_top);

        return view;
    }

    public List<TextTextItem> getStatusList(Context context) {
        List<Pair<String, String>> pointsList = game.getStatusCalculator().getStatusList(context);
        List<TextTextItem> textTextItems = new ArrayList<>();
        for(Pair<String, String> pointsRow : pointsList) {
            String label = pointsRow.first;
            String value = pointsRow.second;
            TextTextItem textTextItem = new TextTextItem(label, value);
            textTextItem.setTextSize(getDimension(context, R.dimen.text_size_normal));
            textTextItems.add(textTextItem);
        }
        return textTextItems;
    }
}
