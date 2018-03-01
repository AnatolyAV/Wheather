package ru.andreev_av.weather.data.db;

import java.util.List;

import ru.andreev_av.weather.data.model.City;
import ru.andreev_av.weather.data.model.WeatherForecastModel;
import ru.andreev_av.weather.domain.model.WeatherForecast;

public interface IWeatherForecastDao {

    void insertOrUpdate(WeatherForecastModel weatherForecastModel);

    void insertOrUpdate(ru.andreev_av.weather.data.model.WeatherForecast weatherForecast, City city);

    List<WeatherForecast> getWeatherForecastsByCityId(int cityId, int countDays);
}
