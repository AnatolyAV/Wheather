package ru.andreev_av.weather.ui.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.andreev_av.weather.R;

public class WeatherForecastViewHolder extends RecyclerView.ViewHolder {

    public TextView tvDateTime;
    public TextView tvDescription;
    public TextView tvTemperatureMin;
    public TextView tvTemperatureMax;
    public TextView tvIcon;

    public WeatherForecastViewHolder(View itemView) {
        super(itemView);

        tvDateTime = (TextView) itemView.findViewById(R.id.tv_forecast_date_time);
        tvDescription = (TextView) itemView.findViewById(R.id.tv_forecast_description);
        tvTemperatureMin = (TextView) itemView.findViewById(R.id.tv_forecast_temperature_min);
        tvTemperatureMax = (TextView) itemView.findViewById(R.id.tv_forecast_temperature_max);
        tvIcon = (TextView) itemView.findViewById(R.id.tv_forecast_icon);
    }
}
