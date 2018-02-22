package ru.andreev_av.weather.ui.presentation;

import ru.andreev_av.weather.domain.model.City;


public interface ICityPresenter {

    void init();

    void findCities(String cityNameFirstLetters);

    void loadCityToWatch(City city);
}