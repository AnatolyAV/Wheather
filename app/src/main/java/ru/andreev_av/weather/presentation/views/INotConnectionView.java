package ru.andreev_av.weather.presentation.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface INotConnectionView extends MvpView {

    @StateStrategyType(SkipStrategy.class)
    void showNotConnection();
}
