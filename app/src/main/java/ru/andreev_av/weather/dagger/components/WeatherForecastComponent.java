package ru.andreev_av.weather.dagger.components;

import dagger.Subcomponent;
import ru.andreev_av.weather.dagger.modules.WeatherForecastUseCaseModule;
import ru.andreev_av.weather.ui.activities.WeatherForecastActivity;

@Subcomponent(modules = {WeatherForecastUseCaseModule.class})
public interface WeatherForecastComponent {

    void inject(WeatherForecastActivity activity);
}
