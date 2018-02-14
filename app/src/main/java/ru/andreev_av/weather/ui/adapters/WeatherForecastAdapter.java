package ru.andreev_av.weather.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.data.model.WeatherForecast;
import ru.andreev_av.weather.ui.adapters.holders.WeatherForecastViewHolder;
import ru.andreev_av.weather.utils.ImageUtils;
import ru.andreev_av.weather.utils.StringUtils;
import ru.andreev_av.weather.utils.UnitUtils;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastViewHolder> {

    private Context context;
    private List<WeatherForecast> weatherForecastList;

    public WeatherForecastAdapter(Context context, List<WeatherForecast> weatherForecastList) {
        this.context = context;
        this.weatherForecastList = weatherForecastList;
    }

    @Override
    public WeatherForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_forecast_item, parent, false);
        return new WeatherForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherForecastViewHolder holder, int position) {
        Typeface weatherFontIcon = Typeface.createFromAsset(context.getAssets(), "fonts/weathericons-regular-webfont.ttf");

        WeatherForecast weatherForecast = weatherForecastList.get(position);

        holder.tvDateTime.setText(UnitUtils.getFormatDateTimeEEEddMMMM(weatherForecast.getDateTime()));
        holder.tvDescription.setText(StringUtils.firstUpperCase(weatherForecast.getWeather().get(0).getDescription()));
        holder.tvTemperatureMin.setText(context.getString(R.string.temperature_with_degree, UnitUtils.getFormatTemperature(weatherForecast.getTemperature().getMin())));
        holder.tvTemperatureMax.setText(context.getString(R.string.temperature_with_degree, UnitUtils.getFormatTemperature(weatherForecast.getTemperature().getMax())));
        holder.tvIcon.setTypeface(weatherFontIcon);
        holder.tvIcon.setText(ImageUtils.getStrIcon(context, weatherForecast.getWeather().get(0).getIcon()));
    }

    @Override
    public int getItemCount() {
        return weatherForecastList.size();
    }

    public void refreshList(List<WeatherForecast> list) {
        weatherForecastList = list;
    }
}
