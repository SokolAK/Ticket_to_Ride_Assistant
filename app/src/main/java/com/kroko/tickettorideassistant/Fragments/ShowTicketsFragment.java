package com.kroko.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Logic.Player;
import com.kroko.TicketToRideAssistant.Logic.Ticket;
import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.Logic.TtRA_Application;
import com.kroko.TicketToRideAssistant.UI.CustomSpinnerAdapter;
import com.kroko.TicketToRideAssistant.UI.CustomSpinnerItem;

import java.util.ArrayList;
import java.util.Collections;

public class ShowTicketsFragment extends Fragment {
    private boolean unlockDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_show_tickets, container, false);

        Player player = ((TtRA_Application) getActivity().getApplication()).player;
        Game game = ((TtRA_Application) getActivity().getApplication()).game;

        ArrayList<CustomSpinnerItem> ticketList = new ArrayList<>();
        for(Ticket ticket: player.getTickets()) {
            int imageResource = 0;
            if(ticket.isRealized()) {
                imageResource = R.drawable.ic_done_black_24dp;
            } else {
                imageResource = R.drawable.ic_close_black_24dp;
            }
            ticketList.add(new CustomSpinnerItem(ticket.toString(), imageResource, ticket.getId()));
        }
        //Collections.sort(ticketList, (x, y) -> x.compareTo(y));

        ListView listTickets = drawer.findViewById(R.id.list_tickets);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), ticketList);
        listTickets.setAdapter(adapter);

        listTickets.setOnItemLongClickListener((arg0, arg1, position, id) -> {
            if(unlockDelete) {
                //Ticket ticket = player.getTickets().get(position);
                player.removeTicket(position);
                ticketList.remove(position);
                adapter.notifyDataSetChanged();
            }
            return true;
        });

        Switch switchControl = drawer.findViewById(R.id.switch_delete);
        if (switchControl != null) {
            switchControl.setChecked(false);
            switchControl.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    unlockDelete = true;
                    switchControl.setText(R.string.unlocked);
                    switchControl.setTextColor(getResources().getColor(R.color.cardsUnlocked));
                    TextView deleteComment = drawer.findViewById(R.id.delete_comment);
                    deleteComment.setVisibility(View.VISIBLE);
                } else {
                    unlockDelete = false;
                    switchControl.setText(R.string.locked);
                    switchControl.setTextColor(getResources().getColor(R.color.cardsLocked));
                    TextView deleteComment = drawer.findViewById(R.id.delete_comment);
                    deleteComment.setVisibility(View.INVISIBLE);
                }
            });
        }

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_showTickets);

        return drawer;
    }

}
