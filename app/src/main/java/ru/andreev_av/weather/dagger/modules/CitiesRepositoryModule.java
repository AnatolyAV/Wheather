package ru.andreev_av.weather.dagger.modules;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.dagger.scope.ForCities;
import ru.andreev_av.weather.data.db.ICityDao;
import ru.andreev_av.weather.data.repository.CitiesRepository;
import ru.andreev_av.weather.data.repository.ICitiesRepository;

@Module(includes = {CityDaoModule.class})
public class CitiesRepositoryModule {

    @Provides
    @ForCities
    public ICitiesRepository provideCitiesRepository(ICityDao cityDao) {
        return new CitiesRepository(cityDao);
    }

}
