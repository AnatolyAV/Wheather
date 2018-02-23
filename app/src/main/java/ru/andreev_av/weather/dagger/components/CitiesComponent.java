package ru.andreev_av.weather.dagger.components;

import dagger.Subcomponent;
import ru.andreev_av.weather.ui.activities.SplashActivity;
import ru.andreev_av.weather.ui.fragments.AddCityFragment;

@Subcomponent
public interface CitiesComponent {

    void inject(AddCityFragment fragment);

    void inject(SplashActivity activity);
}
