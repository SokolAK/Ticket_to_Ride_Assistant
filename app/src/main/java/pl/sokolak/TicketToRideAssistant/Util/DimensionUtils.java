package pl.sokolak.TicketToRideAssistant.Util;

import android.content.Context;

import pl.sokolak.TicketToRideAssistant.R;

public class DimensionUtils {
    private Context context;

    public static int getDimension(Context context, int resource) {
        float dimension = context.getResources().getDimension(resource);
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dimension/density);
    }
}
