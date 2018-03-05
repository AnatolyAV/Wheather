package ru.andreev_av.weather.presentation.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface ISwipeRefreshingView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSwipeRefreshing();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideSwipeRefreshing();
}
