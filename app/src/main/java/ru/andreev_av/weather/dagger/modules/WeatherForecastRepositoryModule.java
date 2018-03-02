package ru.andreev_av.weather.dagger.modules;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.dagger.scope.ForWeatherForecast;
import ru.andreev_av.weather.data.db.IWeatherForecastDao;
import ru.andreev_av.weather.data.repository.IWeatherForecastRepository;
import ru.andreev_av.weather.data.repository.WeatherForecastRepository;

@Module(includes = {WeatherForecastDaoModule.class})
public class WeatherForecastRepositoryModule {

    @Provides
    @ForWeatherForecast
    public IWeatherForecastRepository provideWeatherForecastRepository(IWeatherForecastDao weatherForecastDao) {
        return new WeatherForecastRepository(weatherForecastDao);
    }
}
