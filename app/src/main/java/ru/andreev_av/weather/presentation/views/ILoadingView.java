package ru.andreev_av.weather.presentation.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.andreev_av.weather.presentation.enums.RefreshingType;

public interface ILoadingView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showLoading(RefreshingType refreshingType);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideLoading(RefreshingType refreshingType);
}
