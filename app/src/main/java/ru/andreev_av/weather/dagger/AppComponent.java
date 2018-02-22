package ru.andreev_av.weather.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.andreev_av.weather.dagger.modules.ConnectionDetectorModule;
import ru.andreev_av.weather.dagger.modules.WeatherCurrentUseCaseModule;
import ru.andreev_av.weather.ui.activities.CitiesListActivity;

@Singleton
@Component(modules = {WeatherCurrentUseCaseModule.class, ConnectionDetectorModule.class})
public interface AppComponent {
    void inject(CitiesListActivity activity);
}
