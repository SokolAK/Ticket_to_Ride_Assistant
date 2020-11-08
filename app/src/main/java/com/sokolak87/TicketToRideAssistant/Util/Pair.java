package com.sokolak87.TicketToRideAssistant.Util;

public class Pair<F, S> {
    public F first;
    public S second;

    public static <F, S> Pair<F, S> create(F first, S second) {
        return new Pair<F, S>(first, second);
    }

    private Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
}
