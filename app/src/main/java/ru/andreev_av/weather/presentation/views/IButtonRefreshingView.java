package ru.andreev_av.weather.presentation.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface IButtonRefreshingView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showButtonRefreshing();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideButtonRefreshing();
}
