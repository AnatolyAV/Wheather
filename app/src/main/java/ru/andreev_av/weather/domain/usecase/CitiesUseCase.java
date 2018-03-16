package ru.andreev_av.weather.domain.usecase;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import ru.andreev_av.weather.data.repository.ICitiesRepository;
import ru.andreev_av.weather.domain.model.City;
import ru.andreev_av.weather.utils.RxUtils;

public class CitiesUseCase implements ICitiesUseCase {

    private final ICitiesRepository mRepository;

    private PublishSubject<String> subject = PublishSubject.create();

    public CitiesUseCase(ICitiesRepository repository) {
        mRepository = repository;
    }

    @Override
    public Observable<List<City>> initPublishSubjectForFindCities(int cityNameLettersMinForSearch) {
        return subject.debounce(500, TimeUnit.MILLISECONDS)
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
