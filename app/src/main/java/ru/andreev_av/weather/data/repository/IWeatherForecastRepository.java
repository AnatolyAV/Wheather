package ru.andreev_av.weather.data.repository;

import java.util.List;

import io.reactivex.Observable;
import ru.andreev_av.weather.domain.model.WeatherForecast;

public interface IWeatherForecastRepository {

    Observable<List<WeatherForecast>> getWeatherForecasts(int cityId, int countDays);
}