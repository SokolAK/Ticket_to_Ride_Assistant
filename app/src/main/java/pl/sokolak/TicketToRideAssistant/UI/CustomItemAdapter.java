package pl.sokolak.TicketToRideAssistant.UI;

import java.util.List;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import pl.sokolak.TicketToRideAssistant.R;

public class CustomItemAdapter extends ArrayAdapter<CustomItem> {
    public CustomItemAdapter(Context context, List<CustomItem> itemsList) {
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView textView = convertView.findViewById(R.id.text_view);

        CustomItem customItem = getItem(position);
        if (customItem != null) {
            imageView.setImageResource(customItem.getImageResource());
            textView.setText(customItem.getText());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, customItem.getTextSize());
        }
        return convertView;
    }
}