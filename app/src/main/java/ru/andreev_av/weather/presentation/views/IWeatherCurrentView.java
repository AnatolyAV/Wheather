package ru.andreev_av.weather.presentation.views;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.andreev_av.weather.domain.model.WeatherCurrent;

@StateStrategyType(SkipStrategy.class)
public interface IWeatherCurrentView extends ISwipeRefreshingView, IButtonRefreshingView, INotConnectionView {

    @StateStrategyType(SingleStateStrategy.class)
    void showWeatherCurrent(WeatherCurrent weatherCurrent);

    void showErrorWeatherCurrent();
}
