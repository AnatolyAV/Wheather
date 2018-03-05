package ru.andreev_av.weather.dagger.components;

import dagger.Subcomponent;
import ru.andreev_av.weather.dagger.scope.ForWeatherCurrent;
import ru.andreev_av.weather.presentation.activities.CitiesListActivity;

@ForWeatherCurrent
@Subcomponent
public interface WeatherCurrentsComponent {

    void inject(CitiesListActivity activity);
}
