package ru.andreev_av.weather.ui.presentation;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.concurrent.TimeUnit;

import ru.andreev_av.weather.domain.model.City;
import ru.andreev_av.weather.domain.usecase.ICitiesUseCase;
import ru.andreev_av.weather.utils.RxUtils;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

@InjectViewState
public class CitiesPresenter extends MvpPresenter<ICitiesView> implements ICitiesPresenter {

    public final static int CITY_LETTERS_MIN_FOR_SEARCH = 3;

    private ICitiesUseCase mCitiesUseCase;

    private PublishSubject<String> subject = PublishSubject.create();

    public CitiesPresenter() {
    }

    public CitiesPresenter(ICitiesUseCase citiesUseCase) {
        mCitiesUseCase = citiesUseCase;
    }

    public void setCitiesUseCase(ICitiesUseCase сitiesUseCase) {
        mCitiesUseCase = сitiesUseCase;
    }

    @Override
    public void init() {
        subject.debounce(500, TimeUnit.MILLISECONDS)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String cityNameFirstLetters) {
                        if (cityNameFirstLetters.isEmpty() || cityNameFirstLetters.length() < CITY_LETTERS_MIN_FOR_SEARCH) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                })
                .distinctUntilChanged()
                .map(String::toLowerCase)
                .switchMap(cityNameFirstLetters -> mCitiesUseCase.findCities(cityNameFirstLetters))
                .compose(RxUtils.async())
                .subscribe(cities -> getViewState().showCities(cities));
    }


    @Override
    public void findCities(String cityNameFirstLetters) {
        subject.onNext(cityNameFirstLetters);
    }

    @Override
    public void loadCityToWatch(City city) {
        mCitiesUseCase.loadCityToWatch(city)
                .compose(RxUtils.async())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean successAddingCity) {
                        if (successAddingCity) {
                            getViewState().processAddedCity(city);
                        }
                    }
                });
    }
}