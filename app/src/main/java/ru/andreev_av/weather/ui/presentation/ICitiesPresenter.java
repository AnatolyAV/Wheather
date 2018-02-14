package ru.andreev_av.weather.ui.presentation;

import java.util.ArrayList;

import ru.andreev_av.weather.data.model.City;

/**
 * Created by Tolik on 01.02.2018.
 */

public interface ICitiesPresenter {

    void loadWeather(int cityId);

    void loadWeather(ArrayList<Integer> cityIds);

    void loadCityToWatch(City city);

    void checkNetworkAvailableAndConnected();
}
