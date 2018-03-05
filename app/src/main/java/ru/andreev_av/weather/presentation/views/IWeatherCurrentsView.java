package ru.andreev_av.weather.presentation.views;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherCurrent;

@StateStrategyType(SkipStrategy.class)
public interface IWeatherCurrentsView extends ILoadingView, IButtonRefreshingView {

    @StateStrategyType(SingleStateStrategy.class)
    void showWeatherCurrents(List<WeatherCurrent> weatherCurrents);

    void showErrorWeatherCurrents();

    void showNotConnection();
}
