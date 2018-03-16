package ru.andreev_av.weather.domain.usecase;

import java.util.List;

import io.reactivex.Observable;
import ru.andreev_av.weather.domain.model.City;

public interface ICitiesUseCase {

    Observable<List<City>> initPublishSubjectForFindCities(int cityNameLettersMinForSearch);

    Observable<List<City>> getCitiesByToWatch(boolean isToWatch);

    void findCities(String cityNameFirstLetters);

    Observable<Boolean> loadCityToWatch(City city);
}
