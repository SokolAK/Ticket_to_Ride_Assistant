package com.kroko.TicketToRideAssistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    private String text;

    public BlankFragment() {
        // Required empty public constructor
    }

    public BlankFragment(String text) {
        this.text = text;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_blank, container, false);
        TextView textView = drawer.findViewById(R.id.blank_text);
        textView.setText(text);
        return drawer;
    }
}
