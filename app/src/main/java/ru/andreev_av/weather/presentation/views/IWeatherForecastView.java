package ru.andreev_av.weather.presentation.views;

import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherForecast;

// TODO добавить стратегии наверно надо
public interface IWeatherForecastView extends ILoadingView {

    void showWeatherForecasts(List<WeatherForecast> weatherForecasts);

    void showErrorWeatherForecasts();

    void showNotConnection();

    void updateButtonState(boolean isUpdate);
}