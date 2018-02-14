package ru.andreev_av.weather.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.domain.model.WeatherCurrent;
import ru.andreev_av.weather.ui.adapters.holders.CityViewHolder;
import ru.andreev_av.weather.utils.ImageUtils;
import ru.andreev_av.weather.utils.UnitUtils;

public class CityListAdapter extends RecyclerView.Adapter<CityViewHolder> {

    private Context mContext;
    private List<WeatherCurrent> mWeatherCurrents;

    public CityListAdapter(Context context, List<WeatherCurrent> weatherCurrents) {
        mContext = context;
        mWeatherCurrents = weatherCurrents;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        Typeface weatherFontIcon = Typeface.createFromAsset(mContext.getAssets(), "fonts/weathericons-regular-webfont.ttf");

        WeatherCurrent weatherCurrent = mWeatherCurrents.get(position);

        holder.tvCityName.setText(weatherCurrent.getCityName());
        holder.imgWeatherCurrentIcon.setTypeface(weatherFontIcon);
        holder.imgWeatherCurrentIcon.setText(ImageUtils.getStrIcon(mContext, weatherCurrent.getWeathers().get(0).getIcon()));
        // TODO когда добавлю "Настройки" поменять для работы и с °F
        holder.tvWeatherCurrentTemp.setText(mContext.getString(R.string.temperature_with_degree, UnitUtils.getFormatTemperature(weatherCurrent.getMain().getTemperature())));

    }

    @Override
    public int getItemCount() {
        return mWeatherCurrents.size();
    }

    public void refreshList(List<WeatherCurrent> weatherCurrents) {
        mWeatherCurrents = weatherCurrents;
        notifyDataSetChanged();
    }
}
