package ru.rzn.gmyasoedov.test4xxi.adapter;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import ru.rzn.gmyasoedov.test4xxi.bean.Weather;
import ru.rzn.gmyasoedov.test4xxi.WeatherUtils;

/**
 * adapter for city weather
 */
public class WeatherAdapter extends SimpleCursorAdapter {
    public WeatherAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    public WeatherAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public Weather getItem(int position) {
        Cursor cursor = (Cursor) super.getItem(position);
        return (cursor != null) ? WeatherUtils.getWeatherFromCursor(cursor) : null;
    }
}
