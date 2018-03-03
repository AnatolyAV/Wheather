package ru.andreev_av.weather.presentation.views;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherCurrent;

public interface IWeatherCurrentView extends ILoadingView {

    @StateStrategyType(SingleStateStrategy.class)
    void showWeatherCurrents(List<WeatherCurrent> weatherCurrents);

    @StateStrategyType(SkipStrategy.class)
    void showWeatherCurrent(WeatherCurrent weatherCurrent);

    @StateStrategyType(SkipStrategy.class)
    void showErrorWeatherCurrent();

    @StateStrategyType(SkipStrategy.class)
    void showErrorWeatherCurrents();

    @StateStrategyType(SkipStrategy.class)
    void showNotConnection();
}
