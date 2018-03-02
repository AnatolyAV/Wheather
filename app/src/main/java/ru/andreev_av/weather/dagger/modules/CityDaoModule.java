package ru.andreev_av.weather.dagger.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.dagger.scope.ForCities;
import ru.andreev_av.weather.data.db.CityDao;
import ru.andreev_av.weather.data.db.ICityDao;

@Module
public class CityDaoModule {

    @Provides
    @ForCities
    public ICityDao provideCityDao(Context context) {
        return CityDao.getInstance(context.getApplicationContext());
    }
}
