package ru.andreev_av.weather.domain.usecase;

import java.util.List;

import io.reactivex.Observable;
import ru.andreev_av.weather.domain.model.WeatherForecast;

public interface IWeatherForecastUseCase {

    Observable<List<WeatherForecast>> loadWeatherForecast(int cityId, int countDays);
}