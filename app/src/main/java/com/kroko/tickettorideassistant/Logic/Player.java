package com.kroko.TicketToRideAssistant.Logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class Player implements Serializable {
    private Game game;

    private int points;
    private int numberOfStations;
    private int numberOfCars;
    private int longestPathLength;
    private int[] cardsNumbers;
    private ArrayList<Route> builtRoutes = new ArrayList<>();
    private ArrayList<Route> builtStations = new ArrayList<>();
    private ArrayList<Ticket> tickets = new ArrayList<>();

    public void setGame(Game game) {
        this.game = game;
    }

    public void prepare() {
        numberOfCars = game.getNumberOfCars();
        numberOfStations = game.getNumberOfStations();
        points = numberOfStations * game.getStationPoints();
        cardsNumbers = new int[game.getCards().size()];
        for (int i = 0; i < game.getCards().size(); ++i) {
            cardsNumbers[i] = 0;
        }
    }

    public void addCards(int[] cardsNumbers) {
        for (int i = 0; i < this.cardsNumbers.length; ++i) {
            this.cardsNumbers[i] += cardsNumbers[i];
        }
    }
    public void spendCards(int[] cardsNumbers) {
        for (int i = 0; i < this.cardsNumbers.length; ++i) {
            this.cardsNumbers[i] -= cardsNumbers[i];
        }
    }

    public void addCars(int cars) {
        this.numberOfCars += cars;
    }
    public void spendCars(int cars) {
        this.numberOfCars -= cars;
    }

    public void addRoute(Route route) {
        builtRoutes.add(0, route);
        updatePoints();
        //findLongestPath();
    }
    public void removeRoute(int i) {
        builtRoutes.remove(i);
        updatePoints();
        //findLongestPath();
    }

    public void addRouteStation(Route route) {
        builtStations.add(0, route);
        numberOfStations--;
        updatePoints();
        //findLongestPath();
    }
    public void removeRouteStation(int i) {
        builtStations.remove(i);
        numberOfStations++;
        updatePoints();
        //findLongestPath();
    }

    public void addTicket(Ticket ticket) {
        tickets.add(0, ticket);
        ticket.setInHand(true);
        updatePoints();
    }
    public void removeTicket(int i) {
        tickets.get(i).setInHand(false);
        tickets.remove(i);
        updatePoints();
    }
    public void checkIfTicketsRealized() {
        for(Ticket ticket: tickets) {
            boolean realized = ticket.checkIfRealized(this);
            ticket.setRealized(realized);
        }
        updatePoints();
    }

    @SuppressWarnings("ConstantConditions")
    public void updatePoints() {
        points = 0;
        for(Ticket ticket: tickets) {
            if(ticket.isRealized()) {
                points += ticket.getPoints();
            } else {
                points -= ticket.getPoints();
            }
        }
        for(Route route: builtRoutes) {
            points += game.getScoring().get(route.getLength());
        }
        points += numberOfStations*game.getStationPoints();
    }

    public void findLongestPath() {
        int length = 0;
        HashSet<Route> segments = new HashSet<>(builtRoutes);
        segments.addAll(builtStations);
        HashSet<String> citiesSet = new HashSet<>();
        for(Route segment: segments) {
            citiesSet.add(segment.getCity1());
            citiesSet.add(segment.getCity2());
        }

        List<String> cities = new ArrayList<>(citiesSet);
        List<String> usedCities = new ArrayList<>();
        while(cities.size() > 0) {
            String city1 = cities.get(0);




            usedCities.add(city1);



            cities.removeAll(usedCities);
        }

        longestPathLength = length;
    }
}

