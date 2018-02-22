package ru.andreev_av.weather.ui.presentation;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ru.andreev_av.weather.domain.model.City;

// TODO добавить стратегии наверно надо
public interface ICityView extends MvpView {
    void showCities(List<City> cities);

    void processAddedCity(City city);
}