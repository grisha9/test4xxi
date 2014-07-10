package ru.rzn.gmyasoedov.test4xxi;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import ru.rzn.gmyasoedov.test4xxi.fragment.CityListFragment;

public class WeatherActivity extends Activity {
    public static final String TAG_FRAGMENT = "tag-fragment";
    private static final String TAG = WeatherActivity.class.getSimpleName();
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private ProgressDialog dialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fragmentManager = getFragmentManager();
        fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (fragment == null) {
            fragment = new CityListFragment();
            fragmentManager.beginTransaction().replace(R.id.container, fragment, TAG_FRAGMENT).commit();
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    public void hideDialog() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void showDialog() {
        try {
            dialog = ProgressDialog.show(this, null, getString(R.string.connecting));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
