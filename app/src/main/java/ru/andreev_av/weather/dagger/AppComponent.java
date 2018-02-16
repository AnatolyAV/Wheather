package ru.andreev_av.weather.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.andreev_av.weather.dagger.modules.CitiesUseCaseModule;
import ru.andreev_av.weather.dagger.modules.ConnectionDetectorModule;
import ru.andreev_av.weather.ui.activities.CityListActivity;

@Singleton
@Component(modules = {CitiesUseCaseModule.class, ConnectionDetectorModule.class})
public interface AppComponent {
    void inject(CityListActivity activity);
}
