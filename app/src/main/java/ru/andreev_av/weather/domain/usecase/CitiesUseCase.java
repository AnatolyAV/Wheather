package ru.andreev_av.weather.domain.usecase;

import java.util.ArrayList;
import java.util.List;

import ru.andreev_av.weather.data.model.City;
import ru.andreev_av.weather.data.repository.ICitiesRepository;
import ru.andreev_av.weather.domain.model.WeatherCurrent;
import rx.Observable;

public class CitiesUseCase implements ICitiesUseCase {

    private final ICitiesRepository mRepository;

    public CitiesUseCase(ICitiesRepository repository) {
        mRepository = repository;
    }

    @Override
    public Observable<List<WeatherCurrent>> loadWeather(int cityId) {
        return mRepository.getWeatherCurrent(cityId)
                .toList();
    }

    @Override
    public Observable<List<WeatherCurrent>> loadWeather(ArrayList<Integer> cityIds) {
        return mRepository.getWeatherCurrents(cityIds);
    }

    @Override
    public void loadCityToWatch(City city) {

    }
}
