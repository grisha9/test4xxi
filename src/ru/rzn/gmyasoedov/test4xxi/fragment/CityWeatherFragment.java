package ru.rzn.gmyasoedov.test4xxi.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import ru.rzn.gmyasoedov.test4xxi.R;
import ru.rzn.gmyasoedov.test4xxi.WeatherRestClient;
import ru.rzn.gmyasoedov.test4xxi.adapter.ForecastAdapter;
import ru.rzn.gmyasoedov.test4xxi.bean.Forecast;
import ru.rzn.gmyasoedov.test4xxi.bean.Weather;
import ru.rzn.gmyasoedov.test4xxi.WeatherUtils;
import ru.rzn.gmyasoedov.test4xxi.db.CityProvider;
import ru.rzn.gmyasoedov.test4xxi.handler.JSONCitiesUpdateResponseHandler;
import ru.rzn.gmyasoedov.test4xxi.handler.JSONCityUpdateResponseHandler;
import ru.rzn.gmyasoedov.test4xxi.handler.JSONForecastResponseHandler;

import java.util.List;

/**
 * Weather for city
 */
public class CityWeatherFragment extends Fragment implements ActionBar.OnNavigationListener{
    private static final int DAYS_3 =0;
    private Weather weather;
    private ListView listView;
    private TextView offline;
    private boolean isOffline;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        weather = (Weather) getArguments().getSerializable(WeatherUtils.PARAM_WEATHER);
        WeatherRestClient.getWeatherById(weather.getId(), new JSONCityUpdateResponseHandler(this, false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, new String[]{getString(R.string.forecast3),
                getString(R.string.forecast7)});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setTitle(weather.getName());
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getActivity().getActionBar().setListNavigationCallbacks(adapter, this);

        View view = inflater.inflate(R.layout.layout_city_weather, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        offline = (TextView) view.findViewById(R.id.offline);
        if (isOffline) {
            offline.setVisibility(View.VISIBLE);
        } else {
            offline.setVisibility(View.GONE);
        }
        updateWeather(view, weather);
        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_action_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.refresh:
                WeatherRestClient.getWeatherById(weather.getId(), new JSONCityUpdateResponseHandler(this, true));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        addAdapterToList(listView, weather.getForecasts(), itemPosition);
        return false;
    }

    public void updateWeather(View view, Weather weather) {
        this.weather = weather;
        ((TextView) view.findViewById(R.id.temp)).setText(String.valueOf(weather.getTemp()));
        ((TextView) view.findViewById(R.id.pressure)).setText(String.valueOf(weather.getPressure()));
        ((TextView) view.findViewById(R.id.humidity)).setText(String.valueOf(weather.getHumidity()));
        ((TextView) view.findViewById(R.id.date)).setText(WeatherUtils.getStringFromDate(weather.getDate()));
        ImageLoader.getInstance().displayImage(weather.getIcon(), (ImageView) view.findViewById(R.id.icon));
    }

    public void onlineForecast(List<Forecast> forecasts) {
        offline.setVisibility(View.GONE);
        isOffline = false;
        weather.setForecasts(forecasts);
        getActivity().getContentResolver().update(CityProvider.CITY_CONTENT_URI,
                WeatherUtils.getContentValuesFromWeather(weather),
                WeatherUtils.getWhereFromInt(weather.getId()), null);
        addAdapterToList(listView, weather.getForecasts(), getActivity().getActionBar().getSelectedNavigationIndex());
    }

    public void offlineForecast() {
        offline.setVisibility(View.VISIBLE);
        isOffline = true;
        Toast.makeText(getActivity(), getString(R.string.error_add_marker), Toast.LENGTH_LONG).show();
    }

    private void addAdapterToList(ListView listView, List<Forecast> forecasts, int mode) {
        if (forecasts != null) {
            if (mode == DAYS_3) {
                listView.setAdapter(new ForecastAdapter(getActivity(), forecasts.subList(0, 3)));
            } else {
                listView.setAdapter(new ForecastAdapter(getActivity(), forecasts));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }
}
