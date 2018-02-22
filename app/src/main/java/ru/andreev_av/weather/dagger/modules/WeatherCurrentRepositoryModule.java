package ru.andreev_av.weather.dagger.modules;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.dagger.scope.ForWeatherCurrent;
import ru.andreev_av.weather.data.db.IWeatherCurrentDao;
import ru.andreev_av.weather.data.repository.IWeatherCurrentRepository;
import ru.andreev_av.weather.data.repository.WeatherCurrentRepository;

@Module(includes = {WeatherCurrentDaoModule.class})
public class WeatherCurrentRepositoryModule {

    @Provides
    @ForWeatherCurrent
    public IWeatherCurrentRepository provideWeatherCurrentRepository(IWeatherCurrentDao weatherCurrentDao) {
        return new WeatherCurrentRepository(weatherCurrentDao);
    }
}
