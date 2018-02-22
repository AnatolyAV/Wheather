package ru.andreev_av.weather.ui.presentation;

import java.util.ArrayList;

/**
 * Created by Tolik on 01.02.2018.
 */

public interface IWeatherCurrentPresenter {

    void loadWeather(int cityId);

    void loadWeather(ArrayList<Integer> cityIds, boolean isCallFromMenuItemRefresh);
}
