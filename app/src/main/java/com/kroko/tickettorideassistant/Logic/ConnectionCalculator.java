package com.kroko.TicketToRideAssistant.Logic;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class ConnectionCalculator {
    private Set<Route> builtSegments;
    private int maxLength = 0;

    public ConnectionCalculator(Player player) {
        builtSegments = new HashSet<>(player.getBuiltRoutes());
        builtSegments.addAll(player.getBuiltStations());
    }

    public boolean checkIfCityIsOnPlayersList(String city) {
        for (Route routes : builtSegments) {
            if (city.equals(routes.getCity1()) || city.equals(routes.getCity2())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfConnected(String cityStart, String cityPrevious, String cityDestination) {
        ArrayList<String> cities2 = getCitiesConnectedToCityWithoutPrevious(builtSegments, cityStart, cityPrevious);

        for (String city2 : cities2) {
            if (city2.equals(cityDestination)) {
                return true;
            }
            if (checkIfConnected(city2, cityStart, cityDestination)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getCitiesConnectedToCityWithoutPrevious(Set<Route> builtSegments, String cityStart, String cityPrevious) {
        ArrayList<String> cities = new ArrayList<>();
        for (Route playerRoute : builtSegments) {
            if (cityStart.equals(playerRoute.getCity1()) && !cityPrevious.equals(playerRoute.getCity2())) {
                cities.add(playerRoute.getCity2());
            }
            if (cityStart.equals(playerRoute.getCity2()) && !cityPrevious.equals(playerRoute.getCity1())) {
                cities.add(playerRoute.getCity1());
            }
        }
        return cities;
    }

    public int findLongestPath() {
        maxLength = 0;
        int length = 0;
        Set<String> citiesSet = new HashSet<>();
        for (Route segment : builtSegments) {
            citiesSet.add(segment.getCity1());
            citiesSet.add(segment.getCity2());
        }

        List<String> cities = new ArrayList<>(citiesSet);
        for (String cityStart : cities) {
            findLongestPathFromCity(cityStart, builtSegments, length);
        }
        return maxLength;
    }


    private void findLongestPathFromCity(String cityStart, Set<Route> segments, int length) {
        for (Route segment : segments) {
            String cityNext = null;
            if (cityStart.equals(segment.getCity1())) {
                cityNext = segment.getCity2();
            }
            if (cityStart.equals(segment.getCity2())) {
                cityNext = segment.getCity1();
            }
            if (cityNext != null) {
                Set<Route> segmentsWithoutSegment = new HashSet<>(segments);
                segmentsWithoutSegment.remove(segment);
                findLongestPathFromCity(cityNext, segmentsWithoutSegment, length + segment.getLength());
            }
        }
        if(length > maxLength) {
            maxLength = length;
        }
    }

}
