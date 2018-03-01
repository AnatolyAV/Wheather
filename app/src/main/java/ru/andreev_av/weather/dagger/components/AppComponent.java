package ru.andreev_av.weather.dagger.components;

import javax.inject.Singleton;

import dagger.Component;
import ru.andreev_av.weather.dagger.modules.AppModule;
import ru.andreev_av.weather.dagger.modules.CitiesUseCaseModule;
import ru.andreev_av.weather.dagger.modules.ConnectionDetectorModule;

@Singleton
@Component(
        modules = {
                AppModule.class,
                ConnectionDetectorModule.class,
                CitiesUseCaseModule.class
        }
)
public interface AppComponent {

    CitiesComponent plusCitiesComponent();

    WeatherCurrentComponent plusWeatherCurrentComponent();

    WeatherForecastComponent plusWeatherForecastComponent();
}