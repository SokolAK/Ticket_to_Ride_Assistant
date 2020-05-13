package com.kroko.TicketToRideAssistant;

import lombok.Data;

@Data
public class Route {
    private int _id;
    private String city1;
    private String city2;
    private int length;
    private int locos;
    private boolean tunnel;
    private String colors;

    public Route(int _id, String city1, String city2, int length, int locos, boolean tunnel, String colors) {
        this._id = _id;
        this.city1 = city1;
        this.city2 = city2;
        this.length = length;
        this.locos = locos;
        this.tunnel = tunnel;
        this.colors = colors;
    }

    @Override
    public String toString() {
        return city1 + " - " + city2 + " (" + colors + ")";
    }
}
