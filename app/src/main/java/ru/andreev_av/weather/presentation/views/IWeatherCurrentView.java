package ru.andreev_av.weather.presentation.views;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherCurrent;

@StateStrategyType(SkipStrategy.class)
public interface IWeatherCurrentView extends ILoadingView, ISwipeRefreshingView, IButtonRefreshingView {

    @StateStrategyType(SingleStateStrategy.class)
    void showWeatherCurrents(List<WeatherCurrent> weatherCurrents);

    void showWeatherCurrent(WeatherCurrent weatherCurrent);

    void showErrorWeatherCurrent();

    void showErrorWeatherCurrents();

    void showNotConnection();
}
