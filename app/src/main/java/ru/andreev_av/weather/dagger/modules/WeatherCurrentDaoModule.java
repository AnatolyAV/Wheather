package ru.andreev_av.weather.dagger.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.data.db.IWeatherCurrentDao;
import ru.andreev_av.weather.data.db.WeatherCurrentDao;

@Module
public class WeatherCurrentDaoModule {

    @Provides
    @Singleton
    public IWeatherCurrentDao provideWeatherCurrentDao(Context context) {
        return WeatherCurrentDao.getInstance(context.getApplicationContext());
    }
}
