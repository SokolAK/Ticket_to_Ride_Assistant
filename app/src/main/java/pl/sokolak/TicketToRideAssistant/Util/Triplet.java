package pl.sokolak.TicketToRideAssistant.Util;

public class Triplet<F, S, T> {
    public F first;
    public S second;
    public T third;

    public static <F, S, T> Triplet<F, S, T> create(F first, S second, T third) {
        return new Triplet<F, S, T>(first, second, third);
    }

    private Triplet(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
