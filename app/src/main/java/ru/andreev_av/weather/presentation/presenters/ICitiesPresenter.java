package ru.andreev_av.weather.presentation.presenters;

import ru.andreev_av.weather.domain.model.City;


public interface ICitiesPresenter {

    void initPublishSubjectForFindCities();

    void getCitiesByToWatch(boolean isToWatch);

    void findCities(String cityNameFirstLetters);

    void processSelectedCity(City city);

    void loadCityToWatch(City city);
}