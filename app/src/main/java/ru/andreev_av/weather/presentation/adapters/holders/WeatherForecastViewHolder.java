package ru.andreev_av.weather.presentation.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.andreev_av.weather.R;

public class WeatherForecastViewHolder extends RecyclerView.ViewHolder {

    public TextView tvDateTime;
    public TextView tvDescription;
    public TextView tvTemperatureDay;
    public TextView tvTemperatureNight;
    public TextView tvIcon;

    public WeatherForecastViewHolder(View itemView) {
        super(itemView);

        tvDateTime = (TextView) itemView.findViewById(R.id.tv_forecast_date_time);
        tvDescription = (TextView) itemView.findViewById(R.id.tv_forecast_description);
        tvTemperatureDay = (TextView) itemView.findViewById(R.id.tv_forecast_temperature_day);
        tvTemperatureNight = (TextView) itemView.findViewById(R.id.tv_forecast_temperature_night);
        tvIcon = (TextView) itemView.findViewById(R.id.tv_forecast_icon);
    }
}
