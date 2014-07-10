package ru.rzn.gmyasoedov.test4xxi;

import android.database.Cursor;
import android.os.AsyncTask;
import ru.rzn.gmyasoedov.test4xxi.db.CityDBHelper;
import ru.rzn.gmyasoedov.test4xxi.db.CityProvider;
import ru.rzn.gmyasoedov.test4xxi.handler.JSONCitiesUpdateResponseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Task for update weather
 */
public class UpdateWeatherForCities extends AsyncTask<Void, Void, List<Integer>> {
    private static final String TAG = UpdateWeatherForCities.class.getSimpleName();
    private WeatherActivity context;
    private boolean showDialog;

    public UpdateWeatherForCities(WeatherActivity context, boolean showDialog) {
        this.context = context;
        this.showDialog = showDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showDialog) {
            context.showDialog();
        }
    }

    @Override
    protected List<Integer> doInBackground(Void... params) {
        List<Integer> list = new ArrayList<Integer>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(CityProvider.CITY_CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(CityDBHelper.COLUMN_ID));
                list.add(id);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<Integer> integers) {
        if (showDialog) {
            context.hideDialog();
        }
        WeatherRestClient.getWeatherByIds(integers, new JSONCitiesUpdateResponseHandler(context));
    }
}
