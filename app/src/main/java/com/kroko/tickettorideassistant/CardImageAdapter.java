package com.kroko.TicketToRideAssistant;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

class CardImageAdapter extends RecyclerView.Adapter<CardImageAdapter.ViewHolder> {
    private int[] imageIds;
    private int[] numbers;
    private Listener listener;
    private int[] listOfTypeViews;

    interface Listener {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public CardImageAdapter(int[] imageIds, int[] numbers){
        this(imageIds, numbers, new int[] {1,1,1,1,1,1,1,1,1});
    }

    public CardImageAdapter(int[] imageIds, int[] numbers, int[] listOfTypeViews){
        this.imageIds = imageIds;
        this.numbers = numbers;
        this.listOfTypeViews = listOfTypeViews;
    }

    @Override
    public int getItemViewType(int position) {
        return listOfTypeViews[position];
    }

    @Override
    public int getItemCount(){
        return imageIds.length;
    }

    @Override
    public CardImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        CardView cv = null;
        if(viewType == 1)
            cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        if(viewType == 0)
            cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_card_view, parent, false);
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) cv.getLayoutParams();
        float density = parent.getContext().getResources().getDisplayMetrics().density;
        float px = 4 * density * 6 + 1;
        params.width = (int) ((parent.getMeasuredWidth() - px) / 3);
        cv.setLayoutParams(params);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        CardView cardView = holder.cardView;
        switch (holder.getItemViewType()) {
            case 1:
                ImageView imageView = cardView.findViewById(R.id.card_image);
                Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), imageIds[position]);
                imageView.setImageDrawable(drawable);
                TextView textView = cardView.findViewById(R.id.card_number);
                textView.setText(String.valueOf(numbers[position]));
                break;
            case 0:
                break;
        }

        cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(position);
            }
        });
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }
}

