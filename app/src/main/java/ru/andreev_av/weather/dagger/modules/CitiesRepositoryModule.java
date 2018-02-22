package ru.andreev_av.weather.dagger.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.data.db.IWeatherCurrentDao;
import ru.andreev_av.weather.data.repository.IWeatherCurrentRepository;
import ru.andreev_av.weather.data.repository.WeatherCurrentRepository;

@Module(includes = {WeatherCurrentDaoModule.class})
public class CitiesRepositoryModule {

    @Provides
    @Singleton
    public IWeatherCurrentRepository provideCitiesUseCase(IWeatherCurrentDao weatherCurrentDao) {
        return new WeatherCurrentRepository(weatherCurrentDao);
    }
}
