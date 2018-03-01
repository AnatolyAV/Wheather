package ru.andreev_av.weather.dagger.components;

import dagger.Subcomponent;
import ru.andreev_av.weather.dagger.modules.WeatherCurrentUseCaseModule;
import ru.andreev_av.weather.dagger.scope.ForWeatherCurrent;
import ru.andreev_av.weather.presentation.activities.CitiesListActivity;
import ru.andreev_av.weather.presentation.activities.WeatherCurrentDetailsActivity;

@ForWeatherCurrent
@Subcomponent(modules = {WeatherCurrentUseCaseModule.class})
public interface WeatherCurrentComponent {

    void inject(CitiesListActivity activity);

    void inject(WeatherCurrentDetailsActivity activity);
}
