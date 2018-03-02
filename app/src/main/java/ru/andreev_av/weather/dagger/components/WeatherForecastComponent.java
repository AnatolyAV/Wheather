package ru.andreev_av.weather.dagger.components;

import dagger.Subcomponent;
import ru.andreev_av.weather.dagger.modules.WeatherForecastUseCaseModule;
import ru.andreev_av.weather.dagger.scope.ForWeatherForecast;
import ru.andreev_av.weather.presentation.activities.WeatherForecastActivity;

@ForWeatherForecast
@Subcomponent(modules = {WeatherForecastUseCaseModule.class})
public interface WeatherForecastComponent {

    void inject(WeatherForecastActivity activity);
}
