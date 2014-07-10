package ru.rzn.gmyasoedov.test4xxi.fragment;

import android.app.Fragment;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.AbsListView;
import android.widget.ListView;
import ru.rzn.gmyasoedov.test4xxi.*;
import ru.rzn.gmyasoedov.test4xxi.bean.Weather;
import ru.rzn.gmyasoedov.test4xxi.adapter.WeatherAdapter;
import ru.rzn.gmyasoedov.test4xxi.db.CityDBHelper;
import ru.rzn.gmyasoedov.test4xxi.db.CityProvider;

/**
 * Fragment with list of cities
 */
public class CityListFragment extends ListFragment implements AbsListView.MultiChoiceModeListener {
    private SparseBooleanArray booleanArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        booleanArray = new SparseBooleanArray();
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(this);
        new LoadCityAsync().execute();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_action_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.add:
                Fragment fragment = new AddMapFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, fragment)
                        .addToBackStack(null).commit();
                return true;
            case R.id.refresh:
                new UpdateWeatherForCities((WeatherActivity) getActivity(), true).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Weather weather = ((WeatherAdapter) getListAdapter()).getItem(position);
        Fragment fragment = new CityWeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(WeatherUtils.PARAM_WEATHER, weather);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        final int checkedCount = getListView().getCheckedItemCount();
        mode.setTitle(checkedCount + " " + getString(R.string.label_selected));
        Weather weather = ((WeatherAdapter) getListAdapter()).getItem(position);
        if (weather != null) {
            booleanArray.put(weather.getId(), checked);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                for(int i = 0; i < booleanArray.size(); i++) {
                    if (booleanArray.valueAt(i)) {
                        getActivity().getContentResolver().delete(CityProvider.CITY_CONTENT_URI,
                                WeatherUtils.getWhereFromInt(booleanArray.keyAt(i)), null);
                    }
                }
                // Close CAB
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    private class LoadCityAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
            Cursor cursor = getActivity().getContentResolver().query(CityProvider.CITY_CONTENT_URI, null, null,
                    null, null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            getActivity().startManagingCursor(cursor);
            String from[] = {CityDBHelper.COLUMN_NAME, CityDBHelper.COLUMN_TEMP};
            int to[] = { android.R.id.text1, android.R.id.text2 };
            setListAdapter(new WeatherAdapter(getActivity(),
                    android.R.layout.simple_list_item_activated_2, cursor, from, to));
            new UpdateWeatherForCities((WeatherActivity) getActivity(), false).execute();
        }
    }
}
