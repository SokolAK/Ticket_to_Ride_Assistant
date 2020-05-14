package com.kroko.TicketToRideAssistant;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class CustomSpinnerAdapter extends ArrayAdapter {
    public CustomSpinnerAdapter(Context context, ArrayList<CustomSpinnerItem> itemsList) {
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
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView textView = convertView.findViewById(R.id.text_view);

        CustomSpinnerItem customSpinnerItem = (CustomSpinnerItem)getItem(position);
        if(customSpinnerItem != null) {
            imageView.setImageResource(customSpinnerItem.getImage());
            textView.setText(customSpinnerItem.getCity());
        }
        return convertView;
    }
}