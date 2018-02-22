package ru.andreev_av.weather.dagger.components;

import dagger.Subcomponent;
import ru.andreev_av.weather.dagger.modules.WeatherCurrentUseCaseModule;
import ru.andreev_av.weather.dagger.scope.ForWeatherCurrent;
import ru.andreev_av.weather.ui.activities.CitiesListActivity;

@ForWeatherCurrent
@Subcomponent(modules = {WeatherCurrentUseCaseModule.class})
public interface WeatherCurrentComponent {
    void inject(CitiesListActivity activity);
}
