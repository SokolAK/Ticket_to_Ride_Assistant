package pl.sokolak.TicketToRideAssistant.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import pl.sokolak.TicketToRideAssistant.Logic.Game;
import pl.sokolak.TicketToRideAssistant.Logic.Player;
import pl.sokolak.TicketToRideAssistant.Logic.Ticket;
import pl.sokolak.TicketToRideAssistant.Logic.TtRA_Application;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.UI.CustomItem;
import pl.sokolak.TicketToRideAssistant.UI.SpinnerListenerInterface;
import pl.sokolak.TicketToRideAssistant.UI.SpinnerTicketFragment;

public class DrawTicketFragment extends Fragment implements View.OnClickListener, SpinnerListenerInterface {
    private Game game;
    private Player player;
    private Ticket ticket;
    private View drawer;
    private int ticketId;
    private String defaultCity1;
    private String defaultCity2;

    public DrawTicketFragment() {
    }

    public DrawTicketFragment(String city1, String city2) {
        defaultCity1 = city1;
        defaultCity2 = city2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        game = ((TtRA_Application) requireActivity().getApplication()).game;
        player = ((TtRA_Application) requireActivity().getApplication()).player;
        drawer = inflater.inflate(R.layout.fragment_draw_ticket, container, false);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        SpinnerTicketFragment spinnerTicketFragment = new SpinnerTicketFragment(defaultCity1, defaultCity2);
        ft.replace(R.id.spinners_container, spinnerTicketFragment);
        ft.commit();

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_drawTicket);

        Button buttonAddTicket = drawer.findViewById(R.id.add_ticket);
        buttonAddTicket.setOnClickListener(this);
        ImageView acceptIcon = drawer.findViewById(R.id.accept_button);
        acceptIcon.setOnClickListener(this);

        return drawer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_button:
                if (ticket != null) {
                    player.addTicket(ticket);
                    returnToTopPage();
                }
                break;
            case R.id.add_ticket:
                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new AddTicketFragment());
                ft.commit();
                break;
        }
    }

    private void returnToTopPage() {
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new ShowTicketsFragment());
        ft.commit();
    }


    @Override
    public void onSpinnerItemSelected(CustomItem... items) {
        if (items.length == 1) {
            player.checkIfTicketsRealized();
            ticketId = items[0].getItemId();
            ticket = game.getTicket(ticketId);
            TextView pointsValue = drawer.findViewById(R.id.points_value);
            pointsValue.setText(String.valueOf(ticket.getPoints()));
            ticket.checkIfRealized(player);
            int imageResource = 0;
            if (ticket.isRealized()) {
                imageResource = R.drawable.ic_done_black_24dp;
            } else {
                imageResource = R.drawable.ic_close_black_24dp;
            }
            ImageView realizedValue = drawer.findViewById(R.id.realized_value);
            realizedValue.setImageResource(imageResource);
        }
    }
}