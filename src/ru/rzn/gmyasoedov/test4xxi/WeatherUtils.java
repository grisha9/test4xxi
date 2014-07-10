package ru.rzn.gmyasoedov.test4xxi;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import ru.rzn.gmyasoedov.test4xxi.bean.Forecast;
import ru.rzn.gmyasoedov.test4xxi.bean.Weather;
import ru.rzn.gmyasoedov.test4xxi.db.CityDBHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * utils for work with JSON
 */
public class WeatherUtils {
    public static final String PARAM_WEATHER = "parameter-weather";
    public static final String JSON_LIST = "list";
    private static final String TAG = WeatherUtils.class.getSimpleName();
    private static final String JSON_ID = "id";
    private static final String JSON_NAME = "name";
    private static final String JSON_TEMP = "temp";
    private static final String JSON_TEMP_DAY = "day";
    private static final String JSON_TEMP_NIGHT = "night";
    private static final String JSON_PRESSURE = "pressure";
    private static final String JSON_HUMIDITY = "humidity";
    private static final String JSON_ICON = "icon";
    private static final String JSON_MAIN = "main";
    private static final String JSON_WEATHER = "weather";
    private static final String JSON_DATE = "dt";
    private static final String ICON_EXT = ".png";
    private static final String DATE_FORMAT = "E dd.M.yy";
    private static final int MILISECOND = 1000;

    /**
     * get weather from
     * @param object json with weather
     * @return weather
     * @throws JSONException
     */
    public static Weather getWeatherFromJSON(JSONObject object) throws JSONException {
        Weather weather = new Weather();
        weather.setId(object.getInt(JSON_ID));
        weather.setName(object.getString(JSON_NAME));
        weather.setTemp(object.getJSONObject(JSON_MAIN).getDouble(JSON_TEMP));
        weather.setPressure(object.getJSONObject(JSON_MAIN).getInt(JSON_PRESSURE));
        weather.setHumidity(object.getJSONObject(JSON_MAIN).getInt(JSON_HUMIDITY));
        weather.setDate(new Date((long) object.getInt(JSON_DATE) * MILISECOND));
        weather.setIcon(WeatherRestClient.BASE_ICON_URL
                + object.getJSONArray(JSON_WEATHER).getJSONObject(0).getString(JSON_ICON) + ICON_EXT);
        return weather;
    }

    /**
     * forecast from json
     * @param object json with forecast
     * @return forecast
     * @throws JSONException
     */
    public static Forecast getForecastFromJSON(JSONObject object) throws JSONException {
        Forecast forecast = new Forecast();
        forecast.setDate(new Date((long) object.getInt(JSON_DATE) * MILISECOND));
        forecast.setIcon(WeatherRestClient.BASE_ICON_URL
                + object.getJSONArray(JSON_WEATHER).getJSONObject(0).getString(JSON_ICON) + ICON_EXT);
        forecast.setTempDay(object.getJSONObject(JSON_TEMP).getDouble(JSON_TEMP_DAY));
        forecast.setTempNight(object.getJSONObject(JSON_TEMP).getDouble(JSON_TEMP_NIGHT));
        return forecast;
    }

    /**
     * get content values from weather
     * @param weather weather
     * @return ContentValues
     */
    public static ContentValues getContentValuesFromWeather(Weather weather) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CityDBHelper.COLUMN_ID, weather.getId());
        contentValues.put(CityDBHelper.COLUMN_NAME, weather.getName());
        contentValues.put(CityDBHelper.COLUMN_IMAGE, weather.getIcon());
        contentValues.put(CityDBHelper.COLUMN_HUMIDITY, weather.getHumidity());
        contentValues.put(CityDBHelper.COLUMN_TEMP, weather.getTemp());
        contentValues.put(CityDBHelper.COLUMN_PRESSURE, weather.getPressure());
        contentValues.put(CityDBHelper.COLUMN_DATE, weather.getDate().getTime());
        if (weather.getForecasts() != null) {
            contentValues.put(CityDBHelper.COLUMN_FORECAST, getByteArrayFromForecasts(weather.getForecasts()));
        }
        return contentValues;
    }

    /**
     * position from cursor to weather
     * @param cursor cursor position
     * @return weather
     */
    public static Weather getWeatherFromCursor(Cursor cursor) {
        Weather weather = new Weather();
        weather.setId(cursor.getInt(cursor.getColumnIndex(CityDBHelper.COLUMN_ID)));
        weather.setName(cursor.getString(cursor.getColumnIndex(CityDBHelper.COLUMN_NAME)));
        weather.setIcon(cursor.getString(cursor.getColumnIndex(CityDBHelper.COLUMN_IMAGE)));
        weather.setHumidity(cursor.getInt(cursor.getColumnIndex(CityDBHelper.COLUMN_HUMIDITY)));
        weather.setTemp(cursor.getDouble(cursor.getColumnIndex(CityDBHelper.COLUMN_TEMP)));
        weather.setPressure(cursor.getInt(cursor.getColumnIndex(CityDBHelper.COLUMN_PRESSURE)));
        weather.setDate(new Date(cursor.getLong(cursor.getColumnIndex(CityDBHelper.COLUMN_DATE))));
        weather.setForecasts(getForecastsFromByteArray(cursor.getBlob(cursor
                .getColumnIndex(CityDBHelper.COLUMN_FORECAST))));
        return weather;
    }

    public static String getWhereFromInt(int weatherId) {
        return CityDBHelper.COLUMN_ID + "=" + weatherId;
    }

    /**
     * forecasts to byte array
     * @param obj list of forecast
     * @return byte array
     */
    public static byte[] getByteArrayFromForecasts(List<Forecast> obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        byte[] result = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            result = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
        return result;
    }

    public static List<Forecast> getForecastsFromByteArray(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = null;
        List<Forecast> result = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            result = (List<Forecast>) objectInputStream.readObject();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
        return result;
    }

    /**
     * format date to string
     * @param date date
     * @return string
     */
    public static String getStringFromDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }
}
