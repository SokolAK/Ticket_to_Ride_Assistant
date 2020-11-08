package com.sokolak87.TicketToRideAssistant.UI;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sokolak87.TicketToRideAssistant.R;

import java.util.List;

public class CardImageAdapter extends RecyclerView.Adapter<CardImageAdapter.ViewHolder> {
    private Listener listener;
    private ListenerLong listenerLong;
    private List<Card> cards;
    private int[] cardsNumbers;

    interface Listener {
        void onClick(int position);
    }

    interface ListenerLong {
        void onLongClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public CardImageAdapter(List<Card> cards, int[] cardsNumbers) {
        this.cards = cards;
        this.cardsNumbers = cardsNumbers;
    }

    @Override
    public int getItemViewType(int position) {
        return cards.get(position).getVisible();
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public CardImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = null;
        if (viewType == 1)
            cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_car_view, parent, false);
        if (viewType == 0)
            cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_card_view, parent, false);
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) cv.getLayoutParams();
        float density = parent.getContext().getResources().getDisplayMetrics().density;
        float px = 4 * density * 6 + 1;
        params.width = (int) ((parent.getMeasuredWidth() - px) / 3);
        cv.setLayoutParams(params);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        switch (holder.getItemViewType()) {
            case 1:
                cardView.setRadius(24); //to avoid ghosts
                ImageView imageView = cardView.findViewById(R.id.card_image);
                Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), cards.get(position).getImageResourceId());
                imageView.setImageDrawable(drawable);
                TextView textView = cardView.findViewById(R.id.card_number);
                textView.setText(String.valueOf(cardsNumbers[position]));
                break;
            case 0:
                cardView.setRadius(0); //to avoid ghosts
                break;
        }

        cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(position);
            }
        });

        cardView.setOnLongClickListener(v -> {
            if (listenerLong != null) {
                listenerLong.onLongClick(position);
            }
            return true;
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setListenerLong(ListenerLong listener) {
        this.listenerLong = listener;
    }
}

