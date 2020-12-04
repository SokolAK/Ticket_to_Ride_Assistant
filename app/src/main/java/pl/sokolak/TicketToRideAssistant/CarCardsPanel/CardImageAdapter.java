package pl.sokolak.TicketToRideAssistant.CarCardsPanel;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.sokolak.TicketToRideAssistant.R;

public class CardImageAdapter extends RecyclerView.Adapter<CardImageAdapter.ViewHolder> {
    private Listener listener;
    private ListenerLong listenerLong;
    private List<CarCardTile> carCardTiles;

    public CardImageAdapter(List<CarCardTile> carCardTiles) {
        this.carCardTiles = carCardTiles;
    }

    interface Listener {
        void onClick(int position);
    }

    interface ListenerLong {
        void onLongClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return carCardTiles.get(position).isVisible() ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return carCardTiles.size();
    }

    @Override
    public CardImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = null;
        if (viewType == 1)
            cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.car_card_view, parent, false);
        if (viewType == 0)
            cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_card_view, parent, false);
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) cv.getLayoutParams();
        int margin = parent.getContext().getResources().getDimensionPixelSize(R.dimen.card_margin);
        int space = margin * (2 +2*2);
        params.width = (parent.getMeasuredWidth() - space) / 3;
        cv.setLayoutParams(params);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        switch (holder.getItemViewType()) {
            case 1:
                //cardView.setRadius(24); //to avoid ghosts
                ImageView imageView = cardView.findViewById(R.id.card_image);
                Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), carCardTiles.get(position).getCarCardColor().getImageResourceId());
                imageView.setImageDrawable(drawable);
                TextView textView = cardView.findViewById(R.id.card_number);
                textView.setText(String.valueOf(carCardTiles.get(position).getAmount()));
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

