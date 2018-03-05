package ru.andreev_av.weather.presentation.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.andreev_av.weather.domain.model.City;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ICitiesView extends MvpView {

    void showCities(List<City> cities);

    void showSelectedCity(City city);

    @StateStrategyType(SkipStrategy.class)
    void processAddedCity(City city);
}