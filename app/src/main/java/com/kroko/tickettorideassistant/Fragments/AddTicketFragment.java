package com.kroko.TicketToRideAssistant.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kroko.TicketToRideAssistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTicketFragment extends Fragment {

    public AddTicketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ticket, container, false);
    }
}