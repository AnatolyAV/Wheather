package ru.andreev_av.weather.dagger.components;

import dagger.Subcomponent;
import ru.andreev_av.weather.dagger.modules.CitiesUseCaseModule;
import ru.andreev_av.weather.dagger.scope.ForCities;
import ru.andreev_av.weather.presentation.activities.SplashActivity;
import ru.andreev_av.weather.presentation.fragments.AddCityFragment;

@ForCities
@Subcomponent(modules = {CitiesUseCaseModule.class})
public interface CitiesComponent {

    void inject(AddCityFragment fragment);

    void inject(SplashActivity activity);
}
