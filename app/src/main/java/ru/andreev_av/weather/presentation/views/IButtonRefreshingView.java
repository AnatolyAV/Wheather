package ru.andreev_av.weather.presentation.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface IButtonRefreshingView extends MvpView {

    void showButtonRefreshing();

    void hideButtonRefreshing();
}
