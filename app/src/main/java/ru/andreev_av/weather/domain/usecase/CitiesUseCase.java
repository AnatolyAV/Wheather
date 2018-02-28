package ru.andreev_av.weather.domain.usecase;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.andreev_av.weather.data.repository.ICitiesRepository;
import ru.andreev_av.weather.domain.model.City;
import ru.andreev_av.weather.utils.RxUtils;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

public class CitiesUseCase implements ICitiesUseCase {

    private final ICitiesRepository mRepository;

    private PublishSubject<String> subject = PublishSubject.create();

    public CitiesUseCase(ICitiesRepository repository) {
        mRepository = repository;
    }

    @Override
    public Observable<List<City>> initPublishSubjectForFindCities(int cityNameLettersMinForSearch) {
        return subject.debounce(500, TimeUnit.MILLISECONDS)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String cityNameFirstLetters) {
                        if (cityNameFirstLetters.isEmpty() || cityNameFirstLetters.length() < cityNameLettersMinForSearch) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                })
                .distinctUntilChanged()
                .map(String::toLowerCase)
                .switchMap(cityNameFirstLetters -> mRepository.findCities(cityNameFirstLetters))
                .compose(RxUtils.async());
    }

    @Override
    public Observable<List<City>> getCitiesByToWatch(boolean isToWatch) {
        return mRepository.getCitiesByToWatch(isToWatch)
                .compose(RxUtils.async());
    }

    @Override
    public void findCities(String cityNameFirstLetters) {
        subject.onNext(cityNameFirstLetters);
    }

    @Override
    public Observable<Boolean> loadCityToWatch(City city) {
        return mRepository.loadCityToWatch(city)
                .compose(RxUtils.async());
    }
}
