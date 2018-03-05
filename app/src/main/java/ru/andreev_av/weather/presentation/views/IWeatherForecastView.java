package ru.andreev_av.weather.presentation.views;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherForecast;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface IWeatherForecastView extends ILoadingView, IButtonRefreshingView {

    @StateStrategyType(SingleStateStrategy.class)
    void showWeatherForecasts(List<WeatherForecast> weatherForecasts);

    @StateStrategyType(SkipStrategy.class)
    void showErrorWeatherForecasts();

    @StateStrategyType(SkipStrategy.class)
    void showNotConnection();

    void setCountDays(int countDays);

    void showCountDaysImageThree();

    void showCountDaysImageSeven();
}