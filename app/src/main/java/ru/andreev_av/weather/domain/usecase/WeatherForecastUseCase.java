package ru.andreev_av.weather.domain.usecase;

import java.util.List;

import ru.andreev_av.weather.data.repository.IWeatherForecastRepository;
import ru.andreev_av.weather.domain.model.WeatherForecast;
import ru.andreev_av.weather.utils.RxUtils;
import rx.Observable;

public class WeatherForecastUseCase implements IWeatherForecastUseCase {

    private final IWeatherForecastRepository mRepository;

    public WeatherForecastUseCase(IWeatherForecastRepository repository) {
        mRepository = repository;
    }

    @Override
    public Observable<List<WeatherForecast>> loadWeatherForecast(int cityId, int countDays) {
        return mRepository.getWeatherForecasts(cityId, countDays)
                .compose(RxUtils.async());
    }
}