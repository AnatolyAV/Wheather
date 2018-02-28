package ru.andreev_av.weather.ui.presentation;

import ru.andreev_av.weather.domain.model.City;


public interface ICitiesPresenter {

    void initPublishSubjectForFindCities();

    void getCitiesByToWatch(boolean isToWatch);

    void findCities(String cityNameFirstLetters);

    void loadCityToWatch(City city);
}