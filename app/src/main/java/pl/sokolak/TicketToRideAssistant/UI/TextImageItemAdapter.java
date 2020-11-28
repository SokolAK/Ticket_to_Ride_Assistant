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

public class TextImageItemAdapter extends ArrayAdapter<TextImageItem> {
    public TextImageItemAdapter(Context context, List<TextImageItem> itemsList) {
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.text_image_list_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView textView = convertView.findViewById(R.id.text_view);

        TextImageItem textImageItem = getItem(position);
        if (textImageItem != null) {
            imageView.setImageResource(textImageItem.getImageResource());
            textView.setText(textImageItem.getText());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textImageItem.getTextSize());
        }
        return convertView;
    }
}