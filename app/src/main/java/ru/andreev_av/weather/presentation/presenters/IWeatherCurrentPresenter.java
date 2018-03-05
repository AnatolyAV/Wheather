package ru.andreev_av.weather.presentation.presenters;

import ru.andreev_av.weather.presentation.enums.RefreshingType;

public interface IWeatherCurrentPresenter {

    void loadWeather(int cityId);

    void loadWeather(int cityId, RefreshingType refreshingType);
}
