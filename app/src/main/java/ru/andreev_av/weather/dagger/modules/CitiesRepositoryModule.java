package ru.andreev_av.weather.dagger.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.data.db.IWeatherCurrentDao;
import ru.andreev_av.weather.data.repository.CitiesRepository;
import ru.andreev_av.weather.data.repository.ICitiesRepository;

@Module(includes = {WeatherCurrentDaoModule.class})
public class CitiesRepositoryModule {

    @Provides
    @Singleton
    public ICitiesRepository provideCitiesUseCase(IWeatherCurrentDao weatherCurrentDao) {
        return new CitiesRepository(weatherCurrentDao);
    }
}
