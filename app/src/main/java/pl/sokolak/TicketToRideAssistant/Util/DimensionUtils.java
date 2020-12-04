package pl.sokolak.TicketToRideAssistant.Util;

import android.content.Context;

public class DimensionUtils {
    public static int getDimension(Context context, int resource) {
        float dimension = context.getResources().getDimension(resource);
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dimension / density);
    }
}
