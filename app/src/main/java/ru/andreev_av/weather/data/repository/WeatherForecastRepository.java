package ru.andreev_av.weather.data.repository;

import java.util.List;
import java.util.Locale;

import ru.andreev_av.weather.data.cache.WeatherForecastCacheTransformer;
import ru.andreev_av.weather.data.db.IWeatherForecastDao;
import ru.andreev_av.weather.data.network.OwmService;
import ru.andreev_av.weather.domain.model.WeatherForecast;
import ru.andreev_av.weather.presentation.App;
import rx.Observable;

public class WeatherForecastRepository implements IWeatherForecastRepository {

    private IWeatherForecastDao mWeatherForecastDao;

    public WeatherForecastRepository(IWeatherForecastDao weatherForecastDao) {
        mWeatherForecastDao = weatherForecastDao;
    }

    @Override
    public Observable<List<WeatherForecast>> getWeatherForecasts(int cityId, int countDays) {
        return App.getOwmService()
                .getWeatherForecasts(cityId, countDays, "metric", Locale.getDefault().getLanguage(), OwmService.API_KEY)
                .compose(new WeatherForecastCacheTransformer(mWeatherForecastDao, cityId, countDays));

    }
}