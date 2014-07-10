package ru.rzn.gmyasoedov.test4xxi;

import android.text.TextUtils;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.List;

/**
 * Http client
 */
public class WeatherRestClient {
    public static final String BASE_ICON_URL = "http://openweathermap.org/img/w/";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String GROUP = "group";
    private static final String WEATHER = "weather";
    private static final String FORECAST = "forecast/daily";
    private static final String GROUP_DELIMITER = ",";
    private static final String PARAM_ID = "id";
    private static final String PARAM_LAT = "lat";
    private static final String PARAM_LON = "lon";
    private static final String PARAM_UNIT = "units";
    private static final String PARAM_METRIC_VALUE = "metric";
    private static final String PARAM_CNT = "cnt";
    private static final String PARAM_CNT_VALUE = "7";

    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     *  get weather for cities - list with city id
     * @param listId city ids
     * @param responseHandler handler for response
     */
    public static void getWeatherByIds(List<Integer> listId, ResponseHandlerInterface responseHandler) {
        String ids = TextUtils.join(GROUP_DELIMITER, listId.toArray());
        RequestParams requestParams = new MetricRequestParams();
        requestParams.add(PARAM_ID, ids);
        client.get(BASE_URL + GROUP, requestParams, responseHandler);
    }

    /**
     * forecast by city id
     * @param id city id
     * @param responseHandler handler for response
     */
    public static void getForecastById(int id, ResponseHandlerInterface responseHandler) {
        RequestParams requestParams = new MetricRequestParams();
        requestParams.add(PARAM_ID, String.valueOf(id));
        requestParams.add(PARAM_CNT, PARAM_CNT_VALUE);
        client.get(BASE_URL + FORECAST, requestParams, responseHandler);
    }

    /**
     * get weather by lat lon
     * @param latLng lat lon
     * @param responseHandler handler for response
     */
    public static void getWeatherByLatLng(LatLng latLng, ResponseHandlerInterface responseHandler) {
        RequestParams requestParams = new MetricRequestParams();
        requestParams.add(PARAM_LAT, String.valueOf(latLng.latitude));
        requestParams.add(PARAM_LON, String.valueOf(latLng.longitude));
        client.get(BASE_URL + WEATHER, requestParams, responseHandler);
    }

    /**
     * get weather by id
     * @param id city id
     * @param responseHandler handler for response
     */
    public static void getWeatherById(int id, ResponseHandlerInterface responseHandler) {
        RequestParams requestParams = new MetricRequestParams();
        requestParams.add(PARAM_ID, String.valueOf(id));
        client.get(BASE_URL + WEATHER, requestParams, responseHandler);
    }

    private static class MetricRequestParams extends RequestParams {
        public MetricRequestParams() {
            add(PARAM_UNIT, PARAM_METRIC_VALUE);
        }
    }
}
