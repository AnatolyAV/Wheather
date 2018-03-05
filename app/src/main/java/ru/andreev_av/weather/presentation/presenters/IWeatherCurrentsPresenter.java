package ru.andreev_av.weather.presentation.presenters;

import java.util.ArrayList;

import ru.andreev_av.weather.presentation.enums.RefreshingType;

public interface IWeatherCurrentsPresenter {

    void loadWeather(ArrayList<Integer> cityIds);

    void loadWeather(ArrayList<Integer> cityIds, RefreshingType refreshingType);
}
