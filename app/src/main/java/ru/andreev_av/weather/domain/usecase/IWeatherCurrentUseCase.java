package ru.andreev_av.weather.domain.usecase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ru.andreev_av.weather.domain.model.WeatherCurrent;

public interface IWeatherCurrentUseCase {
    Observable<WeatherCurrent> loadWeather(int cityId);

    Observable<List<WeatherCurrent>> loadWeather(ArrayList<Integer> cityIds);

}
