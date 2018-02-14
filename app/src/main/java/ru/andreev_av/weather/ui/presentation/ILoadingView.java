package ru.andreev_av.weather.ui.presentation;

import com.arellomobile.mvp.MvpView;

/**
 * Created by Tolik on 01.02.2018.
 */

public interface ILoadingView extends MvpView {

    void showLoading();

    void hideLoading();
}
