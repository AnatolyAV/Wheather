package ru.andreev_av.weather.ui.presentation;

public interface IWeatherForecastPresenter {

    void loadWeatherForecast(int cityId, int countDays, boolean isRefreshing);
}