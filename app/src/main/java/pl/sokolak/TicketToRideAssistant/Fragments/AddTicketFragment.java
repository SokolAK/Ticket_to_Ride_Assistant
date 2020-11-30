package pl.sokolak.TicketToRideAssistant.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Ticket;
import pl.sokolak.TicketToRideAssistant.TtRA_Application;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.UI.TextImageItem;
import pl.sokolak.TicketToRideAssistant.UI.SpinnerCitiesFragment;
import pl.sokolak.TicketToRideAssistant.UI.SpinnerListenerInterface;
import pl.sokolak.TicketToRideAssistant.Util.SoftKeyboard;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTicketFragment extends Fragment implements View.OnClickListener, SpinnerListenerInterface {
    private Spinner listCity1;
    private Spinner listCity2;
    private String city1;
    private String city2;
    private EditText pointsField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_add_ticket, container, false);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        SpinnerCitiesFragment spinnerCitiesFragment = new SpinnerCitiesFragment();
        ft.replace(R.id.spinners_container, spinnerCitiesFragment);
        ft.commit();

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_new_ticket);

        Button buttonAdd = drawer.findViewById(R.id.add_ticket);
        buttonAdd.setOnClickListener(this);

        listCity1 = drawer.findViewById(R.id.spinner1);
        listCity2 = drawer.findViewById(R.id.spinner2);
        pointsField = drawer.findViewById(R.id.ticket_value);

        pointsField.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                SoftKeyboard.hideKeyboardFrom(getContext(), drawer);
            }
        });



        return drawer;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_ticket:
                Context context = getContext();
                String pointsText = pointsField.getText().toString();
                if (TextUtils.isEmpty(pointsText)) {
                    String text = getString(R.string.enter_points);
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                } else {
                    int points = Integer.parseInt(pointsText);
                    Game game = ((TtRA_Application) requireActivity().getApplication()).game;
                    game.getTickets().add(new Ticket(city1, city2, points, "custom"));
                    FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new DrawTicketFragment(city1, city2));
                    ft.commit();
                    InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(context).getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;
        }
    }

    @Override
    public void onSpinnerItemSelected(TextImageItem... items) {
        if (items.length == 2) {
            city1 = items[0].getText();
            city2 = items[1].getText();
        }
    }
}
