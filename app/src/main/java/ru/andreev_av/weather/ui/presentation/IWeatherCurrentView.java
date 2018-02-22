package ru.andreev_av.weather.ui.presentation;

import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherCurrent;

// TODO добавить стратегии наверно надо
public interface IWeatherCurrentView extends ILoadingView {

    void showWeatherCurrents(List<WeatherCurrent> weatherCurrents);

    void showWeatherCurrent(List<WeatherCurrent> weatherCurrent);

    void showErrorWeatherCurrent();

    void showErrorWeatherCurrents();

    void showNotConnection();

    void updateButtonState(boolean isUpdate);
}
