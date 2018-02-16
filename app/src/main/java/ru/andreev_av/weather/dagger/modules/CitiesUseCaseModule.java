package ru.andreev_av.weather.dagger.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.andreev_av.weather.data.repository.ICitiesRepository;
import ru.andreev_av.weather.domain.usecase.CitiesUseCase;
import ru.andreev_av.weather.domain.usecase.ICitiesUseCase;

@Module(includes = {CitiesRepositoryModule.class})
public class CitiesUseCaseModule {

    @Provides
    @Singleton
    public ICitiesUseCase provideCitiesUseCase(ICitiesRepository citiesRepository) {
        return new CitiesUseCase(citiesRepository);
    }
}
