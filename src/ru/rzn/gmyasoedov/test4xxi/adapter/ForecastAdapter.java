package ru.rzn.gmyasoedov.test4xxi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import ru.rzn.gmyasoedov.test4xxi.R;
import ru.rzn.gmyasoedov.test4xxi.WeatherUtils;
import ru.rzn.gmyasoedov.test4xxi.bean.Forecast;

import java.util.List;

/**
 * adapter for forecast
 */
public class ForecastAdapter extends ArrayAdapter<Forecast> {
    private Context context;
    private List<Forecast> forecasts;
    private LayoutInflater inflater;
    public ForecastAdapter(Context context, List<Forecast> forecasts) {
        super(context, R.layout.layout_forecast_item, forecasts);
        this.context = context;
        this.forecasts = forecasts;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_forecast_item, null);
            holder.day = (TextView) convertView.findViewById(R.id.day);
            holder.tempDay = (TextView) convertView.findViewById(R.id.temp_day);
            holder.tempNight = (TextView) convertView.findViewById(R.id.temp_night);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.day.setText(WeatherUtils.getStringFromDate(forecasts.get(position).getDate()));
        holder.tempDay.setText(String.valueOf(forecasts.get(position).getTempDay()));
        holder.tempNight.setText(String.valueOf(forecasts.get(position).getTempNight()));
        ImageLoader.getInstance().displayImage(forecasts.get(position).getIcon(), holder.icon);
        return convertView;
    }

    private class ViewHolder {
        TextView day;
        TextView tempDay;
        TextView tempNight;
        ImageView icon;
    }
}
