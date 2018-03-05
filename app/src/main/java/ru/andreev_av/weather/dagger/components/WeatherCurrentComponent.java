package ru.andreev_av.weather.dagger.components;

import dagger.Subcomponent;
import ru.andreev_av.weather.dagger.scope.ForWeatherCurrent;
import ru.andreev_av.weather.presentation.activities.WeatherCurrentDetailsActivity;

@ForWeatherCurrent
@Subcomponent
public interface WeatherCurrentComponent {

    void inject(WeatherCurrentDetailsActivity activity);
}
