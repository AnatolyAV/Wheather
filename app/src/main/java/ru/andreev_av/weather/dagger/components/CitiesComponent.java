package ru.andreev_av.weather.dagger.components;

import dagger.Subcomponent;
import ru.andreev_av.weather.presentation.activities.SplashActivity;
import ru.andreev_av.weather.presentation.fragments.AddCityFragment;

@Subcomponent
public interface CitiesComponent {

    void inject(AddCityFragment fragment);

    void inject(SplashActivity activity);
}
