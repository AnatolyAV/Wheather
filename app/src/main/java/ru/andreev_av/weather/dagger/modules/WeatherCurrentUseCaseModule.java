package ru.andreev_av.weather.dagger.modules;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.dagger.scope.ForWeatherCurrent;
import ru.andreev_av.weather.data.repository.IWeatherCurrentRepository;
import ru.andreev_av.weather.domain.usecase.IWeatherCurrentUseCase;
import ru.andreev_av.weather.domain.usecase.WeatherCurrentUseCase;

@Module(includes = {WeatherCurrentRepositoryModule.class})
public class WeatherCurrentUseCaseModule {

    @Provides
    @ForWeatherCurrent
    public IWeatherCurrentUseCase provideWeatherCurrentUseCase(IWeatherCurrentRepository weatherCurrentRepository) {
        return new WeatherCurrentUseCase(weatherCurrentRepository);
    }
}
