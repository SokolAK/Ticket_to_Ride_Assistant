package pl.sokolak.TicketToRideAssistant.Calculators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.Domain.Route;

public final class ConnectionCalculator {
    private Set<Route> builtSegments;
    private Set<Route> builtRoutes;
    private int maxLength = 0;

    public ConnectionCalculator(Player player) {
        builtRoutes = new HashSet<>(player.getBuiltRoutes());
        builtSegments = new HashSet<>(builtRoutes);
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
        List<String> cities2 = getCitiesConnectedToCityWithoutPrevious(builtSegments, cityStart, cityPrevious);

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

    public List<String> getCitiesConnectedToCityWithoutPrevious(Set<Route> builtSegments, String cityStart, String cityPrevious) {
        List<String> cities = new ArrayList<>();
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
        for (Route route : builtRoutes) {
            citiesSet.add(route.getCity1());
            citiesSet.add(route.getCity2());
        }

        List<String> cities = new ArrayList<>(citiesSet);
        for (String cityStart : cities) {
            findLongestPathFromCity(cityStart, builtRoutes, length);
        }
        return maxLength;
    }

    private void findLongestPathFromCity(String cityStart, Set<Route> routes, int length) {
        for (Route route : routes) {
            String cityNext = null;
            if (cityStart.equals(route.getCity1())) {
                cityNext = route.getCity2();
            }
            if (cityStart.equals(route.getCity2())) {
                cityNext = route.getCity1();
            }
            if (cityNext != null) {
                Set<Route> routesWithoutRoute = new HashSet<>(routes);
                routesWithoutRoute.remove(route);
                findLongestPathFromCity(cityNext, routesWithoutRoute, length + route.getLength());
            }
        }
        if (length > maxLength) {
            maxLength = length;
        }
    }

}
