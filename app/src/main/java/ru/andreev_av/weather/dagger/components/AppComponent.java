package ru.andreev_av.weather.dagger.components;

import javax.inject.Singleton;

import dagger.Component;
import ru.andreev_av.weather.dagger.modules.AppModule;
import ru.andreev_av.weather.dagger.modules.ConnectionDetectorModule;
import ru.andreev_av.weather.dagger.modules.WeatherCurrentUseCaseModule;

@Singleton
@Component(
        modules = {
                AppModule.class,
                ConnectionDetectorModule.class,
                WeatherCurrentUseCaseModule.class
        }
)
public interface AppComponent {

    CitiesComponent plusCitiesComponent();

    WeatherCurrentsComponent plusWeatherCurrentsComponent();

    WeatherCurrentComponent plusWeatherCurrentComponent();

    WeatherForecastComponent plusWeatherForecastComponent();
}