package com.kroko.TicketToRideAssistant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Integer> cardCounter = new MutableLiveData<>();

    public void setCardCounter(int cardCounter) {
        this.cardCounter.setValue(cardCounter);
    }

    public LiveData<Integer> getCardCounter () {
        return cardCounter;
    }
}
