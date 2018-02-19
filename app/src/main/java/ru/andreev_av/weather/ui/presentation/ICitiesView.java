package ru.andreev_av.weather.ui.presentation;

import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherCurrent;

/**
 * Created by Tolik on 01.02.2018.
 */
// TODO добавить стратегии наверно надо
public interface ICitiesView extends ILoadingView {

    void showWeatherCurrents(List<WeatherCurrent> weatherCurrents);

    void showWeatherCurrent(List<WeatherCurrent> weatherCurrent);

    void showErrorWeatherCurrent();

    void showErrorWeatherCurrents();

    void showNotConnection();

    void updateButtonState(boolean isUpdate);
}
