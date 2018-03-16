package ru.andreev_av.weather.data.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import ru.andreev_av.weather.data.cache.WeatherCurrentCacheTransformer;
import ru.andreev_av.weather.data.cache.WeatherCurrentsCacheTransformer;
import ru.andreev_av.weather.data.db.IWeatherCurrentDao;
import ru.andreev_av.weather.data.model.WeatherCurrentListModel;
import ru.andreev_av.weather.data.network.OwmService;
import ru.andreev_av.weather.domain.model.WeatherCurrent;
import ru.andreev_av.weather.presentation.App;
import ru.andreev_av.weather.utils.StringUtils;

public class WeatherCurrentRepository implements IWeatherCurrentRepository {

    private IWeatherCurrentDao mWeatherCurrentDao;

    public WeatherCurrentRepository(IWeatherCurrentDao weatherCurrentDao) {
        mWeatherCurrentDao = weatherCurrentDao;
    }

    @Override
    public Observable<List<WeatherCurrent>> getWeatherCurrents(ArrayList<Integer> cityIds) {
        return App.getOwmService()
                .getWeatherCurrent(StringUtils.joinIds(cityIds), "metric", Locale.getDefault().getLanguage(), OwmService.API_KEY)
                .map(WeatherCurrentListModel::getWeatherCurrentModelList)
                .compose(new WeatherCurrentsCacheTransformer(mWeatherCurrentDao));
    }

    @Override
    public Observable<WeatherCurrent> getWeatherCurrent(int cityId) {
        return App.getOwmService()
                .getWeatherCurrent(cityId, "metric", Locale.getDefault().getLanguage(), OwmService.API_KEY)
                .compose(new WeatherCurrentCacheTransformer(mWeatherCurrentDao, cityId));
    }
}
