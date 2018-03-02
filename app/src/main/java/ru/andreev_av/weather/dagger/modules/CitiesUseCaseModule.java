package ru.andreev_av.weather.dagger.modules;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.dagger.scope.ForCities;
import ru.andreev_av.weather.data.repository.ICitiesRepository;
import ru.andreev_av.weather.domain.usecase.CitiesUseCase;
import ru.andreev_av.weather.domain.usecase.ICitiesUseCase;

@Module(includes = {CitiesRepositoryModule.class})
public class CitiesUseCaseModule {

    @Provides
    @ForCities
    public ICitiesUseCase provideCitiesUseCase(ICitiesRepository citiesRepository) {
        return new CitiesUseCase(citiesRepository);
    }

}
