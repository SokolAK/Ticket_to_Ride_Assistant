package com.kroko.TicketToRideAssistant.Logic;

import lombok.Data;

@Data
public class Ticket {
    private int id;
    private String City1;
    private String City2;
    private int points;
    private boolean realized;

    public Ticket(int id, String city1, String city2, int points) {
        this.id = id;
        City1 = city1;
        City2 = city2;
        this.points = points;
        this.realized = false;
    }
}
