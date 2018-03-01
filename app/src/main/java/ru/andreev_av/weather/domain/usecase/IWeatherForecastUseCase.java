package ru.andreev_av.weather.domain.usecase;

import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherForecast;
import rx.Observable;

public interface IWeatherForecastUseCase {

    Observable<List<WeatherForecast>> loadWeatherForecast(int cityId, int countDays);
}