package ru.andreev_av.weather.presentation.presenters;

import java.util.ArrayList;

public interface IWeatherCurrentPresenter {

    void loadWeather(int cityId, boolean isRefreshing, boolean isSwipeRefreshing);

    void loadWeather(ArrayList<Integer> cityIds, boolean isRefreshing);
}
