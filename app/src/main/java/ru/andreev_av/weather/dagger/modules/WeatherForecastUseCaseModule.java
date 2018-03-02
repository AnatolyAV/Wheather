package ru.andreev_av.weather.dagger.modules;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.dagger.scope.ForWeatherForecast;
import ru.andreev_av.weather.data.repository.IWeatherForecastRepository;
import ru.andreev_av.weather.domain.usecase.IWeatherForecastUseCase;
import ru.andreev_av.weather.domain.usecase.WeatherForecastUseCase;

@Module(includes = {WeatherForecastRepositoryModule.class})
public class WeatherForecastUseCaseModule {

    @Provides
    @ForWeatherForecast
    public IWeatherForecastUseCase provideWeatherForecastUseCase(IWeatherForecastRepository weatherForecastRepository) {
        return new WeatherForecastUseCase(weatherForecastRepository);
    }
}
