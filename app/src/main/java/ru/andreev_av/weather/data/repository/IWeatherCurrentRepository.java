package ru.andreev_av.weather.data.repository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ru.andreev_av.weather.domain.model.WeatherCurrent;

public interface IWeatherCurrentRepository {

    Observable<List<WeatherCurrent>> getWeatherCurrents(ArrayList<Integer> cityIds);

    Observable<WeatherCurrent> getWeatherCurrent(int cityId);

}
