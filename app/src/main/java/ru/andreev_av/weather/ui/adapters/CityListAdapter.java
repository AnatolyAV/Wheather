package ru.andreev_av.weather.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.model.WeatherCurrentModel;
import ru.andreev_av.weather.ui.adapters.holders.CityViewHolder;
import ru.andreev_av.weather.utils.ImageUtils;
import ru.andreev_av.weather.utils.UnitUtils;

public class CityListAdapter extends RecyclerView.Adapter<CityViewHolder> {

    private Context context;
    private List<WeatherCurrentModel> weatherCurrentModelList;

    public CityListAdapter(Context context, List<WeatherCurrentModel> weatherCurrentModelList) {
        this.context = context;
        this.weatherCurrentModelList = weatherCurrentModelList;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        Typeface weatherFontIcon = Typeface.createFromAsset(context.getAssets(), "fonts/weathericons-regular-webfont.ttf");

        WeatherCurrentModel weatherCurrentModel = weatherCurrentModelList.get(position);

        holder.tvCityName.setText(weatherCurrentModel.getCityName());
        holder.imgWeatherCurrentIcon.setTypeface(weatherFontIcon);
        holder.imgWeatherCurrentIcon.setText(ImageUtils.getStrIcon(context, weatherCurrentModel.getWeather().get(0).getIcon()));
        // TODO когда добавлю "Настройки" поменять для работы и с °F
        holder.tvWeatherCurrentTemp.setText(context.getString(R.string.temperature_with_degree, UnitUtils.getFormatTemperature(weatherCurrentModel.getMain().getTemperature())));

    }

    @Override
    public int getItemCount() {
        return weatherCurrentModelList.size();
    }

    public void refreshList(List<WeatherCurrentModel> list) {
        weatherCurrentModelList = list;
    }
}
