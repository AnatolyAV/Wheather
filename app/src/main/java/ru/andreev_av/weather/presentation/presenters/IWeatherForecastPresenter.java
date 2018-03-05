package ru.andreev_av.weather.presentation.presenters;

import ru.andreev_av.weather.presentation.enums.RefreshingType;

public interface IWeatherForecastPresenter {

    void loadWeatherForecast(int cityId, int countDays);

    void loadWeatherForecast(int cityId, int countDays, RefreshingType refreshingType);

    void updateCountDaysAndItsImage(boolean isClickingImage);
}