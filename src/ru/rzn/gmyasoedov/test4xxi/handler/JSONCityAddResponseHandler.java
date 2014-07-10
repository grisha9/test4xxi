package ru.rzn.gmyasoedov.test4xxi.handler;

import android.util.Log;
import android.widget.Toast;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONObject;
import ru.rzn.gmyasoedov.test4xxi.R;
import ru.rzn.gmyasoedov.test4xxi.WeatherActivity;
import ru.rzn.gmyasoedov.test4xxi.bean.Weather;
import ru.rzn.gmyasoedov.test4xxi.WeatherUtils;
import ru.rzn.gmyasoedov.test4xxi.db.CityProvider;

/**
 * handler for add city
 */
public class JSONCityAddResponseHandler extends BaseJsonHttpResponseHandler<Weather> {
    private static final String TAG = JSONCityAddResponseHandler.class.getSimpleName();
    private WeatherActivity weatherActivity;

    public JSONCityAddResponseHandler(WeatherActivity context) {
        this.weatherActivity = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        weatherActivity.showDialog();
    }

    @Override
    public void onFinish() {
        super.onFinish();
        weatherActivity.hideDialog();
    }

    @Override
    public void onSuccess(int i, Header[] headers, String s, Weather weather) {
        try {
            weatherActivity.getContentResolver().insert(CityProvider.CITY_CONTENT_URI,
                    WeatherUtils.getContentValuesFromWeather(weather));
            weatherActivity.getFragmentManager().popBackStack();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, Throwable throwable, String s, Weather weather) {
        try {
            Toast.makeText(weatherActivity, weatherActivity.getString(R.string.error_add_marker), Toast.LENGTH_LONG).show();
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
