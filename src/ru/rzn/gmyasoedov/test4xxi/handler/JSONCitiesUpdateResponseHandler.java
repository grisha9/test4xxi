package ru.rzn.gmyasoedov.test4xxi.handler;

import android.content.Context;
import android.util.Log;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.rzn.gmyasoedov.test4xxi.bean.Weather;
import ru.rzn.gmyasoedov.test4xxi.WeatherUtils;
import ru.rzn.gmyasoedov.test4xxi.db.CityProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * handler for update cities
 */
public class JSONCitiesUpdateResponseHandler extends BaseJsonHttpResponseHandler<List<Weather>> {
    private static final String TAG = JSONCitiesUpdateResponseHandler.class.getSimpleName();
    private Context context;

    public JSONCitiesUpdateResponseHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int i, Header[] headers, String s, List<Weather> weathers) {
        try {
            for (Weather weather : weathers) {
                context.getContentResolver().update(CityProvider.CITY_CONTENT_URI,
                        WeatherUtils.getContentValuesFromWeather(weather),
                        WeatherUtils.getWhereFromInt(weather.getId()), null);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, Throwable throwable, String s, List<Weather> weathers) {
        Log.e(TAG, throwable.toString(), throwable);
    }

    @Override
    protected List<Weather> parseResponse(String s, boolean b) throws Throwable {
        List<Weather> list = new ArrayList<Weather>();
        JSONArray cities = new JSONObject(s).getJSONArray(WeatherUtils.JSON_LIST);
        for (int i = 0; i < cities.length(); i++) {
            JSONObject weatherJson = cities.getJSONObject(i);
            Weather weather = WeatherUtils.getWeatherFromJSON(weatherJson);
            list.add(weather);
        }
        return list;
    }
}
