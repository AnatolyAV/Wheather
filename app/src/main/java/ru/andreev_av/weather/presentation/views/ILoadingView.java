package ru.andreev_av.weather.presentation.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface ILoadingView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showLoading();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideLoading();
}
