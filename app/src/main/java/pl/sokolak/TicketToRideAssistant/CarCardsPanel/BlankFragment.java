package pl.sokolak.TicketToRideAssistant.CarCardsPanel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.sokolak.TicketToRideAssistant.R;

public class BlankFragment extends Fragment {
    public BlankFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_blank, container, false);
        TextView textView = drawer.findViewById(R.id.blank_text);
        return drawer;
    }
}
