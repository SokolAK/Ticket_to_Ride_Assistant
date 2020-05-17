package com.kroko.TicketToRideAssistant.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Logic.Player;
import com.kroko.TicketToRideAssistant.Logic.Ticket;
import com.kroko.TicketToRideAssistant.Logic.TtRA_Application;
import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.UI.CustomSpinnerItem;
import com.kroko.TicketToRideAssistant.UI.SpinnerListenerInterface;
import com.kroko.TicketToRideAssistant.UI.SpinnerRouteFragment;
import com.kroko.TicketToRideAssistant.UI.SpinnerTicketFragment;

public class DrawTicketFragment extends Fragment implements View.OnClickListener, SpinnerListenerInterface {
    private Game game;
    private Player player;
    private Ticket ticket;
    private View drawer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        game = ((TtRA_Application) getActivity().getApplication()).game;
        player = ((TtRA_Application) getActivity().getApplication()).player;

        drawer = inflater.inflate(R.layout.fragment_draw_ticket, container, false);

        ImageView acceptIcon = drawer.findViewById(R.id.accept_icon);
        acceptIcon.setOnClickListener(this);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        SpinnerTicketFragment spinnerTicketFragment = new SpinnerTicketFragment();
        ft.replace(R.id.spinners_container, spinnerTicketFragment);
        ft.commit();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_drawTicket);

        return drawer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_icon:
                ticket.setInHand(true);
                player.addTicket(new Ticket(ticket));
                returnToTopPage();
                break;
        }
    }

    private void returnToTopPage() {
        ((MainActivity) getActivity()).onNavigationItemSelected(0);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }


    @Override
    public void onSpinnerItemSelected(CustomSpinnerItem spinnerItem) {
        int ticketId = spinnerItem.getItemId();
        ticket = game.getTicket(ticketId);
        TextView pointsValue = drawer.findViewById(R.id.points_value);
        pointsValue.setText(String.valueOf(ticket.getPoints()));

        ticket.checkIfRealized(player);
        int imageResource = 0;
        if(ticket.isRealized()) {
            imageResource = R.drawable.ic_done_black_24dp;
        } else {
            imageResource = R.drawable.ic_close_black_24dp;
        }
        ImageView realizedValue = drawer.findViewById(R.id.realized_value);
        realizedValue.setImageResource(imageResource);
    }
}