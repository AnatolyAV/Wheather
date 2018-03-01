package ru.andreev_av.weather.presentation.views;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ru.andreev_av.weather.domain.model.City;

// TODO добавить стратегии наверно надо
public interface ICitiesView extends MvpView {
    void showCities(List<City> cities);

    void processAddedCity(City city);
}