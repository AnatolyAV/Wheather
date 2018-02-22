package ru.andreev_av.weather.dagger.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.dagger.scope.ForWeatherCurrent;
import ru.andreev_av.weather.data.db.IWeatherCurrentDao;
import ru.andreev_av.weather.data.db.WeatherCurrentDao;

@Module
public class WeatherCurrentDaoModule {

    @Provides
    @ForWeatherCurrent
    public IWeatherCurrentDao provideWeatherCurrentDao(Context context) {
        return WeatherCurrentDao.getInstance(context.getApplicationContext());
    }
}
