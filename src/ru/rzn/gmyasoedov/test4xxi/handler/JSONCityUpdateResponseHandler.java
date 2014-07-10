package ru.rzn.gmyasoedov.test4xxi.handler;

import android.util.Log;
import android.widget.Toast;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONObject;
import ru.rzn.gmyasoedov.test4xxi.R;
import ru.rzn.gmyasoedov.test4xxi.WeatherActivity;
import ru.rzn.gmyasoedov.test4xxi.WeatherRestClient;
import ru.rzn.gmyasoedov.test4xxi.WeatherUtils;
import ru.rzn.gmyasoedov.test4xxi.bean.Weather;
import ru.rzn.gmyasoedov.test4xxi.db.CityProvider;
import ru.rzn.gmyasoedov.test4xxi.fragment.CityWeatherFragment;

/**
 * handler for update city
 */
public class JSONCityUpdateResponseHandler extends BaseJsonHttpResponseHandler<Weather> {
    private static final String TAG = JSONCityUpdateResponseHandler.class.getSimpleName();
    private WeatherActivity weatherActivity;
    private CityWeatherFragment fragment;
    private boolean showDialog;

    public JSONCityUpdateResponseHandler(CityWeatherFragment fragment, boolean showDialog) {
        this.weatherActivity = (WeatherActivity) fragment.getActivity();
        this.fragment = fragment;
        this.showDialog = showDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (showDialog) {
            weatherActivity.showDialog();
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (showDialog) {
            weatherActivity.hideDialog();
        }
    }

    @Override
    public void onSuccess(int i, Header[] headers, String s, Weather weather) {
        try {
            weatherActivity.getContentResolver().update(CityProvider.CITY_CONTENT_URI,
                    WeatherUtils.getContentValuesFromWeather(weather),
                    WeatherUtils.getWhereFromInt(weather.getId()), null);
            fragment.updateWeather(fragment.getView(), weather);
            WeatherRestClient.getForecastById(weather.getId(), new JSONForecastResponseHandler(fragment, showDialog));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, Throwable throwable, String s, Weather weather) {
        try {
            fragment.offlineForecast();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Log.e(TAG, throwable.toString());
    }

    @Override
    protected Weather parseResponse(String s, boolean b) throws Throwable {
        return WeatherUtils.getWeatherFromJSON(new JSONObject(s));
    }

}
