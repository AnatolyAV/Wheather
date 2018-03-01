package ru.andreev_av.weather.data.repository;

import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherForecast;
import rx.Observable;

public interface IWeatherForecastRepository {

    Observable<List<WeatherForecast>> getWeatherForecasts(int cityId, int countDays);
}