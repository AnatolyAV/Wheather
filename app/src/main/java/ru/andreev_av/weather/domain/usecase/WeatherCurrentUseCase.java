package ru.andreev_av.weather.domain.usecase;

import java.util.ArrayList;
import java.util.List;

import ru.andreev_av.weather.data.repository.IWeatherCurrentRepository;
import ru.andreev_av.weather.domain.model.WeatherCurrent;
import rx.Observable;

public class WeatherCurrentUseCase implements IWeatherCurrentUseCase {

    private final IWeatherCurrentRepository mRepository;

    public WeatherCurrentUseCase(IWeatherCurrentRepository repository) {
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
}
