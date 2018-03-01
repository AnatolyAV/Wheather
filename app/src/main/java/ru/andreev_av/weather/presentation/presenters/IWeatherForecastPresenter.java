package ru.andreev_av.weather.presentation.presenters;

public interface IWeatherForecastPresenter {

    void loadWeatherForecast(int cityId, int countDays, boolean isRefreshing);
}