package ru.rzn.gmyasoedov.test4xxi.handler;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.rzn.gmyasoedov.test4xxi.R;
import ru.rzn.gmyasoedov.test4xxi.WeatherActivity;
import ru.rzn.gmyasoedov.test4xxi.WeatherUtils;
import ru.rzn.gmyasoedov.test4xxi.bean.Forecast;
import ru.rzn.gmyasoedov.test4xxi.bean.Weather;
import ru.rzn.gmyasoedov.test4xxi.db.CityProvider;
import ru.rzn.gmyasoedov.test4xxi.fragment.CityWeatherFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * handler for add forecast
 */
public class JSONForecastResponseHandler extends BaseJsonHttpResponseHandler<List<Forecast>> {
    private static final String TAG = JSONForecastResponseHandler.class.getSimpleName();
    private CityWeatherFragment fragment;
    private WeatherActivity weatherActivity;
    private boolean showDialog;

    /**
     *  forecast handler
     * @param fragment fragment
     * @param showDialog show progress dailog
     */
    public JSONForecastResponseHandler(CityWeatherFragment fragment, boolean showDialog) {
        this.fragment = fragment;
        this.weatherActivity = (WeatherActivity) fragment.getActivity();
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
    public void onSuccess(int i, Header[] headers, String s, List<Forecast> forecasts) {
        try {
            fragment.onlineForecast(forecasts);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, Throwable throwable, String s, List<Forecast> forecasts) {
        try {
            fragment.offlineForecast();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Log.e(TAG, throwable.toString(), throwable);
    }

    @Override
    protected List<Forecast> parseResponse(String s, boolean b) throws Throwable {
        List<Forecast> list = new ArrayList<Forecast>();
        JSONArray cities = new JSONObject(s).getJSONArray(WeatherUtils.JSON_LIST);
        for (int i = 0; i < cities.length(); i++) {
            JSONObject weatherJson = cities.getJSONObject(i);
            Forecast forecast = WeatherUtils.getForecastFromJSON(weatherJson);
            list.add(forecast);
        }
        return list;
    }
}
