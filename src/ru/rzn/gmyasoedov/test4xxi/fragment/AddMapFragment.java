package ru.rzn.gmyasoedov.test4xxi.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import ru.rzn.gmyasoedov.test4xxi.*;
import ru.rzn.gmyasoedov.test4xxi.handler.JSONCityAddResponseHandler;

/**
 * Map fragment
 * choose location
 */
public class AddMapFragment extends Fragment {
    private static final String TAG = AddMapFragment.class.getSimpleName();
    private static final int MAP_PADDING = 50;
    private static final int MAP_TIMEOUT = 300;
    private MapFragment mapFragment;
    private Marker marker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        View view = inflater.inflate(R.layout.layout_map, container, false);
        mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map_container, mapFragment);
        fragmentTransaction.commit();
        view.post(new Runnable() {
            @Override
            public void run() {
                final GoogleMap map = mapFragment.getMap();
                if (map != null) {
                    map.setMyLocationEnabled(true);
                    if (marker != null) {
                        map.addMarker(new MarkerOptions().position(marker.getPosition()));
                    }
                    map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            map.clear();
                            marker = map.addMarker(new MarkerOptions().position(latLng));
                        }
                    });
                }
            }
        });
        return view;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_action_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.select:
                if (marker != null) {
                    WeatherRestClient.getWeatherByLatLng(marker.getPosition(),
                            new JSONCityAddResponseHandler((WeatherActivity) getActivity()));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.error_no_marker), Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
