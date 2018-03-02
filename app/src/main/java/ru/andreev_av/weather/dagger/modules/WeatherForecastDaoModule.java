package ru.andreev_av.weather.dagger.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.dagger.scope.ForWeatherForecast;
import ru.andreev_av.weather.data.db.IWeatherForecastDao;
import ru.andreev_av.weather.data.db.WeatherForecastDao;

@Module
public class WeatherForecastDaoModule {

    @Provides
    @ForWeatherForecast
    public IWeatherForecastDao provideWeatherForecastDao(Context context) {
        return WeatherForecastDao.getInstance(context.getApplicationContext());
    }
}
