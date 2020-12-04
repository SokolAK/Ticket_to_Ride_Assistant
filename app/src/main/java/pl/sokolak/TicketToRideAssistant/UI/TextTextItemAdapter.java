package pl.sokolak.TicketToRideAssistant.UI;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import pl.sokolak.TicketToRideAssistant.R;

public class TextTextItemAdapter extends ArrayAdapter<TextTextItem> {
    public TextTextItemAdapter(Context context, List<TextTextItem> itemsList) {
        super(context, 0, itemsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.text_text_list_item, parent, false);
        }

        TextView textView1 = convertView.findViewById(R.id.text_view_1);
        TextView textView2 = convertView.findViewById(R.id.text_view_2);

        TextTextItem textTextItem = getItem(position);
        if (textTextItem != null) {
            textView1.setText(textTextItem.getText1());
            textView2.setText(textTextItem.getText2());
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, textTextItem.getTextSize());
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, textTextItem.getTextSize());
        }
        return convertView;
    }
}