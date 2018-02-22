package ru.andreev_av.weather.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherCurrent;
import rx.Observable;

public interface IWeatherCurrentRepository {

    Observable<List<WeatherCurrent>> getWeatherCurrents(ArrayList<Integer> cityIds);

    Observable<WeatherCurrent> getWeatherCurrent(int cityId);

}
