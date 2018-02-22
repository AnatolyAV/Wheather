package ru.andreev_av.weather.dagger.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.data.repository.IWeatherCurrentRepository;
import ru.andreev_av.weather.domain.usecase.IWeatherCurrentUseCase;
import ru.andreev_av.weather.domain.usecase.WeatherCurrentUseCase;

@Module(includes = {WeatherCurrentRepositoryModule.class})
public class WeatherCurrentUseCaseModule {

    @Provides
    @Singleton
    public IWeatherCurrentUseCase provideCitiesUseCase(IWeatherCurrentRepository citiesRepository) {
        return new WeatherCurrentUseCase(citiesRepository);
    }
}
