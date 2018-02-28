package ru.andreev_av.weather.domain.usecase;

import java.util.ArrayList;
import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherCurrent;
import rx.Observable;

public interface IWeatherCurrentUseCase {
    Observable<WeatherCurrent> loadWeather(int cityId);

    Observable<List<WeatherCurrent>> loadWeather(ArrayList<Integer> cityIds);

}
