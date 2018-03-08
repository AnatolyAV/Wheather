package ru.andreev_av.weather.presentation.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import ru.andreev_av.weather.domain.model.City;
import ru.andreev_av.weather.domain.usecase.ICitiesUseCase;
import ru.andreev_av.weather.presentation.views.ICitiesView;
import rx.functions.Action1;

@InjectViewState
public class CitiesPresenter extends MvpPresenter<ICitiesView> implements ICitiesPresenter {

    public final static int CITY_NAME_LETTERS_MIN_FOR_SEARCH = 3;

    private ICitiesUseCase mCitiesUseCase;

    private City mSelectedCity;

    @Inject
    public CitiesPresenter(ICitiesUseCase citiesUseCase) {
        mCitiesUseCase = citiesUseCase;
    }

    @Override
    public void initPublishSubjectForFindCities() {
        mCitiesUseCase.initPublishSubjectForFindCities(CITY_NAME_LETTERS_MIN_FOR_SEARCH)
                .subscribe(cities -> getViewState().showCities(cities));
    }

    @Override
    public void getCitiesByToWatch(boolean isToWatch) {
        mCitiesUseCase.getCitiesByToWatch(isToWatch)
                .subscribe(cities -> getViewState().showCities(cities));
    }

    @Override
    public void processEnteredCityName(CharSequence cityNameFirstLetters) {
        // необходимо, чтобы поиск не отрабатывал сразу же после выбора города
        if (mSelectedCity == null) {
            findCities(cityNameFirstLetters.toString());
        } else if (!mSelectedCity.toString().equals(cityNameFirstLetters.toString())) {
            mSelectedCity = null;
            getViewState().updateCity(null);
            getViewState().showCityName(cityNameFirstLetters.toString());
        }
    }

    @Override
    public void processClearCity() {
        mSelectedCity = null;
        getViewState().updateCity(null);
        getViewState().showCityName(null);
    }

    @Override
    public void findCities(String cityNameFirstLetters) {
        mCitiesUseCase.findCities(cityNameFirstLetters);
    }

    @Override
    public void processSelectedCity(City city) {
        mSelectedCity = city;
        getViewState().updateCity(city);
        getViewState().showCityName(city.toString());
    }

    @Override
    public void loadCityToWatch(City city) {
        if (city != null) {
            mCitiesUseCase.loadCityToWatch(city)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean successAddingCity) {
                            if (successAddingCity) {
                                getViewState().processAddedCity(city);
                            }
                        }
                    });
        } else {
            getViewState().showErrorSelectedCity();
        }
    }
}